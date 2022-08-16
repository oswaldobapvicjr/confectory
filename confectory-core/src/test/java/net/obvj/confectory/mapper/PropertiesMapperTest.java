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
import java.util.Properties;

import org.junit.jupiter.api.Test;

import net.obvj.confectory.internal.helper.PropertiesConfigurationHelper;

/**
 * Unit tests for the {@link PropertiesMapper} class.
 *
 * @author oswaldo.bapvic.jr (Oswaldo Junior)
 * @since 0.1.0
 */
class PropertiesMapperTest
{
    private static final String TEST_PROPERTIES_CONTENT = "web.host=localhost\nweb.port=1910";

    private Mapper<Properties> mapper = new PropertiesMapper();

    @Test
    void apply_validInputStream_loadedSuccessfully() throws IOException
    {
        Properties properties = mapper.apply(new ByteArrayInputStream(TEST_PROPERTIES_CONTENT.getBytes()));
        assertThat(properties.getProperty("web.host"), equalTo("localhost"));
        assertThat(properties.getProperty("web.port"), equalTo("1910"));
    }

    @Test
    void configurationHelper_propertiesConfigurationHelper()
    {
        assertThat(mapper.configurationHelper(new Properties()).getClass(),
                equalTo(PropertiesConfigurationHelper.class));
    }
}
