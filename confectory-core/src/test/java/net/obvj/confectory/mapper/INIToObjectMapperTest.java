package net.obvj.confectory.mapper;

import static net.obvj.junit.utils.matchers.AdvancedMatchers.throwsException;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.junit.jupiter.api.Test;

import net.obvj.confectory.ConfigurationException;
import net.obvj.confectory.ConfigurationSourceException;
import net.obvj.confectory.helper.BeanConfigurationHelper;
import net.obvj.confectory.mapper.model.MyIni;

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
        assertThat(() -> testWithString(INVALID_INI_5),
                throwsException(ConfigurationException.class).withMessage(equalTo(
                        "The value defined for property ['number'] cannot be parsed as 'double'"))
                        .withCause(NumberFormatException.class));
    }

    @Test
    void apply_invalidTypeInsideSection_exception() throws IOException
    {
        assertThat(() -> testWithString(INVALID_INI_6),
                throwsException(ConfigurationException.class).withMessage(equalTo(
                        "The value defined for property ['section1']['section_number'] cannot be parsed as 'int'"))
                        .withCause(NumberFormatException.class));
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
        assertThat(() ->
        {
            try
            {
                new INIToObjectMapper<>(MyBeanPrivateConstructor.class).apply(toInputStream(VALID_INI_1));
            }
            catch (IOException e)
            {
                throw new AssertionError("IOException happened, but ConfigurationException was expected", e);
            }
        }, throwsException(ConfigurationException.class).withCause(ReflectiveOperationException.class));
    }

    @Test
    void apply_beanWithPrivateConstructorInSection_configurationException()
    {
        assertThat(() ->
        {
            try
            {
                new INIToObjectMapper<>(MyBeanPrivateSection.class).apply(toInputStream(VALID_INI_1));
            }
            catch (IOException e)
            {
                throw new AssertionError("IOException happened, but ConfigurationException was expected", e);
            }
        }, throwsException(ConfigurationException.class).withCause(ReflectiveOperationException.class));
    }

    @Test
    void configurationHelper_beanConfigurationHelper()
    {
        assertThat(mapper.configurationHelper(new MyIni()).getClass(), equalTo(BeanConfigurationHelper.class));
    }

}
