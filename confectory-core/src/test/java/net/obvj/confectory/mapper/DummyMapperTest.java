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
import static org.junit.jupiter.api.Assertions.fail;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.junit.jupiter.api.Test;

import net.minidev.json.JSONObject;
import net.obvj.confectory.ConfigurationException;
import net.obvj.confectory.internal.helper.JsonSmartConfigurationHelper;

/**
 * Unit tests for the {@link DummyMapper} class.
 *
 * @author oswaldo.bapvic.jr (Oswaldo Junior)
 * @since 2.1.0
 */
class DummyMapperTest
{
    private Mapper<JSONObject> dummyJSONObjectMapper = new DummyMapper<>(JsonSmartConfigurationHelper::new);

    @Test
    void apply_configurationException() throws IOException
    {
        assertThat(() ->
        {
            try
            {
                dummyJSONObjectMapper.apply(new ByteArrayInputStream("".getBytes()));
            }
            catch (IOException exception)
            {
                fail("Unexpected exception", exception);
            }
        }, throwsException(ConfigurationException.class).withMessage("Dummy mapper"));
    }

    @Test
    void configurationHelper_json_jsonSmartConfigurationHelper()
    {
        assertThat(dummyJSONObjectMapper.configurationHelper(new JSONObject()).getClass(),
                equalTo(JsonSmartConfigurationHelper.class));
    }
}
