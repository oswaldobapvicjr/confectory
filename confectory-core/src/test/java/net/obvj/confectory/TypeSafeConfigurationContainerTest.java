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
        ConfigurationContainer container = new ConfigurationContainer();
        assertThat(container.isEmpty(), equalTo(true));

        container.add(CONF_BEAN_3);
        assertThat(container.isEmpty(), equalTo(false));
    }

}
