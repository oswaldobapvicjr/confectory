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
import static java.util.Arrays.stream;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toMap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.obvj.confectory.Configuration;
import net.obvj.confectory.mapper.JSONObjectMapper;
import net.obvj.confectory.util.ConfigurationComparator;
import net.obvj.confectory.util.JsonPathExpression;
import net.obvj.confectory.util.JsonProvider;

/**
 * A generic {@code ConfigurationMerger} that combines two {@link Configuration} objects
 * of type JSON.
 * <p>
 * The operation is provider-agnostic and depends on a specialized {@link JsonProvider}
 * which must be specified via constructor.
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
 * <b>Note: </b> For advanced merge options, refer to {@link JsonMergeOption}.
 *
 * @see ConfigurationMerger
 * @see JsonProvider
 * @see JsonMergeOption
 *
 * @author oswaldo.bapvic.jr (Oswaldo Junior)
 * @since 2.1.0
 */
public class GenericJsonConfigurationMerger<T> extends AbstractConfigurationMerger<T>
{
    private static final Logger LOGGER = LoggerFactory.getLogger(GenericJsonConfigurationMerger.class);

    private final JsonProvider jsonProvider;

    /**
     * Creates a new JSON Configuration Merger for a specific provider.
     *
     * @param jsonProvider the {@link JsonProvider} to use; not {@code null}
     * @throws NullPointerException if the specified JsonProvider is null
     */
    public GenericJsonConfigurationMerger(JsonProvider jsonProvider)
    {
        this.jsonProvider = requireNonNull(jsonProvider, "The JsonProvider cannot be null");
    }

    private Map<JsonPathExpression, List<String>> parseDistinctKeys(MergeOption[] mergeOptions)
    {
        return stream(mergeOptions)
                .filter(JsonMergeOption.class::isInstance)
                .map(JsonMergeOption.class::cast)
                .map(JsonMergeOption::getDistinctKeys)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(toMap(Pair::getLeft, Pair::getRight));
    }

    @Override
    T doMerge(Configuration<T> config1, Configuration<T> config2, MergeOption... mergeOptions)
    {
        Map<JsonPathExpression, List<String>> distinctKeys = parseDistinctKeys(mergeOptions);
        List<T> sortedJSONObjects = getSortedJSONObjects(config1, config2);
        JSONMerger merger = new JSONMerger(JsonPathExpression.ROOT, distinctKeys, jsonProvider);
        return (T) merger.merge(sortedJSONObjects.get(0), sortedJSONObjects.get(1));
    }

    /**
     * An internal class that merges two JSON objects with recursion support.
     */
    static class JSONMerger
    {
        private final JsonPathExpression absolutePath;
        private final Map<JsonPathExpression, List<String>> distinctKeysByJsonPath;
        private final JsonProvider jsonProvider;

        /**
         * Creates a new {@link JSONObjectMapper} for an absolute path.
         *
         * @param absolutePath           the absolute path of the current JSON object or array
         *                               inside the root JSON object}; not {@code null}
         * @param distinctKeysByJsonPath a map that associates JsonPath expressions inside the
         *                               JSON object and distinct keys for object identification
         *                               during the merge of an array; not {@code null}
         * @param jsonProvider           the {@link JsonProvider} to use; not {@code null}
         */
        JSONMerger(JsonPathExpression absolutePath,
                Map<JsonPathExpression, List<String>> distinctKeysByJsonPath, JsonProvider jsonProvider)
        {
            this.absolutePath = absolutePath;
            this.distinctKeysByJsonPath = distinctKeysByJsonPath;
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
                            distinctKeysByJsonPath, jsonProvider);
                    jsonProvider.put(result, key, merger.mergeSafely(value1, value2));
                }
                else if (jsonProvider.isJsonArray(value1))
                {
                    JSONMerger merger = new JSONMerger(absolutePath.appendChild(key),
                            distinctKeysByJsonPath, jsonProvider);
                    jsonProvider.put(result, key, merger.mergeArray(value1, value2));
                }
                else
                {
                    jsonProvider.put(result, key, value1); // Get from the highest-precedence json
                }
            }

            // Then iterate through the second json to find additional keys
            for (Entry<String, Object> entry : jsonProvider.entrySet(json2))
            {
                jsonProvider.putIfAbsent(result, entry.getKey(), entry.getValue());
            }
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

            List<String> keys = distinctKeysByJsonPath.get(absolutePath);
            if (keys != null)
            {
                // Here we add objects from the 2nd array only if they are not present in the 1st one.
                // Because the user specified a distinct key, then use it to find the "equal" objects.
                jsonProvider.forEachElementInArray(array2,
                        object -> addDistinctObject(object, keys, result));
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
         * @param keys   a list of distinctive keys inside the JSON object to be used for "equal"
         *               objects identification in the target array
         * @param array  the array to which the object will be added, provided that it contains no
         *               other object with the same value for the specified {@code key}
         */
        private void addDistinctObject(Object object, List<String> keys, Object array)
        {
            if (jsonProvider.isJsonObject(object))
            {
                Map<String, Object> values = new HashMap<>();
                for (String key : keys)
                {
                    Object value = jsonProvider.get(object, key);
                    values.put(key, value);
                }
                if (!findMatchingObjectOnArray(values, array).isPresent())
                {
                    jsonProvider.add(array, object);
                }
                // Do nothing if the key is already present.
                // It was populated from the highest-precedence json.
            }
            else
            {
                addDistinctObjectToArray(array, object);
            }
        }

        /**
         * @param distinctValues a map of distinctive key-value pairs inside the JSON object to be
         *                       used for "equal" objects identification in the given array
         * @param array          the array to be searched
         * @return the first JSON object matching the specified criteria
         */
        private Optional<Object> findMatchingObjectOnArray(Map<String, Object> distinctValues,
                Object array)
        {
            return jsonProvider.stream(array).filter(jsonProvider::isJsonObject)
                    .filter(json -> jsonObjectContains(distinctValues, json)).findFirst();
        }

        /**
         * @param distinctValues a map of expected keys and values to be found in the JSON
         * @param json           the JSON object to be evaluated
         * @return {@code true} if the all of the distinct key-value pairs specified are found in
         *         the given JSON object; otherwise, {@code false}
         * @since 2.3.0
         */
        private boolean jsonObjectContains(Map<String, Object> distinctValues, Object json)
        {
            for (Entry<String, Object> entry : distinctValues.entrySet())
            {
                String key = entry.getKey();
                Object value = entry.getValue();
                if (value == null || !value.equals(jsonProvider.get(json, key)))
                {
                    return false;
                }
            }
            return true;
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
