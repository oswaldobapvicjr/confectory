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

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jayway.jsonpath.InvalidPathException;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.obvj.confectory.Configuration;
import net.obvj.confectory.mapper.JSONObjectMapper;
import net.obvj.confectory.util.ConfigurationComparator;
import net.obvj.confectory.util.JsonPathExpression;

/**
 * A specialized {@code ConfigurationMerger} that combines two {@link Configuration}
 * objects of type {@link JSONObject} (json-smart implementation) into a single one.
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
 * created using the optional constructor {@link #JSONObjectConfigurationMerger(Map)}.
 *
 * @see ConfigurationMerger
 * @author oswaldo.bapvic.jr (Oswaldo Junior)
 * @since 2.1.0
 */
public class JSONObjectConfigurationMerger extends AbstractConfigurationMerger<JSONObject>
{
    private static final Logger LOGGER = LoggerFactory.getLogger(JSONObjectConfigurationMerger.class);

    private final Map<JsonPathExpression, String> distinctObjectKeysInsideArrays;

    /**
     * Creates a standard {@link JSONObjectConfigurationMerger}.
     */
    public JSONObjectConfigurationMerger()
    {
        this(Collections.emptyMap());
    }

    /**
     * Creates a new {@link JSONObjectConfigurationMerger} with a map of distinct keys
     * associated with specific JsonPath expressions.
     * <p>
     * Use the {@code distinctObjectKeysInsideArrays} map to specify how to combine arrays of
     * objects inside the JSON. For example, considering the following document:
     * <p>
     * <blockquote>
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
     * A map containing an entry with the key {@code "$.params"} associated with the
     * {@code "param"} value tells the algorithm to check for distinct objects identified by
     * the value inside the {@code "param"} field during the merge of the {@code "$.params"}
     * array.
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
     * @param distinctObjectKeysInsideArrays a map that associates JsonPath expressions inside
     *                                       the {@code JSONObject} and object keys for
     *                                       distinction during the merge of an array;
     *                                       {@code null} is allowed
     *
     * @throws IllegalArgumentException if the specified expression is null or empty
     * @throws InvalidPathException     if the specified JsonPath expression is invalid
     */
    public JSONObjectConfigurationMerger(Map<String, String> distinctObjectKeysInsideArrays)
    {
        this.distinctObjectKeysInsideArrays = defaultIfNull(distinctObjectKeysInsideArrays,
                Collections.<String, String>emptyMap()).entrySet().stream()
                        .collect(toMap(entry -> new JsonPathExpression(entry.getKey()), Map.Entry::getValue));
    }

    @Override
    JSONObject doMerge(Configuration<JSONObject> config1, Configuration<JSONObject> config2)
    {
        JSONObject[] sortedJSONObjects = getSortedJSONObjects(config1, config2);
        JSONObjectMerger merger = new JSONObjectMerger(JsonPathExpression.ROOT,
                distinctObjectKeysInsideArrays);
        return merger.merge(sortedJSONObjects[0], sortedJSONObjects[1]);
    }

    /**
     * An internal class that merges two {@link JSONObject} instances with recursion support.
     */
    static class JSONObjectMerger
    {
        private final JsonPathExpression absolutePath;
        private final Map<JsonPathExpression, String> distinctObjectKeysByArrays;

        /**
         * Creates a new {@link JSONObjectMapper} for an absolute path.
         *
         * @param absolutePath                   the absolute path of the current
         *                                       {@link JSONObject} or {@link JSONArray} inside
         *                                       the root {@link JSONObject}
         * @param distinctObjectKeysInsideArrays a map that associates JsonPath expressions inside
         *                                       the {@code JSONObject} and distinct keys for
         *                                       object identification during the merge of an
         *                                       array
         */
        public JSONObjectMerger(JsonPathExpression absolutePath,
                Map<JsonPathExpression, String> distinctObjectKeysInsideArrays)
        {
            this.absolutePath = absolutePath;
            this.distinctObjectKeysByArrays = distinctObjectKeysInsideArrays;
        }

        /**
         * Merges two {@link JSONObject} instances.
         *
         * @param json1 the highest-precedence JSON object
         * @param json2 the lowest-precedence JSON object
         * @return a combination of the two JSON objects
         */
        private JSONObject merge(JSONObject json1, JSONObject json2)
        {
            LOGGER.debug("Merging object on path: {}", absolutePath);

            if (json2.isEmpty())
            {
                return new JSONObject(json1);
            }

            JSONObject result = new JSONObject();

            // First iterate through the first json
            for (Entry<String, Object> entry : json1.entrySet())
            {
                String key = entry.getKey();
                Object value1 = entry.getValue();
                Object value2 = json2.get(key);

                if (value1 instanceof JSONObject)
                {
                    JSONObjectMerger merger = new JSONObjectMerger(absolutePath.appendChild(key),
                            distinctObjectKeysByArrays);
                    result.put(key, merger.merge((JSONObject) value1, value2));
                }
                else if (value1 instanceof JSONArray)
                {
                    JSONObjectMerger merger = new JSONObjectMerger(absolutePath.appendChild(key),
                            distinctObjectKeysByArrays);
                    result.put(key, merger.merge((JSONArray) value1, value2));
                }
                else
                {
                    result.put(key, value1); // Get from the highest-precedence json
                }
            }

            // Then iterate through the second json to find additional keys
            json2.forEach(result::putIfAbsent);
            return result;
        }

