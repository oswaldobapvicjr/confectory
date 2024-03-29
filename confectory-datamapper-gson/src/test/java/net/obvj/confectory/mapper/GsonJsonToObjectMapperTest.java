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
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import net.obvj.confectory.internal.helper.BeanConfigurationHelper;
import net.obvj.confectory.mapper.model.MyBean;

/**
 * Unit tests for the {@link GsonJsonToObjectMapper} class.
 *
 * @author oswaldo.bapvic.jr
 * @since 0.4.0
 */
class GsonJsonToObjectMapperTest
{
    private static final String TEST_JSON_SAMPLE1 = "{"
                                                  + "  \"intValue\": 9,"
                                                  + "  \"booleanValue\": true,"
                                                  + "  \"array\": ["
                                                  + "    \"string1\","
                                                  + "    \"string2\""
                                                  + "  ]"
                                                  + "}";

    private Mapper<MyBean> mapper = new GsonJsonToObjectMapper<>(MyBean.class);

    private ByteArrayInputStream toInputStream(String content)
    {
        return new ByteArrayInputStream(content.getBytes());
    }

    @Test
    void apply_validInputStream_validJsonNode() throws IOException
    {
        MyBean result = mapper.apply(toInputStream(TEST_JSON_SAMPLE1));
        assertThat(result.intValue, equalTo(9));
        assertThat(result.booleanValue, equalTo(true));
        List<String> array = result.array;
        assertThat(array.size(), equalTo(2));
        assertThat(array.containsAll(Arrays.asList("string1", "string2")), equalTo(true));
    }

    @Test
    void configurationHelper_beanConfigurationHelper()
    {
        assertThat(mapper.configurationHelper(new MyBean()).getClass(), equalTo(BeanConfigurationHelper.class));
    }
}
