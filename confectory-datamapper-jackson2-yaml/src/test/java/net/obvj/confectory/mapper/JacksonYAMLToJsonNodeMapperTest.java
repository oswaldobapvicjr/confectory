package net.obvj.confectory.mapper;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * Unit tests for the {@link JacksonYAMLToJsonNodeMapper} class.
 *
 * @author oswaldo.bapvic.jr
 * @since 0.3.0
 */
class JacksonYAMLToJsonNodeMapperTest
{
    private static final String TEST_YAML_SAMPLE1
            = "intValue: 9\r\n"
            + "booleanValue: true\r\n"
            + "doubleValue: 9.87654321\r\n"
            + "array:\r\n"
            + "  - string1\r\n"
            + "  - string2";

    private Mapper<JsonNode> mapper = new JacksonYAMLToJsonNodeMapper();

    private ByteArrayInputStream toInputStream(String content)
    {
        return new ByteArrayInputStream(content.getBytes());
    }

    @Test
    void apply_validInputStream_validJsonNode() throws IOException
    {
        JsonNode result = mapper.apply(toInputStream(TEST_YAML_SAMPLE1));
        assertThat(result.size(), equalTo(4));
        assertThat(result.get("intValue").asInt(), equalTo(9));
        assertThat(result.get("booleanValue").asBoolean(), equalTo(true));
        assertThat(result.get("doubleValue").asDouble(), equalTo(9.87654321));

        JsonNode array = result.get("array");
        assertThat(array.size(), equalTo(2));
        assertThat(array.get(0).asText(), equalTo("string1"));
        assertThat(array.get(1).asText(), equalTo("string2"));
    }

}
