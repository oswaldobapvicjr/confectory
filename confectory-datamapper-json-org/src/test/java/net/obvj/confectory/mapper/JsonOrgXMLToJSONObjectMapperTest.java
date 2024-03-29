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
import static org.hamcrest.MatcherAssert.assertThat;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import net.obvj.confectory.internal.helper.JsonOrgJSONObjectHelper;

/**
 * Unit tests for the {@link JsonOrgXMLToJSONObjectMapper} class.
 *
 * @author oswaldo.bapvic.jr (Oswaldo Junior)
 * @since 0.2.0
 */
class JsonOrgXMLToJSONObjectMapperTest
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


    private Mapper<JSONObject> mapper = new JsonOrgXMLToJSONObjectMapper();

    private ByteArrayInputStream toInputStream(String content)
    {
        return new ByteArrayInputStream(content.getBytes());
    }

    @Test
    void apply_validInputStream_validJSONObject() throws IOException
    {
        JSONObject result = mapper.apply(toInputStream(TEST_XML_SAMPLE1));
        JSONObject rootElement = result.getJSONObject("root");
        assertThat(rootElement.length(), equalTo(3));
        assertThat(rootElement.get("intValue"), equalTo(9));
        assertThat(rootElement.get("booleanValue"), equalTo(true));

        JSONArray arrayElements = rootElement.getJSONObject("array").getJSONArray("element");
        assertThat(arrayElements.length(), equalTo(2));
        assertThat(arrayElements.get(0), equalTo("string1"));
        assertThat(arrayElements.get(1), equalTo("string2"));
    }

    @Test
    void configurationHelper_jsonObjectConfigurationHelper()
    {
        assertThat(mapper.configurationHelper(new JSONObject()).getClass(), equalTo(JsonOrgJSONObjectHelper.class));
    }

}
