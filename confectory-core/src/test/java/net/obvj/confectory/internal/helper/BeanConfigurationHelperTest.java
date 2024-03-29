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
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

import org.hamcrest.Matcher;
import org.junit.jupiter.api.Test;

import net.obvj.confectory.ConfigurationException;
import net.obvj.junit.utils.Procedure;

/**
 * Unit tests for the {@link BeanConfigurationHelper}.
 *
 * @author oswaldo.bapvic.jr (Oswaldo Junior)
 * @since 0.4.0
 */
class BeanConfigurationHelperTest
{
    private static final String KEY = "key";
    private static final String OBJECT = "test";
    private static final ConfigurationHelper<String> HELPER = new BeanConfigurationHelper<>(OBJECT);

    private static final Matcher<Procedure> CONFIGURATION_EXCEPTION_TYPE_NOT_SUPPORTED = throwsException(
            ConfigurationException.class).withMessage("Operation not supported for bean of type 'java.lang.String'");

    @Test
    void getBean_notEmpty()
    {
        assertThat(HELPER.getBean(), is(sameInstance(OBJECT)));
    }

    @Test
    void getAsString_sameAsToString()
    {
        assertThat(HELPER.getAsString(), is(equalTo(OBJECT.toString())));
    }

    @Test
    void get_existingKey_exception()
    {
        assertThat(() -> HELPER.get(KEY), CONFIGURATION_EXCEPTION_TYPE_NOT_SUPPORTED);
    }

    @Test
    void getBoolean_existingKey_exception()
    {
        assertThat(() -> HELPER.getBoolean(KEY), CONFIGURATION_EXCEPTION_TYPE_NOT_SUPPORTED);
    }

    @Test
    void getInteger_existingKey_exception()
    {
        assertThat(() -> HELPER.getInteger(KEY), CONFIGURATION_EXCEPTION_TYPE_NOT_SUPPORTED);
    }

    @Test
    void getLong_existingKey_exception()
    {
        assertThat(() -> HELPER.getLong(KEY), CONFIGURATION_EXCEPTION_TYPE_NOT_SUPPORTED);
    }

    @Test
    void getDouble_existingKey_exception()
    {
        assertThat(() -> HELPER.getDouble(KEY), CONFIGURATION_EXCEPTION_TYPE_NOT_SUPPORTED);
    }

    @Test
    void getString_existingKey_exception()
    {
        assertThat(() -> HELPER.getString(KEY), CONFIGURATION_EXCEPTION_TYPE_NOT_SUPPORTED);
    }

    @Test
    void getMandatoryString_existingKey_exception()
    {
        assertThat(() -> HELPER.getMandatoryString(KEY), CONFIGURATION_EXCEPTION_TYPE_NOT_SUPPORTED);
    }

    @Test
    void configurationMerger_unsupportedOperation()
    {
        assertThat(() -> HELPER.configurationMerger(), throwsException(UnsupportedOperationException.class)
                .withMessageContaining("not supported", "user-defined bean"));
    }

}
