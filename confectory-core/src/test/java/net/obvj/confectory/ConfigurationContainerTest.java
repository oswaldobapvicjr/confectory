package net.obvj.confectory;

import static org.hamcrest.CoreMatchers.either;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import net.obvj.confectory.mapper.PropertiesMapper;
import net.obvj.confectory.settings.ConfectorySettings;
import net.obvj.confectory.source.StringSource;

/**
 * Unit tests for the {@link ConfigurationContainer} class;
 *
 * @author oswaldo.bapvic.jr (Oswaldo Junior)
 * @since 0.1.0
 */
class ConfigurationContainerTest
{
    private static final String NAMESPACE1 = "namespace1";
    private static final String NAMESPACE2 = "namespace2";

    private static final String KEY_TEST = "test";
    private static final String KEY_BAD = "bad";
    private static final String KEY_STRING = "string";
    private static final String KEY_BOOLEAN = "boolean";
    private static final String KEY_INT = "int";
    private static final String KEY_LONG = "long";
    private static final String KEY_DOUBLE = "double";

    private static final Configuration<Properties> CONF_NS1_PROPERTIES_1 = Configuration.<Properties>builder()
            .namespace(NAMESPACE1)
            .precedence(11)
            .mapper(new PropertiesMapper())
            .source(new StringSource<>(join("string=string1", "int=1", "long=111", "boolean=false")))
            .build();

    private static final Configuration<Properties> CONF_NS1_PROPERTIES_2 = Configuration.<Properties>builder()
            .namespace(NAMESPACE1)
            .precedence(22)
            .mapper(new PropertiesMapper())
            .source(new StringSource<>(join("string=string2", "int=2", "double=2.2", "boolean=true")))
            .build();

    private static final Configuration<Properties> CONF_NS2_PROPERTIES_1 = Configuration.<Properties>builder()
            .namespace(NAMESPACE2)
            .precedence(1)
            .mapper(new PropertiesMapper())
            .source(new StringSource<>(join("test=ok21")))
            .build();

    // Configuration without namespace
    private static final Configuration<Properties> CONF_PROPERTIES_1 = Configuration.<Properties>builder()
            .precedence(0)
            .mapper(new PropertiesMapper())
            .source(new StringSource<>(join("test=ok01", "int=10", "double=10.1", "boolean=true", "long=1010")))
            .build();

    private ConfigurationContainer container;

    private static String join(String... lines)
    {
        return StringUtils.join(lines, "\n");
    }

    private void assertDefaultDataFetchStrategy()
    {
        assertThat(container.getDataFetchStrategy(),
                equalTo(ConfectorySettings.getInstance().getDefaultDataFetchStrategy()));
    }

    @Test
    void constructor_empty_default()
    {
        container = new ConfigurationContainer();
        assertThat(container.getNamespaces().size(), equalTo(0));
        assertDefaultDataFetchStrategy();
    }

    @Test
    void constructor_customDataFetchStrategy_default()
    {
        DataFetchStrategy dataFetchStrategy = Mockito.mock(DataFetchStrategy.class);
        container = new ConfigurationContainer(dataFetchStrategy);
        assertThat(container.getNamespaces().size(), equalTo(0));
        assertThat(container.getDataFetchStrategy(), equalTo(dataFetchStrategy));
    }

    @Test
    void constructor_oneConfiguration_oneNamespace()
    {
        container = new ConfigurationContainer(CONF_NS1_PROPERTIES_1);
        assertThat(container.getNamespaces().size(), equalTo(1));
        assertThat(container.size(NAMESPACE1), equalTo(1L));
        assertDefaultDataFetchStrategy();
    }

    @Test
    void constructor_twoConfigurationsWithSameNamespace_oneNamespaceWithTwoConfigs()
    {
        container = new ConfigurationContainer(CONF_NS1_PROPERTIES_1, CONF_NS1_PROPERTIES_2);
        assertThat(container.getNamespaces().size(), equalTo(1));
        assertThat(container.size(NAMESPACE1), equalTo(2L));
        assertDefaultDataFetchStrategy();
    }

