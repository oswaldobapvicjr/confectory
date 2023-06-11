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

package net.obvj.confectory.mapper;

import static java.util.Arrays.asList;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;
import static net.obvj.junit.utils.matchers.AdvancedMatchers.throwsException;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import net.obvj.confectory.ConfigurationException;
import net.obvj.confectory.TestUtils;
import net.obvj.confectory.internal.helper.BeanConfigurationHelper;
import net.obvj.confectory.util.ObjectFactory;
import net.obvj.confectory.util.ParseException;
import net.obvj.confectory.util.Property;
import net.obvj.confectory.util.TypeConverter;

/**
 * Unit tests for the {@link PropertiesToObjectMapper} class.
 *
 * @author oswaldo.bapvic.jr (Oswaldo Junior)
 * @since 1.2.0
 */
class PropertiesToObjectMapperTest
{
    private static final String STRING1 = "string1";
    private static final int INT1 = 1910;

    private static final String TEST_PROPERTIES_CONTENT
                    = "booleanValue=true\n"
                    + "stringValue=string1\n"
                    + "intValue=1910\n";

    private static final Date DATE_UTC = TestUtils.toDateUtc(2023, 05, 12, 18, 50, 59, 0);
    private static final String TEST_PROPERTIES_DATE = "myDate=2023-05-12T15:50:59-03:00\n"
                                                     + "dayOfWeek=friday\n";

    private static final String TEST_PROPERTIES_INVALID_DATE = "myDate=2023a05-12T15:50:59-03:00\n";

    private static final String TEST_PROPERTIES_CUSTOM = "myInts=0,1,2,3,4\n"
                                                       + "myPair=agent1->0 0 * * SUN\n";

    static class MyBeanNoExplicitMapping
    {
        Boolean booleanValue; // implicit mapping
        String stringValue;   // implicit mapping
        Integer intValue;     // implicit mapping
    }

    static class MyBeanExplicitMapping
    {
        @Property("booleanValue") boolean b; // explicit mapping
        @Property("stringValue") String s;   // explicit mapping
        @Property("intValue") int i;         // explicit mapping
    }

    static class MyBeanHybrid
    {
        @Property("booleanValue") boolean b; // explicit mapping
        @Property String stringValue;        // implicit mapping
        int intValue;                        // implicit mapping
        double unknownDouble;                // invalid property
    }

    static class MyBeanAllFieldsTransient
    {
        transient boolean booleanValue; // implicit, but transient
        transient String stringValue;   // implicit, but transient
        transient int intValue;         // implicit, but transient
    }

    static class MyBeanPrivateConstructor
    {
        boolean booleanValue;
        String stringValue;
        int intValue;

        // since this value is assigned during constructor,
        // it will not happen when using the ObjectFactor.UNSAFE
        double undefined = -1.0;

        private MyBeanPrivateConstructor() {}
    }

    private static ByteArrayInputStream newInputStream()
    {
        return newInputStream(TEST_PROPERTIES_CONTENT);
    }

    private static ByteArrayInputStream newInputStream(String string)
    {
        return new ByteArrayInputStream(string.getBytes());
    }

    static class MyBeanDate
    {
        Date myDate;
        DayOfWeek dayOfWeek;

        public MyBeanDate() {}
    }

    static class MyBeanCustomConverters
    {
        @Property(converter = MyIntsConverter.class)
        List<Integer> myInts;
        @Property(key = "myPair", converter = MyPairConverter.class)
        Pair<String, String> thePair;

        public MyBeanCustomConverters() {}
    }

    static class MyIntsConverter implements TypeConverter<List<Integer>>
    {
        public MyIntsConverter() {}

        @Override
        public List<Integer> convert(String value) throws ParseException
        {
            return stream(value.split(",")).map(Integer::parseInt).collect(toList());
        }
    }

    static class MyPairConverter implements TypeConverter<Pair<String, String>>
    {
        public MyPairConverter() {}

        @Override
        public Pair<String, String> convert(String value) throws ParseException
        {
            String[] values = value.split("->");
            return Pair.of(values[0], values[1]);
        }
    }

    private static final String STR_UTC = "UTC";

    @BeforeAll
    public static void setup()
    {
        Locale.setDefault(Locale.UK);
        TimeZone.setDefault(TimeZone.getTimeZone(STR_UTC));
    }

    @Test
    void apply_beanWithNoExplicitMapping_success() throws IOException
    {
        MyBeanNoExplicitMapping bean = new PropertiesToObjectMapper<>(MyBeanNoExplicitMapping.class)
                .apply(newInputStream());

        assertThat(bean.booleanValue, equalTo(true));
        assertThat(bean.stringValue, equalTo(STRING1));
        assertThat(bean.intValue, equalTo(INT1));
    }

