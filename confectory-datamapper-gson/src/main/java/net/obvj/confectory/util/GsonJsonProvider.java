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
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * A specialized {@link JsonProvider} implementation for {@code Gson}.
 *
 * @author oswaldo.bapvic.jr (Oswaldo Junior)
 * @since 2.2.0
 *
 * @see JsonObject
 * @see JsonArray
 */
public class GsonJsonProvider implements JsonProvider
{
    private final Gson gson = new Gson();

    private JsonObject toJsonObject(final Object jsonObject)
    {
        return (JsonObject) jsonObject;
    }

    private JsonArray toJsonArray(final Object jsonArray)
    {
        return (JsonArray) jsonArray;
    }

    protected JsonElement toJsonElement(final Object object)
    {
        return object instanceof JsonElement ? (JsonElement) object : gson.toJsonTree(object);
    }

    @Override
    public boolean isJsonObject(final Object object)
    {
        return object instanceof JsonObject;
    }

    @Override
    public boolean isJsonArray(final Object object)
    {
        return object instanceof JsonArray;
    }

    @Override
    public boolean isEmpty(final Object jsonObject)
    {
        return toJsonObject(jsonObject).size() == 0;
    }

    @Override
    public Object newJsonObject()
    {
        return new JsonObject();
    }

    @Override
    public Object newJsonObject(final Object sourceJsonObject)
    {
        return toJsonObject(sourceJsonObject).deepCopy();
    }

    @Override
    public Object newJsonArray()
    {
        return new JsonArray();
    }

    @Override
    public Object newJsonArray(final Object sourceJsonArray)
    {
        return toJsonArray(sourceJsonArray).deepCopy();
    }

    @Override
    public Set<Entry<String, Object>> entrySet(final Object jsonObject)
    {
        return (Set) toJsonObject(jsonObject).entrySet();
    }

    @Override
    public Object get(final Object jsonObject, final String key)
    {
        JsonElement element = toJsonObject(jsonObject).get(key);
        return element;
    }


    @Override
    public void put(final Object jsonObject, final String key, final Object value)
    {
        toJsonObject(jsonObject).add(key, toJsonElement(value));
    }

    @Override
    public void putIfAbsent(final Object jsonObject, final String key, final Object value)
    {
        JsonObject json = toJsonObject(jsonObject);
        if (json.get(key) == null)
        {
            json.add(key, toJsonElement(value));
        }
    }

    @Override
    public void add(final Object jsonArray, final Object element)
    {
        toJsonArray(jsonArray).add(toJsonElement(element));
    }

    @Override
    public void forEachElementInArray(final Object jsonArray, final Consumer<? super Object> action)
    {
        toJsonArray(jsonArray).forEach(action);
    }

    @Override
    public boolean arrayContains(final Object jsonArray, final Object element)
    {
        return toJsonArray(jsonArray).contains(toJsonElement(element));
    }

    @Override
    public Stream<Object> stream(final Object jsonArray)
    {
        Spliterator<JsonElement> spliterator = toJsonArray(jsonArray).spliterator();
        return (Stream) StreamSupport.stream(spliterator, false);
    }

}
