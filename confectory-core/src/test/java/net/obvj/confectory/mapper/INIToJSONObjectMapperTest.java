package net.obvj.confectory.mapper;

import static net.obvj.junit.utils.matchers.AdvancedMatchers.throwsException;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.junit.jupiter.api.Test;

import net.minidev.json.JSONObject;
import net.obvj.confectory.ConfigurationSourceException;
import net.obvj.confectory.helper.JsonSmartConfigurationHelper;

/**
 * Unit tests for the {@link INIToJSONObjectMapper} class.
 *
 * @author oswaldo.bapvic.jr
 * @since 1.3.0
 */
class INIToJSONObjectMapperTest
{
    private static final String VALID_INI_1    = ";Comment\n"
                                               + "rootProperty = myRootValue\n"
                                               + "\n"
                                               + "[section1]\n"
                                               + "# Alternative comment\n"
                                               + "section_string = mySection1Value\n"
                                               + "section_number = 1\n"
                                               + "section_bool = false\n"
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

    private static final String INVALID_INI_3  = "=value\n";

    private static final String INVALID_INI_4  = "invalid line\n";

    private static final String VALID_INI_2    = "empty value = \n"; // this is OK

    private Mapper<JSONObject> mapper = new INIToJSONObjectMapper();

    private ByteArrayInputStream toInputStream(String content)
    {
        return new ByteArrayInputStream(content.getBytes());
    }

    private JSONObject testWithString(String string)
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
        JSONObject result = testWithString(VALID_INI_1);
        assertThat(result.size(), equalTo(3));
        assertThat(result.get("rootProperty"), equalTo("myRootValue"));

        JSONObject section1 = (JSONObject) result.get("section1");
        assertThat(section1.size(), equalTo(3));
        assertThat(section1.get("section_string"), equalTo("mySection1Value"));
        assertThat(section1.get("section_number"), equalTo(1));
        assertThat(section1.get("section_bool"), equalTo(false));

        JSONObject section2 = (JSONObject) result.get("section2");
        assertThat(section2.size(), equalTo(3));
        assertThat(section2.get("section_string"), equalTo("mySection2Value"));
        assertThat(section2.get("section_number"), equalTo(2));
        assertThat(section2.get("section_bool"), equalTo(true));
    }

    @Test
    void apply_validIni2_validJSONObject() throws IOException
    {
        JSONObject result = testWithString(VALID_INI_2);
        assertThat(result.size(), equalTo(1));
        assertThat(result.get("empty value"), equalTo(""));
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
    void configurationHelper_smartJSONObjectConfigurationHelper()
    {
        assertThat(mapper.configurationHelper(new JSONObject()).getClass(),
                equalTo(JsonSmartConfigurationHelper.class));
    }

}
