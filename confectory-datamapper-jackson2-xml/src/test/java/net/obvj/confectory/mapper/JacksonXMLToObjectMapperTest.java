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

import static net.obvj.junit.utils.matchers.AdvancedMatchers.containsAll;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.exc.InvalidDefinitionException;

import net.obvj.confectory.internal.helper.BeanConfigurationHelper;
import net.obvj.confectory.mapper.model.MyBean;
import net.obvj.confectory.mapper.model.MyBeanWithMoney;

/**
 * Unit tests for the {@link JacksonXMLToObjectMapper} class.
 *
 * @author oswaldo.bapvic.jr (Oswaldo Junior)
 * @since 0.3.0
 */
class JacksonXMLToObjectMapperTest
{
    private static final String TEST_XML_SAMPLE1
            = "<root>\n"
            + "  <intValue>9</intValue>\n"
            + "  <booleanValue>true</booleanValue>\n"
            + "  <array>\n"
            + "    <element>string1</element>\n"
            + "    <element>string2</element>\n"
            + "  </array>\n"
            + "</root>\n";

    private static final String TEST_XML_SAMPLE2
            = "<root>\n"
            + "  <product>Notebook</product>\n"
            + "  <price amount=\"4999.95\" currency=\"BRL\"/>\n"
            + "</root>\n";

    private Mapper<MyBean> mapper = new JacksonXMLToObjectMapper<>(MyBean.class);

    @AfterEach
    private void cleanup()
    {
        JacksonXMLToObjectMapper.resetModulesCache();
    }

    private ByteArrayInputStream toInputStream(String content)
    {
        return new ByteArrayInputStream(content.getBytes());
    }

    @Test
    void apply_validInputStream_validJsonNode() throws IOException
    {
        MyBean result = mapper.apply(toInputStream(TEST_XML_SAMPLE1));
        assertThat(result.intValue, equalTo(9));
        assertThat(result.booleanValue, equalTo(true));
        List<String> array = result.array;
        assertThat(array.size(), equalTo(2));
        assertThat(array.containsAll(Arrays.asList("string1", "string2")), equalTo(true));
    }

    @Test
    void apply_jsonSample2WithModuleSupport_validObject() throws IOException
    {
        MyBeanWithMoney result = new JacksonXMLToObjectMapper<>(MyBeanWithMoney.class)
                .apply(toInputStream(TEST_XML_SAMPLE2));

        assertThat(result.product, equalTo("Notebook"));
        assertThat(result.price.getNumber().doubleValueExact(), equalTo(4999.95));
        assertThat(result.price.getCurrency().getCurrencyCode(), equalTo("BRL"));
    }

    @Test
    void apply_jsonSample2WithoutModuleSupport_exception()
    {
        Mapper<MyBeanWithMoney> mapper = new JacksonXMLToObjectMapper<>(
                MyBeanWithMoney.class, true);
        try
        {
            mapper.apply(toInputStream(TEST_XML_SAMPLE2));
        }
        catch (IOException exception)
        {
            assertThat(exception.getClass(), equalTo(InvalidDefinitionException.class));
            assertThat(exception.getMessage(),
                    containsAll("Cannot construct instance of `javax.money.MonetaryAmount`"));
        }
    }

    @Test
    void configurationHelper_beanConfigurationHelper()
    {
        assertThat(mapper.configurationHelper(new MyBean()).getClass(), equalTo(BeanConfigurationHelper.class));
    }
}
