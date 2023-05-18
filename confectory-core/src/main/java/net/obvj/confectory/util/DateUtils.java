/*
 * Copyright 2023 obvj.net
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

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.TemporalAccessor;
import java.util.Date;

/**
 * Common methods for working with dates.
 *
 * @author oswaldo.bapvic.jr
 * @since 2.5.0
 */
public class DateUtils
{

    private static final DateTimeFormatter RFC_3339_DATETIME_FORMATTER = new DateTimeFormatterBuilder()
            .append(DateTimeFormatter.ISO_LOCAL_DATE)
            .optionalStart().appendLiteral('T').optionalEnd()
            .optionalStart().appendLiteral(' ').optionalEnd()
            .append(DateTimeFormatter.ISO_LOCAL_TIME)
            .optionalStart().appendOffset("+HH:MM", "Z").optionalEnd()
            .toFormatter();

    /**
     * Private constructor to hide the public, implicit one.
     */
    private DateUtils()
    {
        throw new UnsupportedOperationException("Instantiation not allowed");
    }

    /**
     * Parses the given date-time representation string based in RFC-3339 format, producing a
     * {@link Date}.
     * <p>
     * Either of the following examples are acceptable:
     * <ul>
     * <li>{@code "2019-09-07T13:21:59Z"}</li>
     * <li>{@code "2020-10-08 13:22:58.654Z"}</li>
     * <li>{@code "2021-11-09T10:23:57.321+00:00"}</li>
     * <li>{@code "2022-12-10 10:24:56.987654321-03:00"}</li>
     * </ul>
     *
     * @param string the string to parse; not null
     * @return the parsed {@link Date}; not null
     */
    public static Date parseDateRfc3339(String string)
    {
        Instant instant = parseInstantRfc3339(string);
        return Date.from(instant);
    }

    /**
     * Parses the given date-time representation string based in RFC-3339 format, producing a
     * {@link Date}.
     * <p>
     * Either of the following examples are acceptable:
     * <ul>
     * <li>{@code "2019-09-07T13:21:59.987Z"}</li>
     * <li>{@code "2020-10-08 13:22:58.654Z"}</li>
     * <li>{@code "2021-11-09T10:23:57.321+00:00"}</li>
     * <li>{@code "2022-12-10 10:24:56.987654321-03:00"}</li>
     * </ul>
     *
     * @param string the string to parse; not null
     * @return the parsed {@link Instant}; not null
     */
    public static Instant parseInstantRfc3339(String string)
    {
        TemporalAccessor temporalAccessor = RFC_3339_DATETIME_FORMATTER.parse(string);
        return Instant.from(temporalAccessor);
    }

}
