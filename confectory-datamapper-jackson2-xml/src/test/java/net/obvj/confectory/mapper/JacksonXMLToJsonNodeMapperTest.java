/*
 * Copyright 2021 obvj.net
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

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fasterxml.jackson.databind.JsonNode;

import net.obvj.confectory.internal.helper.JacksonJsonNodeHelper;

/**
 * Unit tests for the {@link JacksonXMLToJsonNodeMapper} class.
 *
 * @author oswaldo.bapvic.jr (Oswaldo Junior)
 * @since 0.3.0
 */
@ExtendWith(MockitoExtension.class)
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

    @Mock
    private JsonNode jsonNode;

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

        // Jackson modules cache shall not be populated by this type of mapper
        assertThat(JacksonXMLToObjectMapper.getCachedModules(), nullValue());
    }

    @Test
    void configurationHelper_jsonObjectConfigurationHelper()
    {
        assertThat(mapper.configurationHelper(jsonNode).getClass(), equalTo(JacksonJsonNodeHelper.class));
    }
}
