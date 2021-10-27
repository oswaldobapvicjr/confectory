package net.obvj.confectory.mapper;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * Unit tests for the {@link JacksonXMLToJsonNodeMapper} class.
 *
 * @author oswaldo.bapvic.jr
 * @since 0.3.0
 */
class JacksonXMLToJsonNodeMapperTest
{
    private static final String TEST_XML_SAMPLE1
            = "<root>\n"
            + "  <intValue>9</intValue>\n"
            + "  <booleanValue>true</booleanValue>\n"
            + "  <array>\n"
            + "    <element>string1</element>\n"
            + "    <element>string2</element>\n"
            + "  </array>\n"
            + "</root>\n";

    private Mapper<JsonNode> mapper = new JacksonXMLToJsonNodeMapper();

    private ByteArrayInputStream toInputStream(String content)
    {
        return new ByteArrayInputStream(content.getBytes());
    }

    @Test
    void apply_validInputStream_validJsonNode() throws IOException
    {
        JsonNode result = mapper.apply(toInputStream(TEST_XML_SAMPLE1));
        assertThat(result.size(), equalTo(3));
        assertThat(result.get("intValue").asInt(), equalTo(9));
        assertThat(result.get("booleanValue").asBoolean(), equalTo(true));

        JsonNode array = result.get("array").get("element");
        assertThat(array.size(), equalTo(2));
        assertThat(array.get(0).asText(), equalTo("string1"));
        assertThat(array.get(1).asText(), equalTo("string2"));
    }

}