        /**
         * Support method for type-safety during the merge a {@link JSONObject}.
         *
         * @param json   the highest-precedence {@link JSONObject} to be merged
         * @param object the lowest-precedence object to be merged
         * @return a combination of the two objects, provided that the second {@code object} is a
         *         {@link JSONObject} too
         */
        private JSONObject merge(JSONObject json, Object object)
        {
            if (object instanceof JSONObject)
            {
                return merge(json, (JSONObject) object);
            }
            // The second object is either null or has an incompatible type,
            // so simply assume object with the highest precedence.
            // Create a copy of it for safety.
            return new JSONObject(json);
        }

        /**
         * Support method for type-safety during the merge a {@link JSONArray}.
         *
         * @param array  the highest-precedence {@link JSONArray} to be merged
         * @param object the lowest-precedence object to be merged
         * @return a combination of the two objects, provided that the second {@code object} is a
         *         {@link JSONArray} too
         */
        private JSONArray merge(JSONArray array, Object object)
        {
            LOGGER.debug("Merging array on path: {}", absolutePath);
            if (object instanceof JSONArray)
            {
                return merge(array, (JSONArray) object);
            }
            // The second object is either null or has an incompatible type,
            // so simply assume the array from the highest-precedence object.
            // Create a copy of it for safety.
            return newJsonArray(array);
        }

        /**
         * Merges two {@link JSONArray} instances.
         *
         * @param array1 the highest-precedence JSON array
         * @param array2 the lowest-precedence JSON array
         * @return a combination of the two JSON arrays
         */
        private JSONArray merge(JSONArray array1, JSONArray array2)
        {
            // The 1st array is always the highest-precedence one
            JSONArray result = newJsonArray(array1);

            String distinctKey = distinctObjectKeysByArrays.get(absolutePath);
            if (distinctKey != null)
            {
                // Here we add objects from the 2nd array only if they are not present in the 1st one
                // Because the user specified a distinct key, then use it to find the "equal" objects.
                array2.forEach(object -> addDistinctObject(object, distinctKey, result));
            }
            else
            {
                array2.forEach(object ->
                {
                    addDistinctObject(result, object);
                });
            }
            return result;
        }

        private void addDistinctObject(JSONArray result, Object object)
        {
            if (!result.contains(object))
            {
                result.add(object);
            }
        }

        /**
         * Adds a distinct object, identified by a specific key, to the target array.
         *
         * @param object the object to be added, given it is not already is the {@code array}
         * @param key    a distinctive key inside the {@code JSONObject} to be used for "equal"
         *               objects identification in the target array
         * @param array  the array to which the object will be added, provided that it contains no
         *               other object with the same value for the specified {@code key}
         */
        private void addDistinctObject(Object object, String key, JSONArray array)
        {
            if (object instanceof JSONObject)
            {
                Object value = ((JSONObject) object).get(key);
                if (value != null && !findMatchingObject(value, key, array).isPresent())
                {
                    array.add(object);
                }
            }
            else
            {
                addDistinctObject(array, object);
            }
        }

        /**
         * @param value the value to search
         * @param key   a distinctive key inside the {@code JSONObject} to be used for "equal"
         *              objects identification in the given array
         * @param array the array to be searched
         * @return the first {@link JSONObject} matching the specified criteria
         */
        private static Optional<JSONObject> findMatchingObject(Object value, String key, JSONArray array)
        {
            return array.stream().filter(JSONObject.class::isInstance).map(JSONObject.class::cast)
                    .filter(json -> value.equals(json.get(key))).findFirst();
        }

        /**
         * @param elements the list of elements to compose the new {@link JSONArray}
         * @return a new {@link JSONArray} containing all elements of the specified list.
         */
        private static JSONArray newJsonArray(List<Object> elements)
        {
            JSONArray array = new JSONArray();
            array.addAll(elements);
            return array;
        }

    }

    private JSONObject[] getSortedJSONObjects(Configuration<JSONObject> config1,
            Configuration<JSONObject> config2)
    {
        return asList(config1, config2).stream().sorted(new ConfigurationComparator())
                .map(this::getJSONObjectSafely).toArray(JSONObject[]::new);
    }

    private JSONObject getJSONObjectSafely(Configuration<JSONObject> config)
    {
        return getBeanSafely(config, JSONObject::new);
    }

}
