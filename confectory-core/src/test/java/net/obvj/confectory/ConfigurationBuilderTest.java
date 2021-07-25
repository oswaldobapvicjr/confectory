package net.obvj.confectory;

import static net.obvj.junit.utils.matchers.AdvancedMatchers.throwsException;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import net.obvj.confectory.helper.ConfigurationHelper;
import net.obvj.confectory.helper.provider.NullValueProvider;
import net.obvj.confectory.helper.provider.StandardNullValueProvider;
import net.obvj.confectory.mapper.Mapper;
import net.obvj.confectory.source.AbstractSource;
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
    @Mock
    private NullValueProvider nullValueProvider;

    private void assertConfigurationMetadata(ConfigurationMetadataRetriever<Object> configuration, Source<?> source,
            Mapper<?> mapper, String namespace, int precedence, boolean optional, NullValueProvider nullValueProvider)
    {
        assertThat(configuration.getSource(), equalTo(source));
        assertThat(configuration.getMapper(), equalTo(mapper));
        assertThat(configuration.getNamespace(), equalTo(namespace));
        assertThat(configuration.getPrecedence(), equalTo(precedence));
        assertThat(configuration.isOptional(), equalTo(optional));
        assertThat(configuration.getNullValueProvider(), equalTo(nullValueProvider));
    }

    @Test
    void constructor_noArgument_emptyBuilder()
    {
        ConfigurationBuilder<Object> builder = new ConfigurationBuilder<>();
        assertConfigurationMetadata(builder, null, null, null, 0, false, null);
    }

    @Test
    void constructor_null_emptyBuilder()
    {
        ConfigurationBuilder<Object> builder = new ConfigurationBuilder<>(null);
        assertConfigurationMetadata(builder, null, null, null, 0, false, null);
    }

    @Test
    void constructor_exisitingConfiguration_presetBuilder()
    {
        when(configuration.getSource()).thenReturn(source);
        when(configuration.getMapper()).thenReturn(mapper);
        when(configuration.getNamespace()).thenReturn(NAMESPACE1);
        when(configuration.getPrecedence()).thenReturn(999);
        when(configuration.isOptional()).thenReturn(true);
        when(configuration.getNullValueProvider()).thenReturn(nullValueProvider);

        ConfigurationBuilder<Object> builder = new ConfigurationBuilder<>(configuration);
        assertConfigurationMetadata(builder, source, mapper, NAMESPACE1, 999, true, nullValueProvider);
    }

    @Test
    void build_nullSource_nullPointerException()
    {
        ConfigurationBuilder<Object> builder = new ConfigurationBuilder<>();
        assertThat(() -> builder.build(),
                throwsException(NullPointerException.class).withMessage("The configuration source must not be null"));
    }

    @Test
    void build_nullMapper_nullPointerException()
    {
        ConfigurationBuilder<Object> builder = new ConfigurationBuilder<>()
                .source(source);
        assertThat(() -> builder.build(),
                throwsException(NullPointerException.class).withMessage("The configuration mapper must not be null"));
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

        assertConfigurationMetadata(newConfiguration, source, mapper, "", 0, false,
                StandardNullValueProvider.instance());
        assertThat(newConfiguration.getBean().get(), equalTo(OBJECT1));
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
                .nullValueProvider(nullValueProvider);

        Configuration<Object> newConfiguration = builder.build();

        assertConfigurationMetadata(newConfiguration, source, mapper, NAMESPACE1, 777, true, nullValueProvider);
        assertThat(newConfiguration.getBean().get(), equalTo(OBJECT1));
    }

    @Test
    void required_existingOptionalConfiguration_required()
    {
        when(configuration.isOptional()).thenReturn(true);
        ConfigurationBuilder<Object> builder = new ConfigurationBuilder<>(configuration).required();
        assertFalse(builder.isOptional());
    }
}
