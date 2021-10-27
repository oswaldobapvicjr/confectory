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
 * Unit tests for the {@link JacksonJsonNodeMapper} class.
 *
 * @author oswaldo.bapvic.jr
 * @since 0.3.0
 */
@ExtendWith(MockitoExtension.class)
class JacksonJsonNodeMapperTest
{
    private static final String TEST_JSON_SAMPLE1 = "{"
                                                  + "  \"intValue\": 9,"
                                                  + "  \"booleanValue\": true,"
                                                  + "  \"array\": ["
                                                  + "    \"string1\","
                                                  + "    \"string2\""
                                                  + "  ]"
                                                  + "}";
    @Mock
    private JsonNode jsonNode;

    private Mapper<JsonNode> mapper = new JacksonJsonNodeMapper();

    private ByteArrayInputStream toInputStream(String content)
    {
        return new ByteArrayInputStream(content.getBytes());
    }

    @Test
    void apply_validInputStream_validJsonNode() throws IOException
    {
        JsonNode result = mapper.apply(toInputStream(TEST_JSON_SAMPLE1));
        assertThat(result.size(), equalTo(3));
        assertThat(result.get("intValue").asInt(), equalTo(9));
        assertThat(result.get("booleanValue").asBoolean(), equalTo(true));

        JsonNode array = result.get("array");
        assertThat(array.size(), equalTo(2));
        assertThat(array.get(0).asText(), equalTo("string1"));
        assertThat(array.get(1).asText(), equalTo("string2"));
    }

    @Test
    void configurationHelper_jsonObjectConfigurationHelper()
    {
        assertThat(mapper.configurationHelper(jsonNode).getClass(), equalTo(JacksonJsonNodeHelper.class));
    }

}
