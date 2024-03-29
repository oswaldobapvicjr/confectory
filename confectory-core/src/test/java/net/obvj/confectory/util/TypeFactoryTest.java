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

import static net.obvj.junit.utils.matchers.AdvancedMatchers.containsAny;
import static net.obvj.junit.utils.matchers.AdvancedMatchers.instantiationNotAllowed;
import static net.obvj.junit.utils.matchers.AdvancedMatchers.throwsException;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.charset.UnsupportedCharsetException;
import java.nio.file.Path;
import java.nio.file.Paths;
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
    private static final String INVALID = "invalid";
    private static final String STR_TRUE = "true";
    private static final String STR_123 = "123";
    private static final String STR_A = "A";
    private static final String STR_ZONE_AMERICA_SP = "America/Sao_Paulo";
    private static final String FILE1_PATH = "testfiles/drive1.json";

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
    private static final String TIME_10_15_30_MINUS_03_00 = "10:15:30-03:00";

    private static final long TIME_10_15_30_AS_TIMESTAMP = TestUtils.toDateUtc(1970, 01, 01, 10, 15, 30, 0).getTime();

    private static final ZoneOffset ZONE_OFFSET_MINUS_3 = ZoneOffset.ofHours(-3);
    private static final ZoneId ZONE_ID_MINUS_3 = ZoneId.of("-3");
    private static final ZoneId ZONE_ID_AMERICA_SP = ZoneId.of(STR_ZONE_AMERICA_SP);

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
    void parse_primitiveTypes_success() throws ParseException
    {
        assertThat(TypeFactory.parse(boolean.class, STR_TRUE), equalTo(true));
        assertThat(TypeFactory.parse(int.class, STR_123), equalTo(123));
        assertThat(TypeFactory.parse(long.class, STR_123), equalTo(123l));
        assertThat(TypeFactory.parse(float.class, STR_123), equalTo(123f));
        assertThat(TypeFactory.parse(double.class, STR_123), equalTo(123.0));
        assertThat(TypeFactory.parse(char.class, STR_A), equalTo('A'));
    }

    @Test
    void parse_wrapperTypes_success() throws ParseException
    {
        assertThat(TypeFactory.parse(Boolean.class, STR_TRUE), equalTo(true));
        assertThat(TypeFactory.parse(Integer.class, STR_123), equalTo(123));
        assertThat(TypeFactory.parse(Long.class, STR_123), equalTo(123l));
        assertThat(TypeFactory.parse(Float.class, STR_123), equalTo(123f));
        assertThat(TypeFactory.parse(Double.class, STR_123), equalTo(123.0));
        assertThat(TypeFactory.parse(Character.class, STR_A), equalTo('A'));
    }

    @Test
    void parse_string_sameInstance() throws ParseException
    {
        assertThat(TypeFactory.parse(String.class, STR_123), sameInstance(STR_123));
    }

    @Test
    void parse_characterEmptyString_zero() throws ParseException
    {
        assertThat(TypeFactory.parse(Character.class, ""), equalTo('\0'));
    }

    @Test
    void parse_localDate_success() throws ParseException
    {
        assertThat(TypeFactory.parse(LocalDate.class, DATE_2022_12_03),
                equalTo(LocalDate.of(2022, 12, 3)));
    }

    @Test
    void parse_localTime_success() throws ParseException
    {
        assertThat(TypeFactory.parse(LocalTime.class, TIME_10_15),
                equalTo(LocalTime.of(10, 15, 0)));
        assertThat(TypeFactory.parse(LocalTime.class, TIME_10_15_30),
                equalTo(LocalTime.of(10, 15, 30)));
    }

    @Test
    void parse_localDateTime_success() throws ParseException
    {
        assertThat(TypeFactory.parse(LocalDateTime.class, DATE_2022_12_03T10_15_30),
                equalTo(LocalDateTime.of(2022, 12, 3, 10, 15, 30, 0)));
    }

    @Test
    void parse_offsetDateTime_success() throws ParseException
    {
        assertThat(TypeFactory.parse(OffsetDateTime.class, DATE_2022_12_03T10_15_30_MINUS_03_00),
                equalTo(OffsetDateTime.of(2022, 12, 3, 10, 15, 30, 0, ZONE_OFFSET_MINUS_3)));
    }

    @Test
    void parse_offsetTime_success() throws ParseException
    {
        assertThat(TypeFactory.parse(OffsetTime.class, TIME_10_15_30_MINUS_03_00),
                equalTo(OffsetTime.of(10, 15, 30, 0, ZONE_OFFSET_MINUS_3)));
    }

    @Test
    void parse_zoneOffset_sucess() throws ParseException
    {
        assertThat(TypeFactory.parse(ZoneOffset.class, "-3"), equalTo(ZONE_OFFSET_MINUS_3));
        assertThat(TypeFactory.parse(ZoneOffset.class, "-03"), equalTo(ZONE_OFFSET_MINUS_3));
        assertThat(TypeFactory.parse(ZoneOffset.class, "-0300"), equalTo(ZONE_OFFSET_MINUS_3));
        assertThat(TypeFactory.parse(ZoneOffset.class, "-03:00"), equalTo(ZONE_OFFSET_MINUS_3));
    }

    @Test
    void parse_zoneId_sucess() throws ParseException
    {
        assertThat(TypeFactory.parse(ZoneId.class, "-3"), equalTo(ZONE_ID_MINUS_3));
        assertThat(TypeFactory.parse(ZoneId.class, "-03"), equalTo(ZONE_ID_MINUS_3));
        assertThat(TypeFactory.parse(ZoneId.class, "-0300"), equalTo(ZONE_ID_MINUS_3));
        assertThat(TypeFactory.parse(ZoneId.class, "-03:00"), equalTo(ZONE_ID_MINUS_3));
        assertThat(TypeFactory.parse(ZoneId.class, STR_ZONE_AMERICA_SP),
                equalTo(ZONE_ID_AMERICA_SP));
    }

    @Test
    void parse_zonedDateTime_success() throws ParseException
    {
        assertThat(TypeFactory.parse(ZonedDateTime.class, DATE_2022_12_03T10_15_30_MINUS_03_00_AMERICA_SP),
                equalTo(ZonedDateTime.of(2022, 12, 3, 10, 15, 30, 0, ZoneId.of(STR_ZONE_AMERICA_SP))));
    }

    @Test
    void parse_instant_success() throws ParseException
    {
        assertThat(TypeFactory.parse(Instant.class, DATE_2022_12_03T13_15_30Z),
                equalTo(Instant.ofEpochMilli(DATE_2022_12_03T13_15_30Z_TIMESTAMP)));
    }

    @Test
    void parse_duration_success() throws ParseException
    {
        assertThat(TypeFactory.parse(Duration.class, "PT15M"), equalTo(Duration.ofMinutes(15)));
    }

    @Test
    void parse_javaUtilDateWithOffset_success() throws ParseException
    {
        assertThat(TypeFactory.parse(Date.class, DATE_2022_12_03T10_15_30_MINUS_03_00),
                equalTo(DATE_2023_12_03T13_15_30Z_AS_DATE));
    }

    @Test
    void parse_javaUtilDateZulu_success() throws ParseException
    {
        assertThat(TypeFactory.parse(Date.class, DATE_2022_12_03T13_15_30Z),
                equalTo(DATE_2023_12_03T13_15_30Z_AS_DATE));
    }

    @Test
    void parse_javaSqlDate_success() throws ParseException
    {
        assertThat(TypeFactory.parse(java.sql.Date.class, DATE_2022_12_03),
                equalTo(TestUtils.toDateUtc(2022, 12, 3, 0, 0, 0, 0)));
    }

    @Test
    void parse_javaSqlTime_success() throws ParseException
    {
        assertThat(TypeFactory.parse(Time.class, TIME_10_15_30).getTime(),
                equalTo(TIME_10_15_30_AS_TIMESTAMP));
    }

    @Test
    void parse_timestamp_success() throws ParseException
    {
        assertThat(TypeFactory.parse(Timestamp.class, DATE_2022_12_03_10_15_30),
                equalTo(new Timestamp(TestUtils.toDateUtc(2022, 12, 3, 10, 15, 30, 0).getTime())));
    }

    @Test
    void parse_enumTypesCaseInsensitive_success() throws ParseException
    {
        assertThat(TypeFactory.parse(Month.class, "JULY"), equalTo(Month.JULY));
        assertThat(TypeFactory.parse(Month.class, "aUguSt"), equalTo(Month.AUGUST));
        assertThat(TypeFactory.parse(DayOfWeek.class, "FRIDAY"), equalTo(DayOfWeek.FRIDAY));
        assertThat(TypeFactory.parse(DayOfWeek.class, "saturday"), equalTo(DayOfWeek.SATURDAY));
    }

    @Test
    void parse_invalidEnumElement_null() throws ParseException
    {
        assertThat(TypeFactory.parse(Month.class, INVALID), equalTo(null));
    }

    @Test
    void parse_validClass_sucess() throws ParseException
    {
        assertThat(TypeFactory.parse(Class.class, "net.obvj.confectory.Configuration"),
                equalTo(Configuration.class));
    }

    @Test
    void parse_invalidClass_parseException() throws ParseException
    {
        Throwable throwable = assertThrows(ParseException.class,
                () -> TypeFactory.parse(Class.class, "net.obvj.confectory.Invalid"));
        Throwable rootCause = ExceptionUtils.getRootCause(throwable);
        assertThat(rootCause.getClass(), equalTo(ClassNotFoundException.class));
    }

    @Test
    void parse_bigInteger_success() throws ParseException
    {
        assertThat(TypeFactory.parse(BigInteger.class, "987654321987654321"),
                equalTo(BigInteger.valueOf(987654321987654321L)));
    }

    @Test
    void parse_bigDecimal_success() throws ParseException
    {
        assertThat(TypeFactory.parse(BigDecimal.class, "987654321.987654321"),
                equalTo(BigDecimal.valueOf(987654321987654321L, 9)));
    }

    @Test
    void parse_currency() throws ParseException
    {
        assertThat(TypeFactory.parse(Currency.class, "BRL").getDisplayName(),
                equalTo("Brazilian Real"));

        assertThat(() -> TypeFactory.parse(Currency.class, INVALID),
                throwsException(ParseException.class)
                        .withMessage("Unparsable java.util.Currency: \"invalid\"")
                        .withCause(IllegalArgumentException.class));
    }

    @Test
    void parse_timeZone() throws ParseException
    {
        int gmtMinus3Offset = TimeZone.getTimeZone("GMT-03:00").getRawOffset();
        assertThat(TypeFactory.parse(TimeZone.class, "GMT-03:00").getRawOffset(),
                equalTo(gmtMinus3Offset));

        assertThat(TypeFactory.parse(TimeZone.class, STR_ZONE_AMERICA_SP).getRawOffset(),
                equalTo(gmtMinus3Offset));

        assertThat(TypeFactory.parse(TimeZone.class, INVALID).getRawOffset(),
                equalTo(0)); // Fallback to GMT
    }

    @Test
    void parse_uuid() throws ParseException
    {
        assertThat(TypeFactory.parse(UUID.class, "937c6fd1-cc61-43be-994a-ae67d7df3547"),
                equalTo(UUID.fromString("937c6fd1-cc61-43be-994a-ae67d7df3547")));

        assertThat(() -> TypeFactory.parse(UUID.class, INVALID),
                throwsException(ParseException.class)
                        .withMessage("Unparsable java.util.UUID: \"invalid\"")
                        .withCause(IllegalArgumentException.class));
    }

    @Test
    void parse_inetAddress() throws UnknownHostException, ParseException
    {
        assertThat(TypeFactory.parse(InetAddress.class, "10.110.12.21"),
                equalTo(Inet4Address.getByAddress(new byte[] { 10, 110, 12, 21 })));

        assertThat(TypeFactory.parse(InetAddress.class, "2001:db8:3333:4444:CCCC:DDDD:EEEE:FFFF"),
                equalTo(Inet6Address.getByName("2001:db8:3333:4444:CCCC:DDDD:EEEE:FFFF")));
    }

    @Test
    void parse_uri() throws ParseException
    {
        assertThat(TypeFactory.parse(URI.class, "example.com/docs"),
                equalTo(URI.create("example.com/docs")));
    }

    @Test
    void parse_url() throws MalformedURLException, ParseException
    {
        assertThat(TypeFactory.parse(URL.class, "https://example.com/docs"),
                equalTo(new URL("https://example.com/docs")));

        Throwable throwable = assertThrows(ParseException.class,
                () -> TypeFactory.parse(URL.class, INVALID));
        Throwable rootCause = ExceptionUtils.getRootCause(throwable);
        assertThat(rootCause.getClass(), equalTo(MalformedURLException.class));
        assertThat(rootCause.getMessage(), containsAny("no protocol"));
    }

    @Test
    void parse_charset() throws ParseException
    {
        assertThat(TypeFactory.parse(Charset.class, "UTF-8"), equalTo(StandardCharsets.UTF_8));
        assertThat(TypeFactory.parse(Charset.class, "ASCII"), equalTo(StandardCharsets.US_ASCII));

        assertThat(() -> TypeFactory.parse(Charset.class, INVALID),
                throwsException(ParseException.class).withCause(UnsupportedCharsetException.class));
    }

    @Test
    void parse_file() throws ParseException
    {
        assertThat(TypeFactory.parse(File.class, FILE1_PATH), equalTo(new File(FILE1_PATH)));
    }

    @Test
    void parse_path() throws ParseException
    {
        assertThat(TypeFactory.parse(Path.class, FILE1_PATH), equalTo(Paths.get(FILE1_PATH)));
    }

}
