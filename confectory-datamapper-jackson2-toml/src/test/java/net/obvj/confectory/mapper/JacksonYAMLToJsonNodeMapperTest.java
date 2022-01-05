package net.obvj.confectory.mapper;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fasterxml.jackson.databind.JsonNode;

import net.obvj.confectory.helper.JacksonJsonNodeHelper;

/**
 * Unit tests for the {@link JacksonTOMLToJsonNodeMapper} class.
 *
 * @author oswaldo.bapvic.jr
 * @since 1.1.0
 */
@ExtendWith(MockitoExtension.class)
class JacksonYAMLToJsonNodeMapperTest
{
    private static final String TEST_TOML_SAMPLE1
            = "intValue = 9\n"
            + "booleanValue = true\n"
            + "doubleValue = 9.87654321\n"
            + "array = [\"string1\", \"string2\"]\n"
            + "[section]\n"
            + "string = \"mySectionStringValue1\"\n"
            + "\n";

    @Mock
    private JsonNode jsonNode;

    private Mapper<JsonNode> mapper = new JacksonTOMLToJsonNodeMapper();

    private ByteArrayInputStream toInputStream(String content)
    {
        return new ByteArrayInputStream(content.getBytes());
    }

    @Test
    void apply_validInputStream_validJsonNode() throws IOException
    {
        JsonNode result = mapper.apply(toInputStream(TEST_TOML_SAMPLE1));
        assertThat(result.size(), equalTo(5));
        assertThat(result.get("intValue").asInt(), equalTo(9));
        assertThat(result.get("booleanValue").asBoolean(), equalTo(true));
        assertThat(result.get("doubleValue").asDouble(), equalTo(9.87654321));

        JsonNode array = result.get("array");
        assertThat(array.size(), equalTo(2));
        assertThat(array.get(0).asText(), equalTo("string1"));
        assertThat(array.get(1).asText(), equalTo("string2"));

        assertThat(result.get("section").get("string").asText(), equalTo("mySectionStringValue1"));
    }

    @Test
    void configurationHelper_jsonObjectConfigurationHelper()
    {
        assertThat(mapper.configurationHelper(jsonNode).getClass(), equalTo(JacksonJsonNodeHelper.class));
    }

}
