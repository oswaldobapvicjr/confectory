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
import static net.obvj.junit.utils.matchers.AdvancedMatchers.throwsException;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;

import net.obvj.confectory.ConfigurationException;
import net.obvj.confectory.ConfigurationSourceException;
import net.obvj.confectory.internal.helper.BeanConfigurationHelper;
import net.obvj.confectory.mapper.PropertiesToObjectMapperTest.MyIntsConverter;
import net.obvj.confectory.mapper.PropertiesToObjectMapperTest.MyPairConverter;
import net.obvj.confectory.mapper.model.MyIni;
import net.obvj.confectory.util.ParseException;
import net.obvj.confectory.util.Property;

/**
 * Unit tests for the {@link INIToObjectMapper} class.
 *
 * @author oswaldo.bapvic.jr
 * @since 2.0.0
 */
class INIToObjectMapperTest
{
    private static final String VALID_INI_1    = ";Comment\n"
                                               + "rootProperty = myRootValue\n"
                                               + "unMappedProperty = no action\n"
                                               + "\n"
                                               + "[section1]\n"
                                               + "# Alternative comment\n"
                                               + "section_string = mySection1Value\n"
                                               + "section_number = 1\n"
                                               + "section_bool = false\n"
                                               + "transientField = should be null\n"
                                               + "\n"
                                               + "[section2]\n"
                                               + "section_string = mySection2Value\n"
                                               + "section_number = 2\n"
                                               + "section_bool = true\n";

    private static final String INVALID_INI_1  = ";Test with invalid section declaration\n"
                                               + "[section1\n" // Missing token ']'
                                               + "section_string = mySection1Value\n";

    private static final String INVALID_INI_2  = ";Test with a missing section name\n"
                                               + "[]\n"
                                               + "section_string = mySection1Value\n";

    private static final String INVALID_INI_3 = "=value\n";

    private static final String INVALID_INI_4 = "invalid line\n";

    private static final String INVALID_INI_5  = ";Test with invalid type\n"
                                               + "number = abc\n"; // exception

    private static final String INVALID_INI_6  = ";Test with invalid type inside section\n"
                                               + "[section1]\n"
                                               + "section_number = abc\n"; // exception

    private static final String VALID_INI_2   = ";Comment\n"
                                              + "rootProperty = myRootValue\n"
                                              + "\n"
                                              + "[section3]\n" // Unmapped, the children properties shall be skipped
                                              + "# Alternative comment\n"
                                              + "section_string = mySection1Value\n"
                                              + "section_number = 1\n"
                                              + "section_bool = false\n"
                                              + "\n"
                                              + "[section2]\n"
                                              + "section_string = mySection2Value\n"
                                              + "section_number = 2\n"
                                              + "section_bool = true\n";

    private static final LocalDate DATE = LocalDate.of(2023, 5, 15);
    private static final String VALID_INI_DATE = "my_date = 2023-05-15\n";
    private static final String INVALID_INI_DATE = "my_date = 2023a05-15\n";

    private static final String VALID_INI_OTHER_TYPES = "my_class = java.lang.IllegalStateException\n"
                                                      + "my_ip = 127.0.0.1";

    private static final String VALID_INI_CUSTOM = "myInts=0,1,2,3,4\n"
                                                 + "myPair=agent1->0 0 * * SUN\n"
                                                 + "[section1]\n"
                                                 + "myBigDecimal=123456789.987654321";

    static class MyBeanPrivateConstructor
    {
        private MyBeanPrivateConstructor() {}
    }

    static class MyBeanPrivateSection
    {
        Section section1;

        static class Section
        {
            private Section() {} // unsupported
        }
    }

    static class MyIniDate
    {
        @Property("my_date")
        LocalDate myDate;

        public MyIniDate() {}
    }

    static class MyIniOtherTypes
    {
        @Property("my_class")
        Class<? extends Exception> myClass;
        @Property("my_ip")
        InetAddress myIp;

        public MyIniOtherTypes() {}
    }

    static class MyBeanCustomConverters
    {
        @Property(converter = MyIntsConverter.class)
        List<Integer> myInts;
        @Property(key = "myPair", converter = MyPairConverter.class)
        Pair<String, String> thePair;
        @Property("section1")
        Section section;

