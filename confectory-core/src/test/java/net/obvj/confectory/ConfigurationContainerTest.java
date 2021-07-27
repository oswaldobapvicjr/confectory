package net.obvj.confectory;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

import net.obvj.confectory.config.ConfectoryConfiguration;
import net.obvj.confectory.helper.provider.AbstractNullValueProvider;
import net.obvj.confectory.helper.provider.NullValueProvider;
import net.obvj.confectory.mapper.PropertiesMapper;
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
            .source(new StringSource<>(join("test=ok")))
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
                equalTo(ConfectoryConfiguration.getInstance().getDefaultNullValueProvider()));
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
                equalTo(ConfectoryConfiguration.getInstance().getDefaultNullValueProvider()));
    }

    @Test
    void constructor_twoConfigurationsWithSameNamespace_oneNamespaceWithTwoConfigs()
    {
        container = new ConfigurationContainer(CONF_NS1_PROPERTIES_1, CONF_NS1_PROPERTIES_2);
        assertThat(container.getNamespaces().size(), equalTo(1));
        assertThat(container.size(NAMESPACE1), equalTo(2));
        assertThat(container.getNullValueProvider(),
                equalTo(ConfectoryConfiguration.getInstance().getDefaultNullValueProvider()));
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
    void getStringProperty_namespaceAndKey_highestPrecedenceConfiguration()
    {
        container = new ConfigurationContainer(CONF_NS1_PROPERTIES_1, CONF_NS1_PROPERTIES_2, CONF_NS2_PROPERTIES_1);
        assertThat(container.getStringProperty(NAMESPACE1, "string"), equalTo("string2")); // CONF_NS1_PROPERTIES_2
    }

    @Test
    void getIntProperty_namespaceAndKey_highestPrecedenceConfiguration()
    {
        container = new ConfigurationContainer(CONF_NS1_PROPERTIES_1, CONF_NS1_PROPERTIES_2, CONF_NS2_PROPERTIES_1);
        assertThat(container.getIntProperty(NAMESPACE1, "int"), equalTo(2)); // CONF_NS1_PROPERTIES_2
    }

    @Test
    void getBooleanProperty_namespaceAndKey_highestPrecedenceConfiguration()
    {
        container = new ConfigurationContainer(CONF_NS1_PROPERTIES_1, CONF_NS1_PROPERTIES_2, CONF_NS2_PROPERTIES_1);
        assertThat(container.getBooleanProperty(NAMESPACE1, "boolean"), equalTo(true)); // CONF_NS1_PROPERTIES_2
    }

    @Test
    void getDoubleProperty_namespaceAndKey_highestPrecedenceConfiguration()
    {
        container = new ConfigurationContainer(CONF_NS1_PROPERTIES_1, CONF_NS1_PROPERTIES_2, CONF_NS2_PROPERTIES_1);
        assertThat(container.getDoubleProperty(NAMESPACE1, "double"), equalTo(2.2)); // CONF_NS1_PROPERTIES_2
    }

    @Test
    void getLongProperty_namespaceAndKey_highestPrecedenceConfiguration()
    {
        container = new ConfigurationContainer(CONF_NS1_PROPERTIES_1, CONF_NS1_PROPERTIES_2, CONF_NS2_PROPERTIES_1);
        assertThat(container.getLongProperty(NAMESPACE1, "long"), equalTo(111L)); // CONF_NS1_PROPERTIES_1
    }

}
