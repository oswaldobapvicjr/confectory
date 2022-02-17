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
import java.util.Optional;
import java.util.function.Function;

import org.apache.commons.lang3.ClassUtils;

/**
 * A class that contains built-in parsers from string into common object types, typically
 * for Reflection purposes.
 *
 * @author oswaldo.bapvic.jr
 * @since 1.2.0
 */
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
        factory.put(Character.class, string -> string.isEmpty() ? 0 : Character.valueOf(string.charAt(0)));
        factory.put(String.class, Function.identity());
    }

    /**
     * Private constructor to hide the public, implicit one.
     */
    private ParseFactory()
    {
        throw new UnsupportedOperationException("Instantiation not allowed");
    }

    /**
     * Parses the contents of a string into the specified type.
     *
     * @param <T>    the target type
     * @param type   the target type
     * @param string the string to be parsed
     * @return an object containing the result of the parsing of the specified string into the
     *         specified type
     * @throws UnsupportedOperationException if the requested target type is not supported
     */
    @SuppressWarnings("unchecked")
    public static <T> T parse(Class<T> type, String string)
    {
        Class<?> objectType = ClassUtils.primitiveToWrapper(type);
        Function<String, ?> function = getFunction(objectType)
                .orElseThrow(() -> new UnsupportedOperationException("Unsupported type: " + type));
        Object object = function.apply(string);
        return (T) object;
    }

    /**
     * Returns an {@link Optional} possibly containing a {@link Function} to parse the
     * specified target type.
     *
     * @param type the target type
     * @return the {@link Function} to be applied for the specified type, or
     *         {@link Optional#empty()} if the specified type is not supported
     */
    private static Optional<Function<String, ?>> getFunction(Class<?> type)
    {
        return Optional.ofNullable(factory.get(type));
    }
}
