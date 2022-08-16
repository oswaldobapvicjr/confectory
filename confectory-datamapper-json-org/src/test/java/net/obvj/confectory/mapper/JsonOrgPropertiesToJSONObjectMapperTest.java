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

import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import net.obvj.confectory.internal.helper.JsonOrgJSONObjectHelper;

/**
 * Unit tests for the {@link JsonOrgPropertiesToJSONObjectMapper} class.
 *
 * @author oswaldo.bapvic.jr (Oswaldo Junior)
 * @since 0.2.0
 */
class JsonOrgPropertiesToJSONObjectMapperTest
{
    private static final String TEST_PROPERTIES_SAMPLE1
            = "intValue=9\n"
            + "booleanValue=true\n"
            + "stringValue=string1\n";


    private Mapper<JSONObject> mapper = new JsonOrgPropertiesToJSONObjectMapper();

    private ByteArrayInputStream toInputStream(String content)
    {
        return new ByteArrayInputStream(content.getBytes());
    }

    @Test
    void apply_validInputStream_validJSONObject() throws IOException
    {
        JSONObject result = mapper.apply(toInputStream(TEST_PROPERTIES_SAMPLE1));
        assertThat(result.length(), equalTo(3));
        assertThat(result.get("intValue"), equalTo("9"));
        assertThat(result.get("booleanValue"), equalTo("true"));
        assertThat(result.get("stringValue"), equalTo("string1"));
    }

    @Test
    void configurationHelper_jsonObjectConfigurationHelper()
    {
        assertThat(mapper.configurationHelper(new JSONObject()).getClass(), equalTo(JsonOrgJSONObjectHelper.class));
    }

}
