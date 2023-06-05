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

import java.io.*;
import java.math.*;
import java.net.*;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.time.*;
import java.util.*;
import java.util.function.Function;

import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.function.Failable;

/**
 * A class that contains built-in parsers from string into common object types, typically
 * for Reflection purposes.
 * <p>
 * It supports all of the primitive types (including their wrappers) as well as
 * enumerations and following object types:
 * <p>
 * <b><i>From the {@code java.time} package:</i></b>
 * <ul>
 * <li>{@code java.time.Duration} such as {@code "PT15M"} (15 minutes)</li>
 * <li>{@code java.time.Instant} such as {@code "2007-12-03T13:15:30Z"}</li>
 * <li>{@code java.time.LocalDate} such as {@code "2007-12-03"}</li>
 * <li>{@code java.time.LocalTime} such as {@code "10:15"} or {@code "10:15:30"}</li>
 * <li>{@code java.time.LocalDateTime} such as {@code "2007-12-03T10:15:30"}</li>
 * <li>{@code java.time.OffsetDateTime} such as {@code "2007-12-03T10:15:30-03:00"}</li>
 * <li>{@code java.time.OffsetTime} such as {@code "10:15:30+01:00"}</li>
 * <li>{@code java.time.ZonedDateTime} such as {@code "2007-12-03T10:15:30-03:00[America/Sao_Paulo]"}</li>
 * <li>{@code java.time.ZoneId} such as {@code "Europe/Paris"} or {@code "+01:00"}</li>
 * <li>{@code java.time.ZoneOffset} such as {@code "+01"}, {@code "+0100"} or {@code "+01:00"}</li>
 * </ul>
 * <p>
 * <b><i>From the {@code java.sql} package:</i></b>
 * <ul>
 * <li>{@code java.sql.Date} such as {@code "2007-12-03"}</li>
 * <li>{@code java.sql.Time} such as {@code "23:13:33"}</li>
 * <li>{@code java.sql.Timestamp} such as {@code "2007-12-03 10:15:30.998"}</li>
 * </ul>
 * <p>
 * <b><i>From the {@code java.util} package:</i></b>
 * <ul>
 * <li>{@code java.util.Currency} from ISO 4217 codes, such as: {@code "BRL"} (Brazilian Real)</li>
 * <li>{@code java.util.Date} such as {@code "2007-12-03T10:15:30+01:00"} (accepting valid date-time representations in RFC 3339 formats)</li>
 * <li>{@code java.util.TimeZone} such as: {@code "America/Sao_Paulo"} or {@code "GMT-03:00"}</li>
 * <li>{@code java.util.UUID} from universally-unique identifier strings in RFC 4122 format</li>
 * </ul>
 * <p>
 * <b><i>From the {@code java.io} package:</i></b>
 * <ul>
 * <li>{@code java.io.File}</li>
 * </ul>
 * <p>
 * <b><i>From the {@code java.nio} package:</i></b>
 * <ul>
 * <li>{@code java.nio.charset.Charset}</li>
 * <li>{@code java.nio.file.Path}</li>
 * </ul>
 * <p>
 * <b><i>From the {@code java.lang} package:</i></b>
 * <ul>
 * <li>{@code java.lang.Class}</li>
 * </ul>
 * <p>
 * <b><i>From the {@code java.math} package:</i></b>
 * <ul>
 * <li>{@code java.math.BigDecimal}</li>
 * <li>{@code java.math.BigInteger}</li>
 * </ul>
 * <p>
 * <b><i>From the {@code java.net} package:</i></b>
 * <ul>
 * <li>{@code java.net.InetAddress} from strings representing an IP address</li>
 * <li>{@code java.net.URI} such as: {@code "example.com/docs"}</li>
 * <li>{@code java.net.URL} such as: {@code "http://www.example.com/resource1.html"}</li>
 * </ul>
 * <p>
 * <b><i>Enum</i></b> elements can also be retrieved based on their constant names (and
 * performs case-insensitive matching). For example:
 * <p>
 * <blockquote>
 *
 * <pre>
 * {@code ParseFactory.parse(Month.class, "january"); // returns Month.JANUARY}
 * {@code ParseFactory.parse(DayOfWeek.class, "friday"); // returns DayOfWeek.FRIDAY}
 * </pre>
 *
 * </blockquote>
 *
 * @author oswaldo.bapvic.jr
 * @since 2.5.0
 */
public class TypeFactory
{
    private static final Map<Class<?>, Function<String, ?>> PARSERS = new HashMap<>();

