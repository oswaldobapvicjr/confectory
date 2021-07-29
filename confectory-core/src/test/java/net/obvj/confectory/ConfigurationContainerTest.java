package net.obvj.confectory;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

import net.obvj.confectory.helper.provider.AbstractNullValueProvider;
import net.obvj.confectory.helper.provider.NullValueProvider;
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
            .precedence(0)
            .mapper(new PropertiesMapper())
            .source(new StringSource<>(join("test=ok21")))
            .build();

    // Configuration without namespace
    private static final Configuration<Properties> CONF_PROPERTIES_1 = Configuration.<Properties>builder()
            .precedence(0)
            .mapper(new PropertiesMapper())
            .source(new StringSource<>(join("test=ok01", "int=10", "double=10.1", "boolean=true", "long=1010")))
            .build();

    private static final NullValueProvider CUSTOM_NVP = new AbstractNullValueProvider()
    {
        @Override
        public String getStringValue()
        {
            return "null";
        }

        @Override
        public long getLongValue()
        {
            return Long.MAX_VALUE;
        }

        @Override
        public int getIntValue()
        {
            return Integer.MAX_VALUE;
        }

        @Override
        public double getDoubleValue()
        {
            return Double.MAX_VALUE;
        }

        @Override
        public boolean getBooleanValue()
        {
            return true;
        }
    };

    private ConfigurationContainer container;

    private static String join(String... lines)
    {
        return StringUtils.join(lines, "\n");
    }

    @Test
    void constructor_empty_default()
    {
        container = new ConfigurationContainer();
        assertThat(container.getNamespaces().size(), equalTo(0));
        assertThat(container.getNullValueProvider(),
                equalTo(ConfectorySettings.getInstance().getDefaultNullValueProvider()));
    }

    @Test
    void constructor_customNVP_default()
    {
        container = new ConfigurationContainer(CUSTOM_NVP);
        assertThat(container.getNamespaces().size(), equalTo(0));
        assertThat(container.getNullValueProvider(), equalTo(CUSTOM_NVP));
    }

    @Test
    void constructor_oneConfiguration_oneNamespace()
    {
        container = new ConfigurationContainer(CONF_NS1_PROPERTIES_1);
        assertThat(container.getNamespaces().size(), equalTo(1));
        assertThat(container.size(NAMESPACE1), equalTo(1));
        assertThat(container.getNullValueProvider(),
                equalTo(ConfectorySettings.getInstance().getDefaultNullValueProvider()));
    }

    @Test
    void constructor_twoConfigurationsWithSameNamespace_oneNamespaceWithTwoConfigs()
    {
        container = new ConfigurationContainer(CONF_NS1_PROPERTIES_1, CONF_NS1_PROPERTIES_2);
        assertThat(container.getNamespaces().size(), equalTo(1));
        assertThat(container.size(NAMESPACE1), equalTo(2));
        assertThat(container.getNullValueProvider(),
                equalTo(ConfectorySettings.getInstance().getDefaultNullValueProvider()));
    }

    @Test
    void constructor_twoConfigurationsWithDifferentNamespaceAndCustomNVP_twoNamespaces()
    {
        container = new ConfigurationContainer(CUSTOM_NVP, CONF_NS1_PROPERTIES_1, CONF_NS2_PROPERTIES_1);
        assertThat(container.getNamespaces().size(), equalTo(2));
        assertThat(container.size(NAMESPACE1), equalTo(1));
        assertThat(container.size(NAMESPACE2), equalTo(1));
        assertThat(container.getNullValueProvider(), equalTo(CUSTOM_NVP));
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
    void getStringProperty_keyOnly_configurationWithoutNamespace()
    {
        container = new ConfigurationContainer(CONF_NS1_PROPERTIES_1, CONF_NS2_PROPERTIES_1, CONF_PROPERTIES_1);
        assertThat(container.getStringProperty(KEY_TEST), equalTo("ok01")); // CONF_PROPERTIES1
    }

    @Test
    void getStringProperty_namespaceAndKey_highestPrecedenceConfiguration()
    {
        container = new ConfigurationContainer(CONF_NS1_PROPERTIES_1, CONF_NS1_PROPERTIES_2, CONF_NS2_PROPERTIES_1);
        assertThat(container.getStringProperty(NAMESPACE1, KEY_STRING), equalTo("string2")); // CONF_NS1_PROPERTIES_2
    }

    @Test
    void getStringProperty_namespaceAndKey_defaultNullValueIfNotFound()
    {
        container = new ConfigurationContainer(CUSTOM_NVP);
        assertThat(container.getStringProperty(NAMESPACE1, KEY_BAD), equalTo(CUSTOM_NVP.getStringValue())); // CUSTOM_NVP
    }

    @Test
    void getIntProperty_keyOnly_configurationWithoutNamespace()
    {
        container = new ConfigurationContainer(CONF_NS1_PROPERTIES_1, CONF_NS2_PROPERTIES_1, CONF_PROPERTIES_1);
        assertThat(container.getIntProperty(KEY_INT), equalTo(10)); // CONF_PROPERTIES1
    }

    @Test
    void getIntProperty_namespaceAndKey_highestPrecedenceConfiguration()
    {
        container = new ConfigurationContainer(CONF_NS1_PROPERTIES_1, CONF_NS1_PROPERTIES_2, CONF_NS2_PROPERTIES_1);
        assertThat(container.getIntProperty(NAMESPACE1, KEY_INT), equalTo(2)); // CONF_NS1_PROPERTIES_2
    }

    @Test
    void getIntProperty_namespaceAndKey_defaultNullValueIfNotFound()
    {
        container = new ConfigurationContainer(CUSTOM_NVP);
        assertThat(container.getIntProperty(NAMESPACE1, KEY_BAD), equalTo(CUSTOM_NVP.getIntValue())); // CUSTOM_NVP
    }

    @Test
    void getBooleanProperty_keyOnly_configurationWithoutNamespace()
    {
        container = new ConfigurationContainer(CONF_NS1_PROPERTIES_1, CONF_NS2_PROPERTIES_1, CONF_PROPERTIES_1);
        assertThat(container.getBooleanProperty(KEY_BOOLEAN), equalTo(true)); // CONF_PROPERTIES1
    }

    @Test
    void getBooleanProperty_namespaceAndKey_highestPrecedenceConfiguration()
    {
        container = new ConfigurationContainer(CONF_NS1_PROPERTIES_1, CONF_NS1_PROPERTIES_2, CONF_NS2_PROPERTIES_1);
        assertThat(container.getBooleanProperty(NAMESPACE1, KEY_BOOLEAN), equalTo(true)); // CONF_NS1_PROPERTIES_2
    }

    @Test
    void getBooleanProperty_namespaceAndKey_defaultNullValueIfNotFound()
    {
        container = new ConfigurationContainer(CUSTOM_NVP);
        assertThat(container.getBooleanProperty(NAMESPACE1, KEY_BAD), equalTo(CUSTOM_NVP.getBooleanValue())); // CUSTOM_NVP
    }

    @Test
    void getDoubleProperty_keyOnly_configurationWithoutNamespace()
    {
        container = new ConfigurationContainer(CONF_NS1_PROPERTIES_1, CONF_NS2_PROPERTIES_1, CONF_PROPERTIES_1);
        assertThat(container.getDoubleProperty(KEY_DOUBLE), equalTo(10.1)); // CONF_PROPERTIES1
    }

    @Test
    void getDoubleProperty_namespaceAndKey_highestPrecedenceConfiguration()
    {
        container = new ConfigurationContainer(CONF_NS1_PROPERTIES_1, CONF_NS1_PROPERTIES_2, CONF_NS2_PROPERTIES_1);
        assertThat(container.getDoubleProperty(NAMESPACE1, KEY_DOUBLE), equalTo(2.2)); // CONF_NS1_PROPERTIES_2
    }

    @Test
    void getDoubleProperty_namespaceAndKey_defaultNullValueIfNotFound()
    {
        container = new ConfigurationContainer(CUSTOM_NVP);
        assertThat(container.getDoubleProperty(NAMESPACE1, KEY_BAD), equalTo(CUSTOM_NVP.getDoubleValue())); // CUSTOM_NVP
    }

    @Test
    void getLongProperty_keyOnly_configurationWithoutNamespace()
    {
        container = new ConfigurationContainer(CONF_NS1_PROPERTIES_1, CONF_NS2_PROPERTIES_1, CONF_PROPERTIES_1);
        assertThat(container.getLongProperty(KEY_LONG), equalTo(1010L)); // CONF_PROPERTIES1
    }

    @Test
    void getLongProperty_namespaceAndKey_highestPrecedenceConfiguration()
    {
        container = new ConfigurationContainer(CONF_NS1_PROPERTIES_1, CONF_NS1_PROPERTIES_2, CONF_NS2_PROPERTIES_1);
        assertThat(container.getLongProperty(NAMESPACE1, KEY_LONG), equalTo(111L)); // CONF_NS1_PROPERTIES_1
    }

    @Test
    void getLongProperty_namespaceAndKey_defaultNullValueIfNotFound()
    {
        container = new ConfigurationContainer(CUSTOM_NVP);
        assertThat(container.getLongProperty(NAMESPACE1, KEY_BAD), equalTo(CUSTOM_NVP.getLongValue())); // CUSTOM_NVP
    }

}
