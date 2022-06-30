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

import java.util.Map.Entry;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Stream;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

/**
 * A concrete {@link JsonProvider} implementation for the {@code json-smart} provider.
 *
 * @author oswaldo.bapvic.jr
 * @since 2.2.0
 *
 * @see JSONObject
 * @see JSONArray
 */
public class JsonSmartJsonProvider implements JsonProvider
{

    private JSONObject toJsonObject(Object jsonObject)
    {
        return (JSONObject) jsonObject;
    }

    private JSONArray toJsonArray(Object jsonArray)
    {
        return (JSONArray) jsonArray;
    }

    @Override
    public boolean isJsonObject(Object object)
    {
        return object instanceof JSONObject;
    }

    @Override
    public boolean isJsonArray(Object object)
    {
        return object instanceof JSONArray;
    }

    @Override
    public boolean isEmpty(Object jsonObject)
    {
        return toJsonObject(jsonObject).isEmpty();
    }

    @Override
    public Object newJsonObject()
    {
        return new JSONObject();
    }

    @Override
    public Object newJsonObject(Object sourceJsonObject)
    {
        return new JSONObject((JSONObject) sourceJsonObject);
    }

    @Override
    public Object newJsonArray()
    {
        return new JSONArray();
    }

    @Override
    public Object newJsonArray(Object sourceJsonArray)
    {
        JSONArray array = new JSONArray();
        array.addAll((JSONArray) sourceJsonArray);
        return array;
    }

    @Override
    public Set<Entry<String, Object>> entrySet(Object jsonObject)
    {
        return toJsonObject(jsonObject).entrySet();
    }

    @Override
    public Object get(Object jsonObject, String key)
    {
        return toJsonObject(jsonObject).get(key);
    }

    @Override
    public Object put(Object jsonObject, String key, Object value)
    {
        return toJsonObject(jsonObject).put(key, value);
    }

    @Override
    public boolean add(Object jsonArray, Object element)
    {
        return toJsonArray(jsonArray).add(element);
    }

    @Override
    public void forEachEntryInJsonObject(Object jsonObject, BiConsumer<? super String, ? super Object> action)
    {
        toJsonObject(jsonObject).forEach(action);
    }

    @Override
    public void forEachElementInArray(Object jsonArray, Consumer<? super Object> action)
    {
        ((JSONArray) jsonArray).forEach(action);
    }

    @Override
    public Object putIfAbsent(Object jsonObject, String key, Object value)
    {
        return toJsonObject(jsonObject).putIfAbsent(key, value);
    }

    @Override
    public boolean arrayContains(Object jsonArray, Object element)
    {
        return toJsonArray(jsonArray).contains(element);
    }

    @Override
    public Stream<Object> stream(Object jsonArray)
    {
        return toJsonArray(jsonArray).stream();
    }

}
