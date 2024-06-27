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

import static net.obvj.junit.utils.matchers.AdvancedMatchers.containsAll;
import static net.obvj.junit.utils.matchers.AdvancedMatchers.throwsException;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import net.obvj.confectory.internal.helper.ConfigurationHelper;
import net.obvj.confectory.mapper.DynamicMapper;
import net.obvj.confectory.mapper.Mapper;
import net.obvj.confectory.mapper.PropertiesMapper;
import net.obvj.confectory.source.AbstractSource;
import net.obvj.confectory.source.DynamicSource;
import net.obvj.confectory.source.Source;

/**
 * Unit tests for the {@link ConfigurationBuilder}.
 *
 * @author oswaldo.bapvic.jr (Oswaldo Junior)
 * @since 0.1.0
 */
@ExtendWith(MockitoExtension.class)
class ConfigurationBuilderTest
{
    private static final String NAMESPACE1 = "namespace1";
    private static final Object OBJECT1 = new Object();

    @Mock
    private Configuration<Object> configuration;

    private Source<Object> source = mock(AbstractSource.class, Answers.CALLS_REAL_METHODS);
    @Mock
    private Mapper<Object> mapper;
    @Mock
    private ConfigurationHelper<Object> helper;

    private void assertConfigurationMetadata(ConfigurationMetadataRetriever<Object> configuration, Source<?> source,
            Mapper<?> mapper, String namespace, int precedence, boolean optional, boolean lazy)
    {
        assertThat(configuration.getSource(), equalTo(source));
        assertThat(configuration.getMapper(), equalTo(mapper));
        assertThat(configuration.getNamespace(), equalTo(namespace));
        assertThat(configuration.getPrecedence(), equalTo(precedence));
        assertThat(configuration.isOptional(), equalTo(optional));
        assertThat(configuration.isLazy(), equalTo(lazy));
    }

    @Test
    void constructor_noArgument_emptyBuilder()
    {
        ConfigurationBuilder<Object> builder = new ConfigurationBuilder<>();
        assertConfigurationMetadata(builder, null, null, null, 0, false, false);
    }

    @Test
    void constructor_null_emptyBuilder()
    {
        ConfigurationBuilder<Object> builder = new ConfigurationBuilder<>(null);
        assertConfigurationMetadata(builder, null, null, null, 0, false, false);
    }

    @Test
    void constructor_exisitingConfiguration_presetBuilder()
    {
        when(configuration.getSource()).thenReturn(source);
        when(configuration.getMapper()).thenReturn(mapper);
        when(configuration.getNamespace()).thenReturn(NAMESPACE1);
        when(configuration.getPrecedence()).thenReturn(999);
        when(configuration.isOptional()).thenReturn(true);
        when(configuration.isLazy()).thenReturn(true);

        ConfigurationBuilder<Object> builder = new ConfigurationBuilder<>(configuration);
        assertConfigurationMetadata(builder, source, mapper, NAMESPACE1, 999, true, true);
    }

    @Test
    void build_nullSource_illegalStateException()
    {
        ConfigurationBuilder<Object> builder = new ConfigurationBuilder<>();
        assertThat(() -> builder.build(),
                throwsException(IllegalStateException.class).withMessage("The configuration source must not be null"));
    }

    @Test
    void build_nullMapperAndCouldNotBeInferred_illegalStateException()
    {
        ConfigurationBuilder<Object> builder = new ConfigurationBuilder<>()
                .source(source);
        assertThat(() -> builder.build(),
                throwsException(IllegalStateException.class).withMessage("The mapper could not be inferred. Please specify a concrete mapper."));
    }

    @Test
    void build_nullMapperButCouldBeInferred_dynamicMapperAssignedBasedOnExtension()
    {
        ConfigurationBuilder<Object> builder = new ConfigurationBuilder<>()
                .source("testfiles/my.properties").lazy();
        Configuration<?> config = builder.build();

        DynamicMapper assignedMapper = (DynamicMapper) config.getMapper();
        assertThat(assignedMapper.getActualMapper().getClass(), is(PropertiesMapper.class));
    }

    @Test
    void build_allMandatoryParametersSet_success()
    {
        when(source.load(mapper)).thenReturn(OBJECT1);
        when(mapper.configurationHelper(OBJECT1)).thenReturn(helper);

        ConfigurationBuilder<Object> builder = new ConfigurationBuilder<>()
                .source(source)
                .mapper(mapper);

        Configuration<Object> newConfiguration = builder.build();

        assertConfigurationMetadata(newConfiguration, source, mapper, "", 0, false, false);
        assertThat(newConfiguration.getBean(), equalTo(OBJECT1));
    }

    @Test
    void build_allParametersSet_success()
    {
        when(source.load(mapper)).thenReturn(OBJECT1);
        when(mapper.configurationHelper(OBJECT1)).thenReturn(helper);

        ConfigurationBuilder<Object> builder = new ConfigurationBuilder<>()
                .source(source)
                .mapper(mapper)
                .namespace(NAMESPACE1)
                .precedence(777)
                .optional()
                .lazy();

        Configuration<Object> newConfiguration = builder.build();

        assertConfigurationMetadata(newConfiguration, source, mapper, NAMESPACE1, 777, true, true);
        assertThat(newConfiguration.getBean(), equalTo(OBJECT1));
    }

    @Test
    void required_existingOptionalConfiguration_notOptional()
    {
        when(configuration.isOptional()).thenReturn(true);
        ConfigurationBuilder<Object> builder = new ConfigurationBuilder<>(configuration).required();
        assertFalse(builder.isOptional());
    }

    @Test
    void eager_existingOptionalConfiguration_notLazy()
    {
        when(configuration.isLazy()).thenReturn(true);
        ConfigurationBuilder<Object> builder = new ConfigurationBuilder<>(configuration).eager();
        assertFalse(builder.isLazy());
    }

    @Test
    void source_string_dynamicSource()
    {
        ConfigurationBuilder<Object> builder = new ConfigurationBuilder<>().source("path1");
        Source<Object> source = builder.getSource();
        assertThat(source.getClass(), equalTo(DynamicSource.class));
        assertThat(source.toString(), containsAll("DynamicSource", "path1"));
    }
}
