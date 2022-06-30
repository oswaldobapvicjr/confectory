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

package net.obvj.confectory.util;

import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * An abstraction that represents a JSON provider (for example: Jackson, Gson, etc.)
 * defining common operations for all implementations.
 *
 * @author oswaldo.bapvic.jr
 * @since 2.2.0
 */
public interface JsonProvider
{

    /**
     * Checks if the specified object is a JSON object for this provider.
     *
     * @param object the object to be checked
     * @return {@code true} if the specified object is a provider-specific JSON object;
     *         otherwise, {@code false}.
     */
    boolean isJsonObject(Object object);

    /**
     * Checks if the specified object is a JSON array for this provider.
     *
     * @param object the object to be checked
     * @return {@code true} if the specified object is a provider-specific JSON array;
     *         otherwise, {@code false}.
     */
    boolean isJsonArray(Object object);

    /**
     * Checks if the specified JSON object contains no data.
     *
     * @param jsonObject the JSON object to be checked; not {@code null}
     * @return {@code true} if the specified JSON object contains no data; otherwise,
     *         {@code false}.
     *
     * @throws ClassCastException if the specified parameter is not a valid JSON object for
     *                            this provider
     */
    boolean isEmpty(Object jsonObject);

    /**
     * Creates a provider-specific JSON object.
     *
     * @return a new, empty JSON object
     */
    Object newJsonObject();

    /**
     * Creates a new provider-specific JSON object with the contents of a preset JSON object.
     *
     * @param sourceJsonObject the JSON whose contents are to be copied; not {@code null}
     * @return a new JSON object with the contents of the source JSON object
     */
    Object newJsonObject(Object sourceJsonObject);

    /**
     * Creates a provider-specific JSON array.
     *
     * @return a new, empty JSON array
     */
    Object newJsonArray();

    /**
     * Creates a new provider-specific JSON array with the elements of a preset JSON array.
     *
     * @param sourceJsonArray the JSON array whose contents are to be copied; not {@code null}
     * @return a new JSON array with the element of the source JSON array
     */
    Object newJsonArray(Object sourceJsonArray);

    /**
     * Returns a {@link Set} view of the mappings contained in the specified JSON object.
     *
     * @param jsonObject the JSON object whose entries shall be accessed; not {@code null}
     * @return the entries of the specified JSON object.
     *
     * @throws ClassCastException if the specified parameter is not a valid JSON object for
     *                            this provider
     */
    Set<Map.Entry<String, Object>> entrySet(Object jsonObject);

    /**
     * Returns the value to which the specified key is mapped in the specified JSON object; or
     * {@code null} if the JSON does not contain the specified key.
     *
     * @param jsonObject the JSON object; not {@code null}
     * @param key        the key to be searched; not {@code null}
     * @return the value associated with the specified key in the specified JSON
     *
     * @throws ClassCastException if the specified {@code jsonObject} is not a valid JSON
     *                            object for this provider
     */
    Object get(Object jsonObject, String key);

    /**
     * Associates the specified value with the specified key in the specified JSON object.
     *
     * @param jsonObject the JSON object; not {@code null}
     * @param key        the key with which the specified value is to be associated in the
     *                   JSON; not {@code null}
     * @param value      the value to be associated with the specified key in the JSON
     *
     * @throws ClassCastException if the specified {@code jsonObject} is not a valid JSON
     *                            object for this provider
     */
    void put(Object jsonObject, String key, Object value);

    /**
     * Appends the specified element to the end of the specified JSON array.
     *
     * @param jsonArray the JSON array; not {@code null}
     * @param element   the element to be added
     *
     * @throws ClassCastException if the specified {@code jsonArray} is not a valid JSON array
     *                            for this provider
     */
    void add(Object jsonArray, Object element);

    void forEachEntryInJsonObject(Object jsonObject, BiConsumer<? super String, ? super Object> action);

    void forEachElementInArray(Object jsonArray, Consumer<? super Object> action);

    Object putIfAbsent(Object jsonObject, String key, Object value);

    boolean arrayContains(Object jsonArray, Object element);

    Stream<Object> stream(Object jsonArray);
}
