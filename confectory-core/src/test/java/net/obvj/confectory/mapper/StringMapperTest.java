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

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.junit.jupiter.api.Test;

import net.obvj.confectory.internal.helper.BeanConfigurationHelper;

/**
 * Unit tests for the {@link StringMapper} class.
 *
 * @author oswaldo.bapvic.jr (Oswaldo Junior)
 * @since 0.1.0
 */
class StringMapperTest
{
    private static final String TEST_CONTENT = "content";

    private Mapper<String> mapper = new StringMapper();

    @Test
    void apply_validInputStream_loadedSuccessfully() throws IOException
    {
        String result = mapper.apply(new ByteArrayInputStream(TEST_CONTENT.getBytes()));
        assertThat(result, equalTo(TEST_CONTENT));
    }

    @Test
    void configurationHelper_beanConfigurationHelper()
    {
        assertThat(mapper.configurationHelper(TEST_CONTENT).getClass(), equalTo(BeanConfigurationHelper.class));
    }
}
