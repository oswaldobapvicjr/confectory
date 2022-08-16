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

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.junit.jupiter.api.Test;

import net.obvj.confectory.ConfigurationException;
import net.obvj.confectory.internal.helper.BeanConfigurationHelper;
import net.obvj.confectory.util.Property;

/**
 * Unit tests for the {@link PropertiesToObjectMapper} class.
 *
 * @author oswaldo.bapvic.jr (Oswaldo Junior)
 * @since 1.2.0
 */
class PropertiesToObjectMapperTest
{
    private static final String STRING1 = "string1";
    private static final int INT1 = 1910;

    private static final String TEST_PROPERTIES_CONTENT
                    = "booleanValue=true\n"
                    + "stringValue=string1\n"
                    + "intValue=1910\n";

    static class MyBeanNoExplicitMapping
    {
        Boolean booleanValue; // implicit mapping
        String stringValue;   // implicit mapping
        Integer intValue;     // implicit mapping

        public MyBeanNoExplicitMapping() {}
    }

    static class MyBeanExplicitMapping
    {
        @Property("booleanValue") boolean b; // explicit mapping
        @Property("stringValue") String s;   // explicit mapping
        @Property("intValue") int i;         // explicit mapping

        public MyBeanExplicitMapping() {}
    }

    static class MyBeanHybrid
    {
        @Property("booleanValue") boolean b; // explicit mapping
        @Property String stringValue;        // implicit mapping
        int intValue;                        // implicit mapping
        double unknownDouble;                // invalid property

        public MyBeanHybrid() {}
    }

    static class MyBeanAllFieldsTransient
    {
        transient boolean booleanValue; // implicit, but transient
        transient String stringValue;   // implicit, but transient
        transient int intValue;         // implicit, but transient

        public MyBeanAllFieldsTransient() {}
    }

    static class MyBeanPrivateConstructor
    {
        private MyBeanPrivateConstructor() {}
    }

    private static ByteArrayInputStream newInputStream()
    {
        return new ByteArrayInputStream(TEST_PROPERTIES_CONTENT.getBytes());
    }

    @Test
    void apply_beanWithNoExplicitMapping_success() throws IOException
    {
        MyBeanNoExplicitMapping bean = new PropertiesToObjectMapper<>(MyBeanNoExplicitMapping.class)
                .apply(newInputStream());

        assertThat(bean.booleanValue, equalTo(true));
        assertThat(bean.stringValue, equalTo(STRING1));
        assertThat(bean.intValue, equalTo(INT1));
    }

    @Test
    void apply_beanWithExplicitMapping_success() throws IOException
    {
        MyBeanExplicitMapping bean = new PropertiesToObjectMapper<>(MyBeanExplicitMapping.class)
                .apply(newInputStream());

        assertThat(bean.b, equalTo(true));
        assertThat(bean.s, equalTo(STRING1));
        assertThat(bean.i, equalTo(INT1));
    }

    @Test
    void apply_hybridBean_success() throws IOException
    {
        MyBeanHybrid bean = new PropertiesToObjectMapper<>(MyBeanHybrid.class)
                .apply(newInputStream());

        assertThat(bean.b, equalTo(true));
        assertThat(bean.stringValue, equalTo(STRING1));
        assertThat(bean.intValue, equalTo(INT1));
        assertThat(bean.unknownDouble, equalTo(0.0));
    }

    @Test
    void apply_beanWithAllFieldsTransient_noDataCopied() throws IOException
    {
        MyBeanAllFieldsTransient bean = new PropertiesToObjectMapper<>(MyBeanAllFieldsTransient.class)
                .apply(newInputStream());

        assertThat(bean.booleanValue, equalTo(false));
        assertThat(bean.stringValue, equalTo(null));
        assertThat(bean.intValue, equalTo(0));
    }

    @Test
    void apply_beanWithPrivateConstructor_configurationException()
    {
        assertThat(() ->
        {
            try
            {
                new PropertiesToObjectMapper<>(MyBeanPrivateConstructor.class)
                        .apply(newInputStream());
            }
            catch (IOException e)
            {
                throw new AssertionError("IOException happened, but ConfigurationException was expected", e);
            }
        }, throwsException(ConfigurationException.class).withCause(ReflectiveOperationException.class));
    }

    @Test
    void configurationHelper_propertiesConfigurationHelper()
    {
        assertThat(new PropertiesToObjectMapper<>(Object.class).configurationHelper(new Object()).getClass(),
                equalTo(BeanConfigurationHelper.class));
    }
}
