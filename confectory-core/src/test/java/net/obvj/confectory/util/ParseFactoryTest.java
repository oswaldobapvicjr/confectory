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

import static net.obvj.junit.utils.matchers.AdvancedMatchers.instantiationNotAllowed;
import static net.obvj.junit.utils.matchers.AdvancedMatchers.throwsException;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.MatcherAssert.assertThat;

import java.sql.Timestamp;
import java.time.*;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import net.obvj.confectory.TestUtils;

/**
 * Unit tests for the {@link ParseFactory}.
 *
 * @author oswaldo.bapvic.jr
 * @since 1.2.0
 */
class ParseFactoryTest
{
    private static final String STR_TRUE = "true";
    private static final String STR_123 = "123";
    private static final String STR_A = "A";

    private static final String DATE_2022_12_03 = "2022-12-03";
    private static final String DATE_2022_12_03_10_15_30 = "2022-12-03 10:15:30";
    private static final String DATE_2022_12_03T10_15_30 = "2022-12-03T10:15:30";
    private static final String DATE_2022_12_03T10_15_30_MINUS_03_00 = "2022-12-03T10:15:30-03:00";
    private static final String DATE_2022_12_03T10_15_30_MINUS_03_00_AMERICA_SP = "2022-12-03T10:15:30-03:00[America/Sao_Paulo]";
    private static final String DATE_2022_12_03T13_15_30Z = "2022-12-03T13:15:30Z";

    private static final Date DATE_2023_12_03T13_15_30Z_AS_DATE = TestUtils.toDateUtc(2022, 12, 03,
            13, 15, 30, 0);
    private static final long DATE_2022_12_03T13_15_30Z_TIMESTAMP = DATE_2023_12_03T13_15_30Z_AS_DATE.getTime();

    @BeforeAll
    public static void setup()
    {
        Locale.setDefault(Locale.UK);
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }

    @Test
    void constructor_instantiationNotAllowed()
    {
        assertThat(ParseFactory.class, instantiationNotAllowed().throwing(UnsupportedOperationException.class));
    }

    @Test
    void parse_unknownType_unsupportedOperation()
    {
        assertThat(() -> ParseFactory.parse(Object.class, STR_123),
                throwsException(UnsupportedOperationException.class).withMessageContaining("Unsupported type"));
    }

    @Test
    void parse_primitiveTypes_success()
    {
        assertThat(ParseFactory.parse(boolean.class, STR_TRUE), equalTo(true));
        assertThat(ParseFactory.parse(int.class, STR_123), equalTo(123));
        assertThat(ParseFactory.parse(long.class, STR_123), equalTo(123l));
        assertThat(ParseFactory.parse(float.class, STR_123), equalTo(123f));
        assertThat(ParseFactory.parse(double.class, STR_123), equalTo(123.0));
        assertThat(ParseFactory.parse(char.class, STR_A), equalTo('A'));
    }

    @Test
    void parse_wrapperTypes_success()
    {
        assertThat(ParseFactory.parse(Boolean.class, STR_TRUE), equalTo(true));
        assertThat(ParseFactory.parse(Integer.class, STR_123), equalTo(123));
        assertThat(ParseFactory.parse(Long.class, STR_123), equalTo(123l));
        assertThat(ParseFactory.parse(Float.class, STR_123), equalTo(123f));
        assertThat(ParseFactory.parse(Double.class, STR_123), equalTo(123.0));
        assertThat(ParseFactory.parse(Character.class, STR_A), equalTo('A'));
    }

    @Test
    void parse_string_sameInstance()
    {
        assertThat(ParseFactory.parse(String.class, STR_123), sameInstance(STR_123));
    }

    @Test
    void parse_characterEmptyString_zero()
    {
        assertThat(ParseFactory.parse(Character.class, ""), equalTo('\0'));
    }

    @Test
    void parse_localDate_success()
    {
        assertThat(ParseFactory.parse(LocalDate.class, DATE_2022_12_03),
                equalTo(LocalDate.of(2022, 12, 3)));
    }

    @Test
    void parse_localDateTime_success()
    {
        assertThat(ParseFactory.parse(LocalDateTime.class, DATE_2022_12_03T10_15_30),
                equalTo(LocalDateTime.of(2022, 12, 3, 10, 15, 30, 0)));
    }

    @Test
    void parse_offsetDateTime_success()
    {
        assertThat(ParseFactory.parse(OffsetDateTime.class, DATE_2022_12_03T10_15_30_MINUS_03_00),
                equalTo(OffsetDateTime.of(2022, 12, 3, 10, 15, 30, 0, ZoneOffset.ofHours(-3))));
    }

    @Test
    void parse_zonedDateTime_success()
    {
        assertThat(ParseFactory.parse(ZonedDateTime.class, DATE_2022_12_03T10_15_30_MINUS_03_00_AMERICA_SP),
                equalTo(ZonedDateTime.of(2022, 12, 3, 10, 15, 30, 0, ZoneId.of("America/Sao_Paulo"))));
    }

    @Test
    void parse_instant_success()
    {
        assertThat(ParseFactory.parse(Instant.class, DATE_2022_12_03T13_15_30Z),
                equalTo(Instant.ofEpochMilli(DATE_2022_12_03T13_15_30Z_TIMESTAMP)));
    }

    @Test
    void parse_duration_success()
    {
        assertThat(ParseFactory.parse(Duration.class, "PT15M"), equalTo(Duration.ofMinutes(15)));
    }

    @Test
    void parse_javaUtilDateWithOffset_success()
    {
        assertThat(ParseFactory.parse(Date.class, DATE_2022_12_03T10_15_30_MINUS_03_00),
                equalTo(DATE_2023_12_03T13_15_30Z_AS_DATE));
    }

    @Test
    void parse_javaUtilDateZulu_success()
    {
        assertThat(ParseFactory.parse(Date.class, DATE_2022_12_03T13_15_30Z),
                equalTo(DATE_2023_12_03T13_15_30Z_AS_DATE));
    }

    @Test
    void parse_javaSqlDate_success()
    {
        assertThat(ParseFactory.parse(java.sql.Date.class, DATE_2022_12_03),
                equalTo(TestUtils.toDateUtc(2022, 12, 3, 0, 0, 0, 0)));
    }

    @Test
    void parse_timestamp_success()
    {
        assertThat(ParseFactory.parse(Timestamp.class, DATE_2022_12_03_10_15_30),
                equalTo(new Timestamp(TestUtils.toDateUtc(2022, 12, 3, 10, 15, 30, 0).getTime())));
    }

    @Test
    void parse_month_success()
    {
        assertThat(ParseFactory.parse(Month.class, "JULY"), equalTo(Month.JULY));
    }

    @Test
    void parse_dayOfWeek_success()
    {
        assertThat(ParseFactory.parse(DayOfWeek.class, "FRIDAY"), equalTo(DayOfWeek.FRIDAY));
    }

}
