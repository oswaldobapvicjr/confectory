/*
 * Copyright 2022 obvj.net
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.obvj.confectory.merger;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toMap;
import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;
import static java.util.Objects.requireNonNull;

import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jayway.jsonpath.InvalidPathException;

import net.obvj.confectory.Configuration;
import net.obvj.confectory.mapper.JSONObjectMapper;
import net.obvj.confectory.util.ConfigurationComparator;
import net.obvj.confectory.util.JsonPathExpression;
import net.obvj.confectory.util.JsonProvider;

/**
 * An abstract {@code ConfigurationMerger} that combines two {@link Configuration} objects
 * of type JSON.
 * <p>
 * The resulting JSON document from the merge operation shall contain all exclusive
 * objects from source documents and in case of In case of key collisions (i.e., the same
 * key appears in both documents), the following rules will be applied:
 * <ul>
 * <li>for simple values, such as strings, numbers and boolean values, the value from the
 * highest-precedence {@code Configuration} will be selected;</li>
 * <li>if the value is a JSON object in <b>both</b> JSON sources, the two objects will be
 * merged recursively; if the types are not compatible (e.g.: JSON object in one side and
 * simple value or array in the other), then a copy of the object from the
 * highest-precedence {@code Configuration} will be selected as fallback;</li>
 * <li>if the value is a JSON array in <b>both</b> JSON sources, then all elements from
 * both two arrays will be copied <b>distinctively</b> (i.e., repeated elements will not
 * be copied to the resulting JSON document); if the types are not compatible (e.g.: JSON
 * array in one side and simple value or complex object in the other), then a copy of the
 * object from the highest-precedence {@code Configuration} will be selected as
 * fallback</li>
 * </ul>
 * <p>
 * A special solution for merging arrays of distinct objects with a specific key can be
 * created using the optional constructor
 * {@link #AbstractJSONConfigurationMerger(JsonProvider, Map)}.
 *
 * @see ConfigurationMerger
 * @author oswaldo.bapvic.jr (Oswaldo Junior)
 * @since 2.1.0
 */