    @Test
    void constructor_twoConfigurationsWithDifferentNamespaces_twoNamespaces()
    {
        container = new ConfigurationContainer(CONF_NS1_PROPERTIES_1, CONF_NS2_PROPERTIES_1);
        assertThat(container.getNamespaces().size(), equalTo(2));
        assertThat(container.size(NAMESPACE1), equalTo(1L));
        assertThat(container.size(NAMESPACE2), equalTo(1L));
    }

    @Test
    void clear_presetConfigurations_empty()
    {
        container = new ConfigurationContainer(CONF_NS1_PROPERTIES_1, CONF_NS2_PROPERTIES_1);
        assertThat(container.getNamespaces().size(), equalTo(2));
        container.clear();
        assertThat(container.getNamespaces().size(), equalTo(0));
    }

    @Test
    void getString_keyOnlyAndStrict_configurationWithoutNamespace()
    {
        container = new ConfigurationContainer(CONF_NS1_PROPERTIES_1, CONF_NS2_PROPERTIES_1, CONF_PROPERTIES_1);
        assertThat(container.getString(KEY_TEST), equalTo("ok01")); // CONF_PROPERTIES1 (strict)
        assertThat(container.getString(KEY_STRING), equalTo(null)); // not found (strict)
    }

    @Test
    void getString_keyOnlyAndLenient_configurationWithoutNamespace()
    {
        container = new ConfigurationContainer(DataFetchStrategy.LENIENT, CONF_NS1_PROPERTIES_1, CONF_NS1_PROPERTIES_2, CONF_NS2_PROPERTIES_1, CONF_PROPERTIES_1);
        assertThat(container.getString(KEY_TEST), equalTo("ok21")); // CONF_NS2_PROPERTIES_1 (lenient)
        assertThat(container.getString(KEY_STRING), equalTo("string2")); // CONF_NS1_PROPERTIES_2 (lenient)
    }

    @Test
    void getString_keyOnlyAndLenientUnsorted_anyOfTheConfigurationsWithoutNamespace()
    {
        container = new ConfigurationContainer(DataFetchStrategy.LENIENT_UNSORTED, CONF_NS1_PROPERTIES_1, CONF_NS1_PROPERTIES_2,
                CONF_NS2_PROPERTIES_1, CONF_PROPERTIES_1);
        assertThat(container.getString(KEY_TEST), equalTo("ok01")); // CONF_NS2_PROPERTIES_1 (lenient)
        assertThat(container.getString(KEY_STRING), either(equalTo("string1")).or(equalTo("string2")));
    }

    @Test
    void getString_namespaceAndKeyStrict_highestPrecedenceConfiguration()
    {
        container = new ConfigurationContainer(CONF_NS1_PROPERTIES_1, CONF_NS1_PROPERTIES_2, CONF_NS2_PROPERTIES_1);
        assertThat(container.getString(NAMESPACE1, KEY_STRING), equalTo("string2")); // CONF_NS1_PROPERTIES_2
    }

    @Test
    void getString_namespaceAndKeyLenient_sameAsStrict()
    {
        container = new ConfigurationContainer(DataFetchStrategy.LENIENT, CONF_NS1_PROPERTIES_1, CONF_NS1_PROPERTIES_2, CONF_NS2_PROPERTIES_1);
        assertThat(container.getString(NAMESPACE1, KEY_STRING), equalTo("string2")); // CONF_NS1_PROPERTIES_2
    }

    @Test
    void getString_namespaceAndKeyLenientUnsorted_sameAsStrictUnsorted()
    {
        container = new ConfigurationContainer(DataFetchStrategy.LENIENT_UNSORTED, CONF_NS1_PROPERTIES_1, CONF_NS1_PROPERTIES_2,
                CONF_NS2_PROPERTIES_1);
        assertThat(container.getString(NAMESPACE1, KEY_STRING), either(equalTo("string1")).or(equalTo("string2")));
    }

