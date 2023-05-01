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
 * Unit tests for the {@link JacksonYAMLToJsonNodeMapper} class.
 *
 * @author oswaldo.bapvic.jr
 * @since 0.3.0
 */
@ExtendWith(MockitoExtension.class)
class JacksonYAMLToJsonNodeMapperTest
{
    private static final String TEST_YAML_SAMPLE1
            = "intValue: 9\r\n"
            + "booleanValue: true\r\n"
            + "doubleValue: 9.87654321\r\n"
            + "array:\r\n"
            + "  - string1\r\n"
            + "  - string2";

    @Mock
    private JsonNode jsonNode;

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

        // Jackson modules cache shall not be populated by this type of mapper
        assertThat(JacksonJsonToObjectMapper.getCachedModules(), nullValue());
    }

    @Test
    void configurationHelper_jsonObjectConfigurationHelper()
    {
        assertThat(mapper.configurationHelper(jsonNode).getClass(), equalTo(JacksonJsonNodeHelper.class));
    }

}
