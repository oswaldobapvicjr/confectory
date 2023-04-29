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

import static java.util.Collections.singletonList;
import static net.obvj.junit.utils.matchers.AdvancedMatchers.containsAll;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidDefinitionException;

import net.obvj.confectory.internal.helper.BeanConfigurationHelper;
import net.obvj.confectory.mapper.model.MyBean;
import net.obvj.confectory.mapper.model.MyBeanWithMoney;

/**
 * Unit tests for the {@link JacksonJsonToObjectMapper} class.
 *
 * @author oswaldo.bapvic.jr
 * @since 0.3.0
 */
class JacksonJsonToObjectMapperTest
{
    private static final String TEST_JSON_SAMPLE1 = "{"
                                                  + "  \"intValue\": 9,"
                                                  + "  \"booleanValue\": true,"
                                                  + "  \"array\": ["
                                                  + "    \"string1\","
                                                  + "    \"string2\""
                                                  + "  ]"
                                                  + "}";

    private static final String TEST_JSON_SAMPLE2 = "{"
                                                  + "  \"product\": \"Notebook\","
                                                  + "  \"price\": {"
                                                  + "    \"amount\": 4999.95,"
                                                  + "    \"currency\": \"BRL\""
                                                  + "  }"
                                                  + "}";

    private Mapper<MyBean> mapper = new JacksonJsonToObjectMapper<>(MyBean.class);

    @AfterEach
    private void cleanup()
    {
        JacksonJsonToObjectMapper.resetModulesCache();
    }

    private ByteArrayInputStream toInputStream(String content)
    {
        return new ByteArrayInputStream(content.getBytes());
    }

    @Test
    void apply_validInputStream_validObject() throws IOException
    {
        MyBean result = mapper.apply(toInputStream(TEST_JSON_SAMPLE1));
        assertThat(result.intValue, equalTo(9));
        assertThat(result.booleanValue, equalTo(true));
        List<String> array = result.array;
        assertThat(array.size(), equalTo(2));
        assertThat(array.containsAll(Arrays.asList("string1", "string2")), equalTo(true));
    }

    @Test
    void apply_jsonSample2WithModuleSupport_validObject() throws IOException
    {
        MyBeanWithMoney result = new JacksonJsonToObjectMapper<>(MyBeanWithMoney.class)
                .apply(toInputStream(TEST_JSON_SAMPLE2));

        assertThat(result.product, equalTo("Notebook"));
        assertThat(result.price.getNumber().doubleValueExact(), equalTo(4999.95));
        assertThat(result.price.getCurrency().getCurrencyCode(), equalTo("BRL"));
    }

    @Test
    void apply_jsonSample2WithoutModuleSupport_validObject()
    {
        JacksonJsonToObjectMapper<MyBeanWithMoney> mapper = new JacksonJsonToObjectMapper<>(
                MyBeanWithMoney.class, true);
        try
        {
            mapper.apply(toInputStream(TEST_JSON_SAMPLE2));
        }
        catch (IOException exception)
        {
            assertThat(exception.getClass(), equalTo(InvalidDefinitionException.class));
            assertThat(exception.getMessage(),
                    containsAll("Cannot construct instance of `javax.money.MonetaryAmount`"));
        }
    }

    @Test
    void reloadModulesCache_verify() throws IOException
    {
        Module module = mock(Module.class);
        when(module.getModuleName()).thenReturn("Module1");
        when(module.version()).thenReturn(mock(Version.class));
        List<com.fasterxml.jackson.databind.Module> modules = singletonList(module);

        try (MockedStatic<ObjectMapper> mocked = mockStatic(ObjectMapper.class))
        {
            mocked.when(ObjectMapper::findModules).thenReturn(modules);

            new JacksonJsonToObjectMapper<>(MyBean.class).apply(toInputStream(TEST_JSON_SAMPLE1));
            new JacksonJsonToObjectMapper<>(MyBean.class).apply(toInputStream(TEST_JSON_SAMPLE1));
            mocked.verify(ObjectMapper::findModules, times(1));

            JacksonJsonToObjectMapper.reloadModulesCache();
            mocked.verify(ObjectMapper::findModules, times(2));
        }
    }

    @Test
    void configurationHelper_beanConfigurationHelper()
    {
        assertThat(mapper.configurationHelper(new MyBean()).getClass(), equalTo(BeanConfigurationHelper.class));
    }
}
