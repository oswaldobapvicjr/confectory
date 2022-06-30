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

    boolean isJsonObject(Object object);

    boolean isJsonArray(Object object);

    boolean isEmpty(Object jsonObject);

    Object newJsonObject();

    Object newJsonObject(Object sourceJsonObject);

    Object newJsonArray();

    Object newJsonArray(Object sourceJsonArray);

    Set<Map.Entry<String, Object>> entrySet(Object jsonObject);

    Object get(Object jsonObject, String key);

    Object put(Object jsonObject, String key, Object value);

    boolean add(Object jsonArray, Object element);

    void forEachEntryInJsonObject(Object jsonObject, BiConsumer<? super String, ? super Object> action);

    void forEachElementInArray(Object jsonArray, Consumer<? super Object> action);

    Object putIfAbsent(Object jsonObject, String key, Object value);

    boolean arrayContains(Object jsonArray, Object element);

    Stream<Object> stream(Object jsonArray);
}
