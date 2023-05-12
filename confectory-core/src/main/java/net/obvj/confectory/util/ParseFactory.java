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

import java.sql.Timestamp;
import java.time.*;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.apache.commons.lang3.ClassUtils;

/**
 * A class that contains built-in parsers from string into common object types, typically
 * for Reflection purposes.
 * <p>
 * It supports all of the primitive types (including their wrappers) as well as the
 * following object types:
 * <p>
 * <b><i>Since 2.5.0:</i></b>
 * <ul>
 * <li>{@code java.sql.Date} such as {@code "2007-12-03"}</li>
 * <li>{@code java.sql.Timestamp} such as {@code "2007-12-03 10:15:30.998"}</li>
 * <li>{@code java.time.DayOfWeek} such as {@code "FRIDAY"}</li>
 * <li>{@code java.time.Duration} such as {@code "PT15M"} (15 minutes)</li>
 * <li>{@code java.time.Instant} such as {@code "2007-12-03T13:15:30Z"}</li>
 * <li>{@code java.time.LocalDate} such as {@code "2007-12-03"}</li>
 * <li>{@code java.time.LocalDateTime} such as {@code "2007-12-03T10:15:30"}</li>
 * <li>{@code java.time.Month} such as {@code "DECEMBER"}</li>
 * <li>{@code java.time.OffsetDateTime} such as {@code "2007-12-03T10:15:30-03:00"}</li>
 * <li>{@code java.time.ZonedDateTime} such as {@code "2007-12-03T10:15:30-03:00[America/Sao_Paulo]"}</li>
 * <li>{@code java.util.Date} such as {@code "2007-12-03T10:15:30+01:00"} (accepting valid
 * date-time representations in RFC 3339 formats)</li>
 * </ul>
 *
 * @author oswaldo.bapvic.jr
 * @since 1.2.0
 */
public class ParseFactory
{
    private static final Map<Class<?>, Function<String, ?>> PARSERS = new HashMap<>();

    static
    {
        PARSERS.put(Boolean.class, Boolean::valueOf);
        PARSERS.put(Byte.class, Byte::valueOf);
        PARSERS.put(Short.class, Short::valueOf);
        PARSERS.put(Integer.class, Integer::valueOf);
        PARSERS.put(Long.class, Long::valueOf);
        PARSERS.put(Float.class, Float::valueOf);
        PARSERS.put(Double.class, Double::valueOf);
        PARSERS.put(Character.class, string -> string.isEmpty() ? 0 : Character.valueOf(string.charAt(0)));
        PARSERS.put(String.class, Function.identity());

        // Legacy java.util.Date may accept the either of the formats
        // "2007-12-03T10:15:30+01:00", or "2007-12-03T09:15:30Z"
        PARSERS.put(java.util.Date.class, DateUtils::parseDateRfc3339);

        // java.sql.Date, such as: "2007-2-28" or "2005-12-1"
        PARSERS.put(java.sql.Date.class, java.sql.Date::valueOf);
        // java.sql.Timestamp, such as: "2007-12-03 09:15:30.99"
        PARSERS.put(Timestamp.class, Timestamp::valueOf);

        // LocalDate, such as: "2007-12-03"
        PARSERS.put(LocalDate.class, LocalDate::parse);
        // LocalDateTime, such as: "2007-12-03T10:15:30"
        PARSERS.put(LocalDateTime.class, LocalDateTime::parse);
        // OffsetDateTime, such as: "2007-12-03T10:15:30+01:00"
        PARSERS.put(OffsetDateTime.class, OffsetDateTime::parse);
        // ZonedDateTime, such as: "2007-12-03T10:15:30+01:00[Europe/Paris]"
        PARSERS.put(ZonedDateTime.class, ZonedDateTime::parse);
        // Instant, such as: "2007-12-03T10:15:30.00Z"
        PARSERS.put(Instant.class, Instant::parse);
        // Duration, such as: "PT15M" (15 minutes)
        PARSERS.put(Duration.class, Duration::parse);

        PARSERS.put(Month.class, Month::valueOf);
        PARSERS.put(DayOfWeek.class, DayOfWeek::valueOf);
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
     * @throws UnsupportedOperationException if the specified type is not supported
     */
    @SuppressWarnings("unchecked")
    public static <T> T parse(Class<T> type, String string)
    {
        Class<?> objectType = ClassUtils.primitiveToWrapper(type);
        Function<String, ?> parser = getParser(objectType);
        Object object = parser.apply(string);
        return (T) object;
    }

    /**
     * Returns a {@link Function} to parse the specified type.
     *
     * @param type the target type
     * @return the {@link Function} to be applied for the specified type; not null
     * @throws UnsupportedOperationException if the specified type is not supported
     */
    private static Function<String, ?> getParser(Class<?> type)
    {
        Function<String, ?> parser = PARSERS.get(type);
        if (parser == null)
        {
            throw new UnsupportedOperationException("Unsupported type: " + type);
        }
        return parser;
    }

}
