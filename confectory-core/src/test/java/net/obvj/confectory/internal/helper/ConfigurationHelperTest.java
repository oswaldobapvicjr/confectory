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

package net.obvj.confectory.internal.helper;

import static net.obvj.junit.utils.matchers.AdvancedMatchers.throwsException;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import net.obvj.confectory.mapper.Mapper;

/**
 * Unit tests for the {@link ConfigurationHelper} class.
 *
 * @author oswaldo.bapvic.jr (Oswaldo Junior)
 * @since 2.2.0
 */
@ExtendWith(MockitoExtension.class)
class ConfigurationHelperTest
{

    @Mock
    private Mapper<String> mapper;
    @Mock
    private ConfigurationHelper<String> helper;

    private final String bean = "test";

    @Test
    void newInstance_validBeanAndMapper_properConfigurationHelper()
    {
        when(mapper.configurationHelper(bean)).thenReturn(helper);
        ConfigurationHelper<String> result = ConfigurationHelper.newInstance(bean, mapper);
        assertThat(result, equalTo(helper));
    }

    @Test
    void newInstance_nullBeanAndValidMapper_nullConfigurationHelper()
    {
        ConfigurationHelper<String> result = ConfigurationHelper.newInstance(null, mapper);
        assertThat(result.getClass(), equalTo(NullConfigurationHelper.class));
    }

    @Test
    void newInstance_nullMapper_exception()
    {
        assertThat(() -> ConfigurationHelper.newInstance(bean, null),
                throwsException(NullPointerException.class).withMessage("The mapper must not be null"));
    }


}