public abstract class AbstractJSONConfigurationMerger<T> extends AbstractConfigurationMerger<T>
{
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractJSONConfigurationMerger.class);

    private final Map<JsonPathExpression, String> distinctObjectKeysInsideArrays;

    private final JsonProvider jsonProvider;


    /**
     * Creates a new JSON Configuration Merger for a specific provider.
     *
     * @param jsonProvider the {@link JsonProvider} to use; not {@code null}
     *
     * @throws NullPointerException if the specified JsonProvider is null
     */
    public AbstractJSONConfigurationMerger(JsonProvider jsonProvider)
    {
        this(jsonProvider, Collections.emptyMap());
    }

    /**
     * Creates a new JSON Configuration Merger for a specific provider and a preset map of
     * distinct keys.
     * <p>
     * The {@code distinctObjectKeysInsideArrays} map shall be used to specify key objects
     * inside arrays.
     * <p>
     * For example, consider the following document: <blockquote>
     *
     * <pre>
     * {
     *   "params": [
     *     {
     *       "param": "name",
     *       "value": "John Doe"
     *     },
     *     {
     *       "param": "age",
     *       "value": 33
     *     }
     *   ]
     * }
     * </pre>
     *
     * </blockquote>
     *
     * <p>
     * A map entry with the key {@code "$.params"} associated with the {@code "param"} value
     * tells the algorithm to check for distinct objects identified by the value inside the
     * {@code "param"} field during the merge of the {@code "$.params"} array.
     * <p>
     * In other words, if two JSON documents contain different objects identified by the same
     * {@code "param"} attribute inside the array returned by the {@code JsonPath} expression
     * {@code "$.params"}, then only the object from the {@link Configuration} with the
     * highest precedence will be selected.
     * <p>
     * A {@code JsonPath} expression can be specified using either dot- or bracket-notation,
     * but complex expressions containing filters, script, subscript, or union operations, are
     * not supported.
     *
     * @param jsonProvider                   the {@link JsonProvider} to use; not {@code null}
     * @param distinctObjectKeysInsideArrays a map that associates JsonPath expressions and
     *                                       distinct keys during the merge of an array;
     *                                       {@code null} is allowed
     *
     * @throws NullPointerException     if the specified JsonProvider is null
     * @throws IllegalArgumentException if the map contains a null or empty expression
     * @throws InvalidPathException     if the specified JsonPath expression is invalid
     */
    public AbstractJSONConfigurationMerger(JsonProvider jsonProvider,
            Map<String, String> distinctObjectKeysInsideArrays)
    {
        this.jsonProvider = requireNonNull(jsonProvider, "The JsonProvider cannot be null");
        this.distinctObjectKeysInsideArrays = parseDistinctKeys(distinctObjectKeysInsideArrays);
    }

    private Map<JsonPathExpression, String> parseDistinctKeys(Map<String, String> map)
    {
        Map<String, String> safeMap = defaultIfNull(map, Collections.<String, String>emptyMap());
        return safeMap.entrySet().stream()
                .collect(toMap(entry -> new JsonPathExpression(entry.getKey()), Map.Entry::getValue));
    }

    @Override
    T doMerge(Configuration<T> config1, Configuration<T> config2)
    {
        List<T> sortedJSONObjects = getSortedJSONObjects(config1, config2);
        JSONMerger merger = new JSONMerger(JsonPathExpression.ROOT,
                distinctObjectKeysInsideArrays, jsonProvider);
        return (T) merger.merge(sortedJSONObjects.get(0), sortedJSONObjects.get(1));
    }

    /**
     * An internal class that merges two JSON objects with recursion support.
     */
    static class JSONMerger
    {
        private final JsonPathExpression absolutePath;
        private final Map<JsonPathExpression, String> distinctObjectKeysByArrays;
        private final JsonProvider jsonProvider;

        /**
         * Creates a new {@link JSONObjectMapper} for an absolute path.
         *
         * @param absolutePath                   the absolute path of the current JSON object or
         *                                       array inside the root JSON object}; not
         *                                       {@code null}
         * @param distinctObjectKeysInsideArrays a map that associates JsonPath expressions inside
         *                                       the JSON object and distinct keys for object
         *                                       identification during the merge of an array; not
         *                                       {@code null}
         * @param jsonProvider                   the {@link JsonProvider} to use; not {@code null}
         */
        JSONMerger(JsonPathExpression absolutePath,
                Map<JsonPathExpression, String> distinctObjectKeysInsideArrays, JsonProvider jsonProvider)
        {
            this.absolutePath = absolutePath;
            this.distinctObjectKeysByArrays = distinctObjectKeysInsideArrays;
            this.jsonProvider = jsonProvider;
        }

        /**
         * Merges two JSON objects.
         *
         * @param json1 the highest-precedence JSON object
         * @param json2 the lowest-precedence JSON object
         * @return a combination of the two JSON objects
         */
        private Object merge(Object json1, Object json2)
        {
            LOGGER.debug("Merging object on path: {}", absolutePath);

            if (jsonProvider.isEmpty(json2))
            {
                return jsonProvider.newJsonObject(json1);
            }

            Object result = jsonProvider.newJsonObject();

            // First iterate through the first json
            for (Entry<String, Object> entry : jsonProvider.entrySet(json1))
            {
                String key = entry.getKey();
                Object value1 = entry.getValue();
                Object value2 = jsonProvider.get(json2, key);

                if (jsonProvider.isJsonObject(value1))
                {
                    JSONMerger merger = new JSONMerger(absolutePath.appendChild(key),
                            distinctObjectKeysByArrays, jsonProvider);
                    jsonProvider.put(result, key, merger.mergeSafely(value1, value2));
                }
                else if (jsonProvider.isJsonArray(value1))
                {
                    JSONMerger merger = new JSONMerger(absolutePath.appendChild(key),
                            distinctObjectKeysByArrays, jsonProvider);
                    jsonProvider.put(result, key, merger.mergeArray(value1, value2));
                }
                else
                {
                    jsonProvider.put(result, key, value1); // Get from the highest-precedence json
                }
            }

            // Then iterate through the second json to find additional keys
            jsonProvider.forEachEntryInJsonObject(json2, (key, value) -> jsonProvider.putIfAbsent(result, key, value));
            return result;
        }

        /**
         * Support method for type-safety during the merge of a JSON object.
         *
         * @param json   the highest-precedence JSON object to be merged
         * @param object the lowest-precedence object to be merged
         * @return a combination of the two objects, provided that the second object is a JSON
         *         object too
         */
        private Object mergeSafely(Object json, Object object)
        {
            if (jsonProvider.isJsonObject(object))
            {
                return merge(json, object);
            }
            // The second object is either null or has an incompatible type,
            // so simply assume object with the highest precedence.
            // Create a copy of it for safety.
            return jsonProvider.newJsonObject(json);
        }

        /**
         * Support method for type-safety during the merge a JSON array.
         *
         * @param array  the highest-precedence JSON array to be merged
         * @param object the lowest-precedence object to be merged
         * @return a combination of the two objects, provided that the second object is a JSON
         *         array too
         */
        private Object mergeArray(Object array, Object object)
        {
            LOGGER.debug("Merging array on path: {}", absolutePath);
            if (jsonProvider.isJsonArray(object))
            {
                return mergeArraySafely(array, object);
            }
            // The second object is either null or has an incompatible type,
            // so simply assume the array from the highest-precedence object.
            // Create a copy of it for safety.
            return jsonProvider.newJsonArray(array);
        }

        /**
         * Merges two JSON array instances.
         *
         * @param array1 the highest-precedence JSON array
         * @param array2 the lowest-precedence JSON array
         * @return a combination of the two JSON arrays
         */
        private Object mergeArraySafely(Object array1, Object array2)
        {
            // The 1st array is always the highest-precedence one
            Object result = jsonProvider.newJsonArray(array1);

            String distinctKey = distinctObjectKeysByArrays.get(absolutePath);
            if (distinctKey != null)
            {
                // Here we add objects from the 2nd array only if they are not present in the 1st one
                // Because the user specified a distinct key, then use it to find the "equal" objects.
                jsonProvider.forEachElementInArray(array2,
                        object -> addDistinctObject(object, distinctKey, result));
            }
            else
            {
                jsonProvider.forEachElementInArray(array2, object ->
                {
                    addDistinctObjectToArray(result, object);
                });
            }
            return result;
        }

        private void addDistinctObjectToArray(Object array, Object object)
        {
            if (!jsonProvider.arrayContains(array, object))
            {
                jsonProvider.add(array, object);
            }
        }

        /**
         * Adds a distinct object, identified by a specific key, to the target array.
         *
         * @param object the object to be added, given it is not already is the {@code array}
         * @param key    a distinctive key inside the JSON object to be used for "equal" objects
         *               identification in the target array
         * @param array  the array to which the object will be added, provided that it contains no
         *               other object with the same value for the specified {@code key}
         */
        private void addDistinctObject(Object object, String key, Object array)
        {
            if (jsonProvider.isJsonObject(object))
            {
                Object value = jsonProvider.get(object, key);
                if (value != null && !findMatchingObjectOnArray(value, key, array).isPresent())
                {
                    jsonProvider.add(array, object);
                }
            }
            else
            {
                addDistinctObjectToArray(array, object);
            }
        }

        /**
         * @param value the value to search
         * @param key   a distinctive key inside the JSON object to be used for "equal" objects
         *              identification in the given array
         * @param array the array to be searched
         * @return the first JSON object matching the specified criteria
         */
        private Optional<Object> findMatchingObjectOnArray(Object value, String key, Object array)
        {
            return jsonProvider.stream(array).filter(jsonProvider::isJsonObject)
                    .filter(json -> value.equals(jsonProvider.get(json, key))).findFirst();
        }

    }

    private List<T> getSortedJSONObjects(Configuration<T> config1, Configuration<T> config2)
    {
        return asList(config1, config2).stream().sorted(new ConfigurationComparator())
                .map(this::getJSONObjectSafely).collect(Collectors.toList());
    }

    private T getJSONObjectSafely(Configuration<T> config)
    {
        return getBeanSafely(config, () -> (T) jsonProvider.newJsonObject());
    }

}
