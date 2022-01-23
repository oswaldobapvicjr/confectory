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

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.apache.commons.lang3.ClassUtils;

public class ParseFactory
{
    private static final Map<Class<?>, Function<String, ?>> factory = new HashMap<>();

    static
    {
        factory.put(Boolean.class, Boolean::valueOf);
        factory.put(Byte.class, Byte::valueOf);
        factory.put(Short.class, Short::valueOf);
        factory.put(Integer.class, Integer::valueOf);
        factory.put(Long.class, Long::valueOf);
        factory.put(Float.class, Float::valueOf);
        factory.put(Double.class, Double::valueOf);
        factory.put(Character.class, string -> Character.valueOf(string.charAt(0)));
        factory.put(String.class, Function.identity());
    }

    private ParseFactory()
    {
        throw new UnsupportedOperationException("Instantiation not allowed");
    }

    @SuppressWarnings("unchecked")
    public static <T> T parse(Class<T> type, String string)
    {
        Class<?> objectType = ClassUtils.primitiveToWrapper(type);
        Object object = getFunction(objectType).apply(string);
        return (T) object;
    }

    private static Function<String, ?> getFunction(Class<?> type)
    {
        Function<String, ?> function = factory.get(type);
        if (function == null)
        {
            throw new UnsupportedOperationException("Unsupported type: " + type);
        }
        return function;
    }
}