    @Test
    void getString_namespaceAndKey_nullIfNotFound()
    {
        container = new ConfigurationContainer();
        assertThat(container.getString(NAMESPACE1, KEY_BAD), equalTo(null));
    }

    @Test
    void getInteger_keyOnly_configurationWithoutNamespace()
    {
        container = new ConfigurationContainer(CONF_NS1_PROPERTIES_1, CONF_NS2_PROPERTIES_1, CONF_PROPERTIES_1);
        assertThat(container.getInteger(KEY_INT), equalTo(10)); // CONF_PROPERTIES1
    }

    @Test
    void getInteger_namespaceAndKey_highestPrecedenceConfiguration()
    {
        container = new ConfigurationContainer(CONF_NS1_PROPERTIES_1, CONF_NS1_PROPERTIES_2, CONF_NS2_PROPERTIES_1);
        assertThat(container.getInteger(NAMESPACE1, KEY_INT), equalTo(2)); // CONF_NS1_PROPERTIES_2
    }

    @Test
    void getInteger_namespaceAndKey_nullIfNotFound()
    {
        container = new ConfigurationContainer();
        assertThat(container.getInteger(NAMESPACE1, KEY_BAD), equalTo(null));
    }

    @Test
    void getBoolean_keyOnly_configurationWithoutNamespace()
    {
        container = new ConfigurationContainer(CONF_NS1_PROPERTIES_1, CONF_NS2_PROPERTIES_1, CONF_PROPERTIES_1);
        assertThat(container.getBoolean(KEY_BOOLEAN), equalTo(true)); // CONF_PROPERTIES1
    }

    @Test
    void getBoolean_namespaceAndKey_highestPrecedenceConfiguration()
    {
        container = new ConfigurationContainer(CONF_NS1_PROPERTIES_1, CONF_NS1_PROPERTIES_2, CONF_NS2_PROPERTIES_1);
        assertThat(container.getBoolean(NAMESPACE1, KEY_BOOLEAN), equalTo(true)); // CONF_NS1_PROPERTIES_2
    }

    @Test
    void getBoolean_namespaceAndKey_nullIfNotFound()
    {
        container = new ConfigurationContainer();
        assertThat(container.getBoolean(NAMESPACE1, KEY_BAD), equalTo(null));
    }

    @Test
    void getDouble_keyOnly_configurationWithoutNamespace()
    {
        container = new ConfigurationContainer(CONF_NS1_PROPERTIES_1, CONF_NS2_PROPERTIES_1, CONF_PROPERTIES_1);
        assertThat(container.getDouble(KEY_DOUBLE), equalTo(10.1)); // CONF_PROPERTIES1
    }

    @Test
    void getDouble_namespaceAndKey_highestPrecedenceConfiguration()
    {
        container = new ConfigurationContainer(CONF_NS1_PROPERTIES_1, CONF_NS1_PROPERTIES_2, CONF_NS2_PROPERTIES_1);
        assertThat(container.getDouble(NAMESPACE1, KEY_DOUBLE), equalTo(2.2)); // CONF_NS1_PROPERTIES_2
    }

    @Test
    void getDouble_namespaceAndKey_nullIfNotFound()
    {
        container = new ConfigurationContainer();
        assertThat(container.getDouble(NAMESPACE1, KEY_BAD), equalTo(null));
    }

    @Test
    void getLong_keyOnly_configurationWithoutNamespace()
    {
        container = new ConfigurationContainer(CONF_NS1_PROPERTIES_1, CONF_NS2_PROPERTIES_1, CONF_PROPERTIES_1);
        assertThat(container.getLong(KEY_LONG), equalTo(1010L)); // CONF_PROPERTIES1
    }

    @Test
    void getLong_namespaceAndKey_highestPrecedenceConfiguration()
    {
        container = new ConfigurationContainer(CONF_NS1_PROPERTIES_1, CONF_NS1_PROPERTIES_2, CONF_NS2_PROPERTIES_1);
        assertThat(container.getLong(NAMESPACE1, KEY_LONG), equalTo(111L)); // CONF_NS1_PROPERTIES_1
    }