        public MyBeanCustomConverters() {}

        static class Section
        {
            @Property("myBigDecimal")
            BigDecimal myDecimal;

            public Section() {}
        }
    }


    private Mapper<MyIni> mapper = new INIToObjectMapper<>(MyIni.class);

    private ByteArrayInputStream toInputStream(String content)
    {
        return new ByteArrayInputStream(content.getBytes());
    }

    private MyIni testWithString(String string)
    {
        try
        {
            return mapper.apply(toInputStream(string));
        }
        catch (IOException exception)
        {
            fail("Test ended with an IOException, but this was not supposed to happen");
            return null;
        }
    }

    @Test
    void apply_validIni_validJSONObject() throws IOException
    {
        MyIni result = testWithString(VALID_INI_1);
        assertThat(result.getRootProperty(), is(equalTo("myRootValue")));

        assertThat(result.getSection1().getSectionString(), is(equalTo("mySection1Value")));
        assertThat(result.getSection1().getSectionNumber(), is(equalTo(1)));
        assertThat(result.getSection1().isSectionBoolean(), is(equalTo(false)));
        assertThat(result.getSection1().getTransientField(), is(equalTo(null)));

        assertThat(result.getSection2().getSectionString(), is(equalTo("mySection2Value")));
        assertThat(result.getSection2().getSectionNumber(), is(equalTo(2)));
        assertThat(result.getSection2().isSectionBoolean(), is(equalTo(true)));
        assertThat(result.getSection2().getTransientField(), is(equalTo(null)));
    }

    @Test
    void apply_missingTokenInSectionDeclaration_exception() throws IOException
    {
        assertThat(() -> testWithString(INVALID_INI_1), throwsException(ConfigurationSourceException.class)
                .withMessage(equalTo("Malformed INI: expected token ']' at line 2: \"[section1\"")));
    }

    @Test
    void apply_sectionDeclarationNoName_exception() throws IOException
    {
        assertThat(() -> testWithString(INVALID_INI_2), throwsException(ConfigurationSourceException.class)
                .withMessage(equalTo("Malformed INI: expected section name at line 2: \"[]\"")));
    }

    @Test
    void apply_valueWithoutProperty_exception() throws IOException
    {
        assertThat(() -> testWithString(INVALID_INI_3), throwsException(ConfigurationSourceException.class)
                .withMessage(equalTo("Malformed INI: expected property key at line 1: \"=value\"")));
    }

    @Test
    void apply_invalidLine_exception() throws IOException
    {
        assertThat(() -> testWithString(INVALID_INI_4), throwsException(ConfigurationSourceException.class)
                .withMessage(equalTo("Malformed INI: expected property at line 1: \"invalid line\"")));
    }

    @Test
    void apply_invalidType_exception() throws IOException
    {
        ConfigurationException exception = assertThrows(ConfigurationException.class,
                () -> testWithString(INVALID_INI_5));

        assertThat(exception.getMessage(), equalTo(
                "Unable to parse the value of the property ['number'] into a field of type 'double'"));

        Throwable cause = exception.getCause();
        assertThat(cause.getClass(), equalTo(ParseException.class));
        assertThat(cause.getMessage(), equalTo("Unparsable double: \"abc\""));

        Throwable rootCause = cause.getCause();
        assertThat(rootCause.getClass(), equalTo(NumberFormatException.class));
        assertThat(rootCause.getMessage(), equalTo("For input string: \"abc\""));
    }

    @Test
    void apply_invalidTypeInsideSection_exception() throws IOException
    {
        ConfigurationException exception = assertThrows(ConfigurationException.class,
                () -> testWithString(INVALID_INI_6));

        assertThat(exception.getMessage(), equalTo(
                "Unable to parse the value of the property ['section1']['section_number'] into a field of type 'int'"));

        Throwable cause = exception.getCause();
        assertThat(cause.getClass(), equalTo(ParseException.class));
        assertThat(cause.getMessage(), equalTo("Unparsable int: \"abc\""));

        Throwable rootCause = cause.getCause();
        assertThat(rootCause.getClass(), equalTo(NumberFormatException.class));
        assertThat(rootCause.getMessage(), equalTo("For input string: \"abc\""));
    }

