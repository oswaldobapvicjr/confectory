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

package net.obvj.confectory;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;

import net.obvj.confectory.mapper.StringMapper;
import net.obvj.confectory.source.StringSource;

/**
 * Unit tests for the {@link TypeSafeConfigurationContainer} class;
 *
 * @author oswaldo.bapvic.jr (Oswaldo Junior)
 * @since 1.0.0
 */
class TypeSafeConfigurationContainerTest
{
    private static final String NAMESPACE1 = "namespace1";
    private static final String NAMESPACE2 = "namespace2";

    private static final String BEAN_1_1 = "bean-1-1";
    private static final String BEAN_1_2 = "bean-1-2";
    private static final String BEAN_2_1 = "bean-2-1";
    private static final String BEAN_3 = "bean-3";

    private static final Configuration<String> CONF_NS1_BEAN_1 = Configuration.<String>builder().namespace(NAMESPACE1)
            .precedence(11).mapper(new StringMapper()).source(new StringSource<>(BEAN_1_1)).build();

    private static final Configuration<String> CONF_NS1_BEAN_2 = Configuration.<String>builder().namespace(NAMESPACE1)
            .precedence(22).mapper(new StringMapper()).source(new StringSource<>(BEAN_1_2)).build();

    private static final Configuration<String> CONF_NS2_BEAN_1 = Configuration.<String>builder().namespace(NAMESPACE2)
            .precedence(1).mapper(new StringMapper()).source(new StringSource<>(BEAN_2_1)).build();

    // Configuration without namespace
    private static final Configuration<String> CONF_BEAN_3 = Configuration.<String>builder().precedence(0)
            .mapper(new StringMapper()).source(new StringSource<>(BEAN_3)).build();

    private TypeSafeConfigurationContainer<String> container;

    @Test
    void constructor_empty_default()
    {
        container = new TypeSafeConfigurationContainer<>();
        assertThat(container.size(), equalTo(0L));
        assertThat(container.getInternal().getDataFetchStrategy(), equalTo(DataFetchStrategy.STRICT));
    }

    @Test
    void getBean_noArgumentAndOnlyConfigsWithNamespace_null()
    {
        container = new TypeSafeConfigurationContainer<>(CONF_NS1_BEAN_1, CONF_NS2_BEAN_1);
        assertThat(container.getBean(), equalTo(null));
    }

    @Test
    void getBean_noArgument_configWithoutNamespace()
    {
        container = new TypeSafeConfigurationContainer<>(CONF_NS1_BEAN_1, CONF_NS2_BEAN_1, CONF_BEAN_3);
        assertThat(container.getBean(), equalTo(BEAN_3));
    }

    @Test
    void getBean_invalidNamespace_null()
    {
        container = new TypeSafeConfigurationContainer<>(CONF_NS1_BEAN_1, CONF_NS2_BEAN_1, CONF_BEAN_3);
        assertThat(container.getBean("invalid"), equalTo(null));
    }

    @Test
    void getBean_validNamespace_highestPrecedenceBeanInNamespace()
    {
        container = new TypeSafeConfigurationContainer<>(CONF_NS1_BEAN_1, CONF_NS1_BEAN_2, CONF_NS2_BEAN_1);
        assertThat(container.getBean(NAMESPACE1), equalTo(BEAN_1_2));
    }

    @Test
    void clear_presetConfigurations_empty()
    {
        container = new TypeSafeConfigurationContainer<>(CONF_NS1_BEAN_1, CONF_NS2_BEAN_1);
        assertThat(container.size(), equalTo(2L));
        container.clear();
        assertThat(container.size(), equalTo(0L));
    }

    @Test
    void size_severalScenarios_validValues()
    {
        container = new TypeSafeConfigurationContainer<>();
        assertThat(container.size(), equalTo(0L));
        assertThat(container.size(null), equalTo(0L)); // null maps to the default namespace
        assertThat(container.size(NAMESPACE1), equalTo(0L));
        assertThat(container.size(NAMESPACE2), equalTo(0L));

        container.add(CONF_NS1_BEAN_1);
        assertThat(container.size(), equalTo(1L));
        assertThat(container.size(null), equalTo(0L));
        assertThat(container.size(NAMESPACE1), equalTo(1L));
        assertThat(container.size(NAMESPACE2), equalTo(0L));

        container.add(CONF_NS1_BEAN_2);
        assertThat(container.size(), equalTo(2L));
        assertThat(container.size(null), equalTo(0L));
        assertThat(container.size(NAMESPACE1), equalTo(2L));
        assertThat(container.size(NAMESPACE2), equalTo(0L));

        container.add(CONF_NS2_BEAN_1);
        assertThat(container.size(), equalTo(3L));
        assertThat(container.size(null), equalTo(0L));
        assertThat(container.size(NAMESPACE1), equalTo(2L));
        assertThat(container.size(NAMESPACE2), equalTo(1L));

        container.add(CONF_BEAN_3); // no namespace defined
        assertThat(container.size(), equalTo(4L));
        assertThat(container.size(null), equalTo(1L));
        assertThat(container.size(NAMESPACE1), equalTo(2L));
        assertThat(container.size(NAMESPACE2), equalTo(1L));
    }

    @Test
    void isEmpty_severalScenarios_validValues()
    {
        container = new TypeSafeConfigurationContainer<String>();
        assertThat(container.isEmpty(), equalTo(true));

        container.add(CONF_BEAN_3);
        assertThat(container.isEmpty(), equalTo(false));
    }

}