    static
    {
        // java.lang
        PARSERS.put(Boolean.class, Boolean::valueOf);
        PARSERS.put(Byte.class, Byte::valueOf);
        PARSERS.put(Short.class, Short::valueOf);
        PARSERS.put(Integer.class, Integer::valueOf);
        PARSERS.put(Long.class, Long::valueOf);
        PARSERS.put(Float.class, Float::valueOf);
        PARSERS.put(Double.class, Double::valueOf);
        PARSERS.put(Character.class, string -> string.isEmpty() ? 0 : Character.valueOf(string.charAt(0)));
        PARSERS.put(String.class, Function.identity());
        PARSERS.put(Class.class, Failable.asFunction(Class::forName));

        // java.time
        // LocalDate, such as: "2007-12-03"
        PARSERS.put(LocalDate.class, LocalDate::parse);
        // LocalTime, such as: "10:15" or "10:15:30"
        PARSERS.put(LocalTime.class, LocalTime::parse);
        // LocalDateTime, such as: "2007-12-03T10:15:30"
        PARSERS.put(LocalDateTime.class, LocalDateTime::parse);
        // OffsetDateTime, such as: "2007-12-03T10:15:30+01:00"
        PARSERS.put(OffsetDateTime.class, OffsetDateTime::parse);
        // OffsetTime, such as: "10:15:30+01:00"
        PARSERS.put(OffsetTime.class, OffsetTime::parse);
        // ZonedDateTime, such as: "2007-12-03T10:15:30+01:00[Europe/Paris]"
        PARSERS.put(ZonedDateTime.class, ZonedDateTime::parse);
        // ZoneId, such as: "Europe/Paris" or "+01:00"
        PARSERS.put(ZoneId.class, ZoneId::of);
        // ZoneId, such as: "+01" or "+01:00"
        PARSERS.put(ZoneOffset.class, ZoneOffset::of);
        // Instant, such as: "2007-12-03T10:15:30.00Z"
        PARSERS.put(Instant.class, Instant::parse);
        // Duration, such as: "PT15M" (15 minutes)
        PARSERS.put(Duration.class, Duration::parse);

        // java.sql
        // java.sql.Date, such as: "2007-2-28" or "2005-12-1"
        PARSERS.put(java.sql.Date.class, java.sql.Date::valueOf);
        // java.sql.Time, such as: "23:13:33"
        PARSERS.put(Time.class, Time::valueOf);
        // java.sql.Timestamp, such as: "2007-12-03 09:15:30.99"
        PARSERS.put(Timestamp.class, Timestamp::valueOf);

        // java.math
        PARSERS.put(BigInteger.class, BigInteger::new);
        PARSERS.put(BigDecimal.class, BigDecimal::new);

        // java.util
        PARSERS.put(Currency.class, Currency::getInstance);
        // Legacy java.util.Date may accept the either of the formats
        // "2007-12-03T10:15:30+01:00", or "2007-12-03T09:15:30Z"
        PARSERS.put(java.util.Date.class, DateUtils::parseDateRfc3339);
        PARSERS.put(TimeZone.class, TimeZone::getTimeZone);
        PARSERS.put(UUID.class, UUID::fromString);

        // java.net
        PARSERS.put(InetAddress.class, Failable.asFunction(InetAddress::getByName));
        PARSERS.put(URI.class, Failable.asFunction(URI::new));
        PARSERS.put(URL.class, Failable.asFunction(URL::new));

        // java.io
        PARSERS.put(File.class, File::new);

        // java.nio
        PARSERS.put(Charset.class, Charset::forName);
        PARSERS.put(Path.class, Paths::get);
    }

    /**
     * Private constructor to hide the public, implicit one.
     */
    private TypeFactory()
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
     * @throws ParseException                if an error is encountered while parsing
     */
    @SuppressWarnings("unchecked")
    public static <T> T parse(Class<T> type, String string) throws ParseException
    {
        if (type.isEnum())
        {
            return getEnumElement(type, string);
        }
        Class<?> objectType = ClassUtils.primitiveToWrapper(type);
        Function<String, ?> parser = getParser(objectType);
        try
        {
            Object object = parser.apply(string);
            return (T) object;
        }
        catch (Exception exception)
        {
            throw new ParseException(exception, "Unparsable %s: \"%s\"", type.getCanonicalName(), string);
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private static <T> T getEnumElement(Class<T> type, String string)
    {
        return (T) EnumUtils.getEnumIgnoreCase((Class<Enum>) type, string);
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