    @Test
    void apply_sectionNotMapped_sectionSkipped() throws IOException
    {
        MyIni result = testWithString(VALID_INI_2);
        assertThat(result.getRootProperty(), is(equalTo("myRootValue")));
        assertThat(result.getSection1(), is(equalTo(null)));
        assertThat(result.getSection2().getSectionString(), is(equalTo("mySection2Value")));
        assertThat(result.getSection2().getSectionNumber(), is(equalTo(2)));
        assertThat(result.getSection2().isSectionBoolean(), is(equalTo(true)));
    }

    @Test
    void apply_beanWithPrivateConstructor_configurationException()
    {
        assertThat(
                () -> new INIToObjectMapper<>(MyBeanPrivateConstructor.class)
                        .apply(toInputStream(VALID_INI_1)),
                throwsException(ConfigurationException.class)
                        .withCause(ReflectiveOperationException.class));
    }

    @Test
    void apply_beanWithPrivateConstructorInSection_configurationException()
    {
        assertThat(
                () -> new INIToObjectMapper<>(MyBeanPrivateSection.class)
                        .apply(toInputStream(VALID_INI_1)),
                throwsException(ConfigurationException.class)
                        .withCause(ReflectiveOperationException.class));
    }

    @Test
    void apply_validIniDate_validObject() throws IOException
    {
        Mapper<MyIniDate> mapper = new INIToObjectMapper<>(MyIniDate.class);
        MyIniDate result = mapper.apply(toInputStream(VALID_INI_DATE));
        assertThat(result.myDate, equalTo(DATE));
    }

    @Test
    void apply_invalidDate_exception() throws IOException
    {
        Mapper<MyIniDate> mapper = new INIToObjectMapper<>(MyIniDate.class);
        ByteArrayInputStream ini = toInputStream(INVALID_INI_DATE);

        ConfigurationException exception = assertThrows(ConfigurationException.class,
                () -> mapper.apply(ini));

        assertThat(exception.getMessage(), equalTo(
                "Unable to parse the value of the property ['my_date'] into a field of type 'java.time.LocalDate'"));

        Throwable cause = exception.getCause();
        assertThat(cause.getClass(), equalTo(ParseException.class));
        assertThat(cause.getMessage(),
                equalTo("Unparsable java.time.LocalDate: \"2023a05-15\""));

        Throwable rootCause = cause.getCause();
        assertThat(rootCause.getClass(), equalTo(DateTimeParseException.class));
        assertThat(rootCause.getMessage(),
                equalTo("Text '2023a05-15' could not be parsed at index 4"));
    }

    @Test
    void apply_validIniOtherTypes_validObject() throws IOException
    {
        Mapper<MyIniOtherTypes> mapper = new INIToObjectMapper<>(MyIniOtherTypes.class);
        MyIniOtherTypes result = mapper.apply(toInputStream(VALID_INI_OTHER_TYPES));
        assertThat(result.myClass, equalTo(IllegalStateException.class));
        assertThat(result.myIp, equalTo(Inet4Address.getByAddress(new byte[] { 127, 0, 0, 1 })));
    }

    @Test
    void apply_validIniWithCustomConverters_success() throws IOException
    {
        MyBeanCustomConverters bean = new INIToObjectMapper<>(MyBeanCustomConverters.class)
                .apply(toInputStream(VALID_INI_CUSTOM));

        assertThat(bean.myInts, equalTo(asList(0, 1, 2, 3, 4)));
        assertThat(bean.thePair.getLeft(), equalTo("agent1"));
        assertThat(bean.thePair.getRight(), equalTo("0 0 * * SUN"));
        assertThat(bean.section.myDecimal, equalTo(new BigDecimal("123456789.987654321")));
    }

    @Test
    void configurationHelper_beanConfigurationHelper()
    {
        assertThat(mapper.configurationHelper(new MyIni()).getClass(), equalTo(BeanConfigurationHelper.class));
    }

}