    @Test
    void apply_beanWithExplicitMapping_success() throws IOException
    {
        MyBeanExplicitMapping bean = new PropertiesToObjectMapper<>(MyBeanExplicitMapping.class)
                .apply(newInputStream());

        assertThat(bean.b, equalTo(true));
        assertThat(bean.s, equalTo(STRING1));
        assertThat(bean.i, equalTo(INT1));
    }

    @Test
    void apply_hybridBean_success() throws IOException
    {
        MyBeanHybrid bean = new PropertiesToObjectMapper<>(MyBeanHybrid.class)
                .apply(newInputStream());

        assertThat(bean.b, equalTo(true));
        assertThat(bean.stringValue, equalTo(STRING1));
        assertThat(bean.intValue, equalTo(INT1));
        assertThat(bean.unknownDouble, equalTo(0.0));
    }

    @Test
    void apply_beanWithAllFieldsTransient_noDataCopied() throws IOException
    {
        MyBeanAllFieldsTransient bean = new PropertiesToObjectMapper<>(MyBeanAllFieldsTransient.class)
                .apply(newInputStream());

        assertThat(bean.booleanValue, equalTo(false));
        assertThat(bean.stringValue, equalTo(null));
        assertThat(bean.intValue, equalTo(0));
    }

    @Test
    void apply_beanWithPrivateConstructorAndObjectFactoryClassic_configurationException()
    {
        assertThat(
                () -> new PropertiesToObjectMapper<>(MyBeanPrivateConstructor.class, ObjectFactory.CONSTRUCTOR_BASED)
                        .apply(newInputStream()),
                throwsException(ConfigurationException.class)
                        .withCause(ReflectiveOperationException.class));
    }

    @Test
    void apply_beanWithPrivateConstructorAndObjectFactoryFast_success() throws IOException
    {
        MyBeanPrivateConstructor bean = new PropertiesToObjectMapper<>(
                MyBeanPrivateConstructor.class, ObjectFactory.FAST).apply(newInputStream());
        assertThat(bean.booleanValue, equalTo(true));
        assertThat(bean.stringValue, equalTo("string1"));
        assertThat(bean.intValue, equalTo(1910));
        assertThat(bean.undefined, equalTo(0.0)); // Default value assigned
    }

    @Test
    void apply_beanWithPrivateConstructorAndObjectFactoryObjenesis_success() throws IOException
    {
        MyBeanPrivateConstructor bean = new PropertiesToObjectMapper<>(
                MyBeanPrivateConstructor.class, ObjectFactory.OBJENESIS).apply(newInputStream());
        assertThat(bean.booleanValue, equalTo(true));
        assertThat(bean.stringValue, equalTo("string1"));
        assertThat(bean.intValue, equalTo(1910));
        assertThat(bean.undefined, equalTo(0.0)); // Default value assigned
    }

    @Test
    void apply_propertiesWithDate_success() throws IOException
    {
        MyBeanDate bean = new PropertiesToObjectMapper<>(MyBeanDate.class)
                .apply(newInputStream(TEST_PROPERTIES_DATE));

        assertThat(bean.myDate, equalTo(DATE_UTC));
        assertThat(bean.dayOfWeek, equalTo(DayOfWeek.FRIDAY));
    }

    @Test
    void apply_propertiesWithInvalidDate_exception() throws IOException
    {
        Mapper<MyBeanDate> mapper = new PropertiesToObjectMapper<>(MyBeanDate.class);
        ByteArrayInputStream input = newInputStream(TEST_PROPERTIES_INVALID_DATE);

        ConfigurationException exception = assertThrows(ConfigurationException.class,
                () -> mapper.apply(input));

        assertThat(exception.getMessage(), equalTo(
                "Unable to parse the value of the property 'myDate' into a field of type 'java.util.Date'"));

        Throwable cause = exception.getCause();
        assertThat(cause.getClass(), equalTo(ParseException.class));
        assertThat(cause.getMessage(),
                equalTo("Unparsable java.util.Date: \"2023a05-12T15:50:59-03:00\""));

        Throwable rootCause = cause.getCause();
        assertThat(rootCause.getClass(), equalTo(DateTimeParseException.class));
        assertThat(rootCause.getMessage(),
                equalTo("Text '2023a05-12T15:50:59-03:00' could not be parsed at index 4"));
    }

    @Test
    void apply_propertiesWithCustomConverters_success() throws IOException
    {
        MyBeanCustomConverters bean = new PropertiesToObjectMapper<>(MyBeanCustomConverters.class)
                .apply(newInputStream(TEST_PROPERTIES_CUSTOM));

        assertThat(bean.myInts, equalTo(asList(0, 1, 2, 3, 4)));
        assertThat(bean.thePair.getLeft(), equalTo("agent1"));
        assertThat(bean.thePair.getRight(), equalTo("0 0 * * SUN"));
    }

    @Test
    void configurationHelper_propertiesConfigurationHelper()
    {
        assertThat(new PropertiesToObjectMapper<>(Object.class).configurationHelper(new Object()).getClass(),
                equalTo(BeanConfigurationHelper.class));
    }
}