    @Test
    void getLong_namespaceAndKey_nullIfNotFound()
    {
        container = new ConfigurationContainer();
        assertThat(container.getLong(NAMESPACE1, KEY_BAD), equalTo(null));
    }

    @Test
    void addAll_validSourceOnEmptyContainer_allConfigurationCopied()
    {
        ConfigurationContainer source = new ConfigurationContainer(CONF_NS1_PROPERTIES_1, CONF_NS1_PROPERTIES_2, CONF_NS2_PROPERTIES_1);

        container = new ConfigurationContainer();
        container.addAll(source);

        assertThat(container.size(NAMESPACE1), equalTo(2L));
        assertThat(container.size(NAMESPACE2), equalTo(1L));
    }

    @Test
    void addAll_nullSourceOnEmptyContainer_noCopyAndNoException()
    {
        ConfigurationContainer source = null;

        container = new ConfigurationContainer();
        container.addAll(source);

        assertTrue(container.getNamespaces().isEmpty());
    }

    @Test
    void addAll_calledTwice_noDuplicateConfigurations()
    {
        ConfigurationContainer source1 = new ConfigurationContainer(CONF_NS1_PROPERTIES_1, CONF_NS1_PROPERTIES_2, CONF_NS2_PROPERTIES_1);

        container = new ConfigurationContainer();
        container.addAll(source1);

        assertThat(container.size(NAMESPACE1), equalTo(2L));
        assertThat(container.size(NAMESPACE2), equalTo(1L));

        ConfigurationContainer source2 = new ConfigurationContainer(CONF_NS1_PROPERTIES_1, CONF_NS1_PROPERTIES_2, CONF_NS2_PROPERTIES_1);

        container.addAll(source2);

        assertThat(container.size(NAMESPACE1), equalTo(2L)); // No change expected
        assertThat(container.size(NAMESPACE2), equalTo(1L)); // No change expected
    }

    @Test
    void size_severalScenarios_validValues()
    {
        ConfigurationContainer container = new ConfigurationContainer();
        assertThat(container.size(),           equalTo(0L));
        assertThat(container.size(null), equalTo(0L)); // null maps to the default namespace
        assertThat(container.size(NAMESPACE1), equalTo(0L));
        assertThat(container.size(NAMESPACE2), equalTo(0L));

        container.add(CONF_NS1_PROPERTIES_1);
        assertThat(container.size(),           equalTo(1L));
        assertThat(container.size(null),       equalTo(0L));
        assertThat(container.size(NAMESPACE1), equalTo(1L));
        assertThat(container.size(NAMESPACE2), equalTo(0L));

        container.add(CONF_NS1_PROPERTIES_2);
        assertThat(container.size(),           equalTo(2L));
        assertThat(container.size(null),       equalTo(0L));
        assertThat(container.size(NAMESPACE1), equalTo(2L));
        assertThat(container.size(NAMESPACE2), equalTo(0L));

        container.add(CONF_NS2_PROPERTIES_1);
        assertThat(container.size(),           equalTo(3L));
        assertThat(container.size(null),       equalTo(0L));
        assertThat(container.size(NAMESPACE1), equalTo(2L));
        assertThat(container.size(NAMESPACE2), equalTo(1L));

        container.add(CONF_PROPERTIES_1); // no namespace defined
        assertThat(container.size(),           equalTo(4L));
        assertThat(container.size(null),       equalTo(1L));
        assertThat(container.size(NAMESPACE1), equalTo(2L));
        assertThat(container.size(NAMESPACE2), equalTo(1L));
    }

    @Test
    void isEmpty_severalScenarios_validValues()
    {
        ConfigurationContainer container = new ConfigurationContainer();
        assertThat(container.isEmpty(), equalTo(true));

        container.add(CONF_PROPERTIES_1);
        assertThat(container.isEmpty(), equalTo(false));
    }

}
