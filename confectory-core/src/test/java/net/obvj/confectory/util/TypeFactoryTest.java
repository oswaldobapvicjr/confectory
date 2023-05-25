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

import static net.obvj.junit.utils.matchers.AdvancedMatchers.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.*;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.*;
import java.util.*;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import net.obvj.confectory.Configuration;
import net.obvj.confectory.TestUtils;

/**
 * Unit tests for the {@link TypeFactory}.
 *
 * @author oswaldo.bapvic.jr
 * @since 1.2.0
 */
class TypeFactoryTest
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

    private static final Date DATE_2023_12_03T13_15_30Z_AS_DATE = TestUtils.toDateUtc(2022, 12, 03, 13, 15, 30, 0);
    private static final long DATE_2022_12_03T13_15_30Z_TIMESTAMP = DATE_2023_12_03T13_15_30Z_AS_DATE.getTime();

    private static final String TIME_10_15 = "10:15";
    private static final String TIME_10_15_30 = "10:15:30";
    private static final long TIME_10_15_30_AS_TIMESTAMP = TestUtils.toDateUtc(1970, 01, 01, 10, 15, 30, 0).getTime();

    @BeforeAll
    public static void setup()
    {
        Locale.setDefault(Locale.UK);
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }

    @Test
    void constructor_instantiationNotAllowed()
    {
        assertThat(TypeFactory.class, instantiationNotAllowed().throwing(UnsupportedOperationException.class));
    }

    @Test
    void parse_unknownType_unsupportedOperation()
    {
        assertThat(() -> TypeFactory.parse(Object.class, STR_123),
                throwsException(UnsupportedOperationException.class).withMessageContaining("Unsupported type"));
    }

    @Test
    void parse_primitiveTypes_success()
    {
        assertThat(TypeFactory.parse(boolean.class, STR_TRUE), equalTo(true));
        assertThat(TypeFactory.parse(int.class, STR_123), equalTo(123));
        assertThat(TypeFactory.parse(long.class, STR_123), equalTo(123l));
        assertThat(TypeFactory.parse(float.class, STR_123), equalTo(123f));
        assertThat(TypeFactory.parse(double.class, STR_123), equalTo(123.0));
        assertThat(TypeFactory.parse(char.class, STR_A), equalTo('A'));
    }

    @Test
    void parse_wrapperTypes_success()
    {
        assertThat(TypeFactory.parse(Boolean.class, STR_TRUE), equalTo(true));
        assertThat(TypeFactory.parse(Integer.class, STR_123), equalTo(123));
        assertThat(TypeFactory.parse(Long.class, STR_123), equalTo(123l));
        assertThat(TypeFactory.parse(Float.class, STR_123), equalTo(123f));
        assertThat(TypeFactory.parse(Double.class, STR_123), equalTo(123.0));
        assertThat(TypeFactory.parse(Character.class, STR_A), equalTo('A'));
    }

    @Test
    void parse_string_sameInstance()
    {
        assertThat(TypeFactory.parse(String.class, STR_123), sameInstance(STR_123));
    }

    @Test
    void parse_characterEmptyString_zero()
    {
        assertThat(TypeFactory.parse(Character.class, ""), equalTo('\0'));
    }

    @Test
    void parse_localDate_success()
    {
        assertThat(TypeFactory.parse(LocalDate.class, DATE_2022_12_03),
                equalTo(LocalDate.of(2022, 12, 3)));
    }

    @Test
    void parse_localDateTime_success()
    {
        assertThat(TypeFactory.parse(LocalDateTime.class, DATE_2022_12_03T10_15_30),
                equalTo(LocalDateTime.of(2022, 12, 3, 10, 15, 30, 0)));
    }

    @Test
    void parse_offsetDateTime_success()
    {
        assertThat(TypeFactory.parse(OffsetDateTime.class, DATE_2022_12_03T10_15_30_MINUS_03_00),
                equalTo(OffsetDateTime.of(2022, 12, 3, 10, 15, 30, 0, ZoneOffset.ofHours(-3))));
    }

    @Test
    void parse_zonedDateTime_success()
    {
        assertThat(TypeFactory.parse(ZonedDateTime.class, DATE_2022_12_03T10_15_30_MINUS_03_00_AMERICA_SP),
                equalTo(ZonedDateTime.of(2022, 12, 3, 10, 15, 30, 0, ZoneId.of("America/Sao_Paulo"))));
    }

    @Test
    void parse_instant_success()
    {
        assertThat(TypeFactory.parse(Instant.class, DATE_2022_12_03T13_15_30Z),
                equalTo(Instant.ofEpochMilli(DATE_2022_12_03T13_15_30Z_TIMESTAMP)));
    }

    @Test
    void parse_duration_success()
    {
        assertThat(TypeFactory.parse(Duration.class, "PT15M"), equalTo(Duration.ofMinutes(15)));
    }

    @Test
    void parse_javaUtilDateWithOffset_success()
    {
        assertThat(TypeFactory.parse(Date.class, DATE_2022_12_03T10_15_30_MINUS_03_00),
                equalTo(DATE_2023_12_03T13_15_30Z_AS_DATE));
    }

    @Test
    void parse_javaUtilDateZulu_success()
    {
        assertThat(TypeFactory.parse(Date.class, DATE_2022_12_03T13_15_30Z),
                equalTo(DATE_2023_12_03T13_15_30Z_AS_DATE));
    }

    @Test
    void parse_javaSqlDate_success()
    {
        assertThat(TypeFactory.parse(java.sql.Date.class, DATE_2022_12_03),
                equalTo(TestUtils.toDateUtc(2022, 12, 3, 0, 0, 0, 0)));
    }

    @Test
    void parse_javaSqlTime_success()
    {
        assertThat(TypeFactory.parse(Time.class, TIME_10_15_30).getTime(), equalTo(TIME_10_15_30_AS_TIMESTAMP));
    }

    @Test
    void parse_timestamp_success()
    {
        assertThat(TypeFactory.parse(Timestamp.class, DATE_2022_12_03_10_15_30),
                equalTo(new Timestamp(TestUtils.toDateUtc(2022, 12, 3, 10, 15, 30, 0).getTime())));
    }

    @Test
    void parse_enumTypesCaseInsensitive_success()
    {
        assertThat(TypeFactory.parse(Month.class, "JULY"), equalTo(Month.JULY));
        assertThat(TypeFactory.parse(Month.class, "aUguSt"), equalTo(Month.AUGUST));
        assertThat(TypeFactory.parse(DayOfWeek.class, "FRIDAY"), equalTo(DayOfWeek.FRIDAY));
        assertThat(TypeFactory.parse(DayOfWeek.class, "saturday"), equalTo(DayOfWeek.SATURDAY));
    }

    @Test
    void parse_invalidEnumElement_null()
    {
        assertThat(TypeFactory.parse(Month.class, "unknown"), equalTo(null));
    }

    @Test
    void parse_validClass_sucess()
    {
        assertThat(TypeFactory.parse(Class.class, "net.obvj.confectory.Configuration"),
                equalTo(Configuration.class));
    }

    @Test
    void parse_invalidClass_parseException()
    {
        Throwable throwable = assertThrows(ParseException.class,
                () -> TypeFactory.parse(Class.class, "net.obvj.confectory.Invalid"));
        Throwable rootCause = ExceptionUtils.getRootCause(throwable);
        assertThat(rootCause.getClass(), equalTo(ClassNotFoundException.class));
    }

    @Test
    void parse_bigInteger_success()
    {
        assertThat(TypeFactory.parse(BigInteger.class, "987654321987654321"),
                equalTo(BigInteger.valueOf(987654321987654321L)));
    }

    @Test
    void parse_bigDecimal_success()
    {
        assertThat(TypeFactory.parse(BigDecimal.class, "987654321.987654321"),
                equalTo(BigDecimal.valueOf(987654321987654321L, 9)));
    }

    @Test
    void parse_currency()
    {
        assertThat(TypeFactory.parse(Currency.class, "BRL").getDisplayName(),
                equalTo("Brazilian Real"));

        assertThat(() -> TypeFactory.parse(Currency.class, "invalid"),
                throwsException(ParseException.class)
                        .withMessage("Unparsable java.util.Currency: \"invalid\"")
                        .withCause(IllegalArgumentException.class));
    }

    @Test
    void parse_timeZone()
    {
        int gmtMinus3Offset = TimeZone.getTimeZone("GMT-03:00").getRawOffset();
        assertThat(TypeFactory.parse(TimeZone.class, "GMT-03:00").getRawOffset(),
                equalTo(gmtMinus3Offset));

        assertThat(TypeFactory.parse(TimeZone.class, "America/Sao_Paulo").getRawOffset(),
                equalTo(gmtMinus3Offset));

        assertThat(TypeFactory.parse(TimeZone.class, "unknown").getRawOffset(),
                equalTo(0)); // Fallback to GMT
    }

    @Test
    void parse_uuid()
    {
        assertThat(TypeFactory.parse(UUID.class, "937c6fd1-cc61-43be-994a-ae67d7df3547"),
                equalTo(UUID.fromString("937c6fd1-cc61-43be-994a-ae67d7df3547")));

        assertThat(() -> TypeFactory.parse(UUID.class, "invalid"),
                throwsException(ParseException.class)
                        .withMessage("Unparsable java.util.UUID: \"invalid\"")
                        .withCause(IllegalArgumentException.class));
    }

    @Test
    void parse_inetAddress() throws UnknownHostException
    {
        assertThat(TypeFactory.parse(InetAddress.class, "10.110.12.21"),
                equalTo(Inet4Address.getByAddress(new byte[] { 10, 110, 12, 21 })));

        assertThat(TypeFactory.parse(InetAddress.class, "2001:db8:3333:4444:CCCC:DDDD:EEEE:FFFF"),
                equalTo(Inet6Address.getByName("2001:db8:3333:4444:CCCC:DDDD:EEEE:FFFF")));
    }

    @Test
    void parse_uri()
    {
        assertThat(TypeFactory.parse(URI.class, "example.com/docs"),
                equalTo(URI.create("example.com/docs")));
    }

    @Test
    void parse_url() throws MalformedURLException
    {
        assertThat(TypeFactory.parse(URL.class, "https://example.com/docs"),
                equalTo(new URL("https://example.com/docs")));

        Throwable throwable = assertThrows(ParseException.class,
                () -> TypeFactory.parse(URL.class, "invalid"));
        Throwable rootCause = ExceptionUtils.getRootCause(throwable);
        assertThat(rootCause.getClass(), equalTo(MalformedURLException.class));
        assertThat(rootCause.getMessage(), containsAny("no protocol"));
    }

}
