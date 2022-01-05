package net.obvj.confectory.mapper;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import net.minidev.json.JSONObject;
import net.obvj.confectory.helper.SmartJsonConfigurationHelper;

/**
 * Unit tests for the {@link YAMLToJSONObjectMapper} class.
 *
 * @author oswaldo.bapvic.jr
 * @since 1.1.0
 */
@ExtendWith(MockitoExtension.class)
class YAMLToJSONObjectMapperTest
{
    private static final String TEST_YAML_SAMPLE1 = "intValue: 9\n"
                                                  + "booleanValue: true\n"
                                                  + "stringValue: myString\n"
                                                  + "\n";
    @Mock
    private JSONObject jsonNode;

    private Mapper<JSONObject> mapper = new YAMLToJSONObjectMapper();

    private ByteArrayInputStream toInputStream(String content)
    {
        return new ByteArrayInputStream(content.getBytes());
    }

    @Test
    void apply_validInputStream_validJsonNode() throws IOException
    {
        JSONObject result = mapper.apply(toInputStream(TEST_YAML_SAMPLE1));
        assertThat(result.size(), equalTo(3));
        assertThat(result.get("intValue"), equalTo(9));
        assertThat(result.get("booleanValue"), equalTo(true));
        assertThat(result.get("stringValue"), equalTo("myString"));
    }

    @Test
    void configurationHelper_jsonObjectConfigurationHelper()
    {
        assertThat(mapper.configurationHelper(jsonNode).getClass(), equalTo(SmartJsonConfigurationHelper.class));
    }

}
