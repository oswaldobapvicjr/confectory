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
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.Properties;

import org.hamcrest.Matcher;
import org.junit.jupiter.api.Test;

import net.minidev.json.JSONObject;
import net.obvj.confectory.ConfigurationException;
import net.obvj.confectory.merger.JSONObjectConfigurationMerger;
import net.obvj.confectory.merger.PropertiesConfigurationMerger;
import net.obvj.junit.utils.Procedure;

/**
 * Unit tests for the {@link NullConfigurationHelper}.
 *
 * @author oswaldo.bapvic.jr (Oswaldo Junior)
 * @since 0.1.0
 */
class NullConfigurationHelperTest
{
    private static final String KEY1 = "key1";

    private static final Matcher<Procedure> EXCEPTION_NOT_FOUND = throwsException(
            ConfigurationException.class)
                .withMessageContaining("Not found");

    private NullConfigurationHelper<Object> helper = new NullConfigurationHelper<>();

    @Test
    void getBean_null()
    {
        assertNull(helper.getBean());
    }

    @Test
    void get_anyKey_null()
    {
        assertNull(helper.get(KEY1));
    }

    @Test
    void getBoolean_anyKey_null()
    {
        assertNull(helper.getBoolean(KEY1));
    }

    @Test
    void getInteger_anyKey_null()
    {
        assertNull(helper.getInteger(KEY1));
    }

    @Test
    void getLong_anyKey_null()
    {
        assertNull(helper.getLong(KEY1));
    }

    @Test
    void getDouble_anyKey_null()
    {
        assertNull(helper.getDouble(KEY1));
    }

    @Test
    void getString_anyKey_null()
    {
        assertNull(helper.getString(KEY1));
    }

    @Test
    void getMandatoryBoolean_anyKey_exception()
    {
        assertThat(() -> helper.getMandatoryBoolean(KEY1), EXCEPTION_NOT_FOUND);
    }

    @Test
    void getMandatoryInteger_anyKey_exception()
    {
        assertThat(() -> helper.getMandatoryInteger(KEY1), EXCEPTION_NOT_FOUND);
    }

    @Test
    void getMandatoryLong_anyKey_exception()
    {
        assertThat(() -> helper.getMandatoryLong(KEY1), EXCEPTION_NOT_FOUND);
    }

    @Test
    void getMandatoryDouble_anyKey_exception()
    {
        assertThat(() -> helper.getMandatoryDouble(KEY1), EXCEPTION_NOT_FOUND);
    }

    @Test
    void getMandatoryString_anyKey_exception()
    {
        assertThat(() -> helper.getMandatoryString(KEY1), EXCEPTION_NOT_FOUND);
    }

    @Test
    void configurationMerger_originalHelperNotPresent_unsupportedOperation()
    {
        assertThat(() -> helper.configurationMerger().getClass(),
                throwsException(UnsupportedOperationException.class));
    }

    @Test
    void configurationMerger_originalPropertiesHelper_propertiesMerger()
    {
        ConfigurationHelper<Properties> helper = new PropertiesConfigurationHelper(new Properties());
        assertThat(new NullConfigurationHelper<Properties>(helper).configurationMerger().getClass(),
                equalTo(PropertiesConfigurationMerger.class));
    }

    @Test
    void configurationMerger_originalJsonHelper_jsonMerger()
    {
        ConfigurationHelper<JSONObject> helper = new JsonSmartConfigurationHelper(new JSONObject());
        assertThat(new NullConfigurationHelper<JSONObject>(helper).configurationMerger().getClass(),
                equalTo(JSONObjectConfigurationMerger.class));
    }

}
