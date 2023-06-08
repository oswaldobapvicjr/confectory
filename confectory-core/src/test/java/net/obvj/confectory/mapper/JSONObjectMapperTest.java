/*
 * Copyright 2022 obvj.net
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

import static net.obvj.junit.utils.matchers.AdvancedMatchers.throwsException;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.junit.jupiter.api.Test;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.ParseException;
import net.obvj.confectory.ConfigurationException;
import net.obvj.confectory.internal.helper.JsonSmartConfigurationHelper;

/**
 * Unit tests for the {@link JSONObjectMapper} class.
 *
 * @author oswaldo.bapvic.jr
 * @since 2.0.0
 */
class JSONObjectMapperTest
{
    private static final String TEST_JSON_SAMPLE1 = "{"
                                                  + "  \"intValue\": 9,"
                                                  + "  \"doubleValue\": 23.98,"
                                                  + "  \"booleanValue\": true,"
                                                  + "  \"array\": ["
                                                  + "    \"string1\","
                                                  + "    \"string2\""
                                                  + "  ]"
                                                  + "}";

    private static final String TEST_JSON_INVALID = "{";

    private Mapper<JSONObject> mapper = new JSONObjectMapper();

    private ByteArrayInputStream toInputStream(String content)
    {
        return new ByteArrayInputStream(content.getBytes());
    }

    @Test
    void apply_validInputStream_validJSONObject() throws IOException
    {
        JSONObject result = mapper.apply(toInputStream(TEST_JSON_SAMPLE1));
        assertThat(result.size(), equalTo(4));
        assertThat(result.get("intValue"), equalTo(9));
        assertThat(result.get("doubleValue"), equalTo(23.98));
        assertThat(result.get("booleanValue"), equalTo(true));

        JSONArray array = (JSONArray) result.get("array");
        assertThat(array.size(), equalTo(2));
        assertThat(array.get(0), equalTo("string1"));
        assertThat(array.get(1), equalTo("string2"));
    }

    @Test
    void apply_invalidJSON_configurationException() throws IOException
    {
        assertThat(() -> mapper.apply(toInputStream(TEST_JSON_INVALID)),
                throwsException(ConfigurationException.class).withCause(ParseException.class));
    }

    @Test
    void configurationHelper_jsonObjectConfigurationHelper()
    {
        assertThat(mapper.configurationHelper(new JSONObject()).getClass(),
                equalTo(JsonSmartConfigurationHelper.class));
    }

}
