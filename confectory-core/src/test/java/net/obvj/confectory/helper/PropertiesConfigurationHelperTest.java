package net.obvj.confectory.helper;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.Properties;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import net.obvj.confectory.helper.provider.AbstractNullValueProvider;
import net.obvj.confectory.helper.provider.NullValueProvider;

/**
 * Unit tests for the {@link PropertiesConfigurationHelper}.
 *
 * @author oswaldo.bapvic.jr (Oswaldo Junior)
 * @since 0.1.0
 */
@ExtendWith(MockitoExtension.class)
class PropertiesConfigurationHelperTest
{
    private static final String PROP_UNKNOWN = "prop.unknown";

    private static final Properties PROPERTIES = new Properties();

    private static final PropertiesConfigurationHelper HELPER = new PropertiesConfigurationHelper(PROPERTIES);

    private static final NullValueProvider CUSTOM_NULL_VALUE_PROVIDER = new AbstractNullValueProvider()
    {
        @Override
        public String getStringValue()
        {
            return "<<null>>";
        }

        @Override
        public long getLongValue()
        {
            return -1L;
        }

        @Override
        public int getIntValue()
        {
            return -1;
        }

        @Override
        public double getDoubleValue()
        {
            return -1.0;
        }

        @Override
        public boolean getBooleanValue()
        {
            return false;
        }
    };

    @BeforeAll
    static void setupProperties()
    {
        PROPERTIES.put("prop.boolean", "true");
        PROPERTIES.put("prop.int", "2015");
        PROPERTIES.put("prop.long", "9876543210");
        PROPERTIES.put("prop.double", "3.333");
        PROPERTIES.put("prop.string", "stringValue");

        HELPER.setNullValueProvider(CUSTOM_NULL_VALUE_PROVIDER);
    }

    @Test
    void getBean_empty()
    {
        assertThat(HELPER.getBean().get(), is(sameInstance(PROPERTIES)));
    }

    @Test
    void getBooleanProperty_exisitngKey_true()
    {
        assertThat(HELPER.getBooleanProperty("prop.boolean"), is(true));
    }

    @Test
    void getBooleanProperty_unknownKey_false()
    {
        assertThat(HELPER.getBooleanProperty(PROP_UNKNOWN), is(false));
    }

    @Test
    void getIntProperty_existingKey_zero()
    {
        assertThat(HELPER.getIntProperty("prop.int"), is(2015));
    }

    @Test
    void getIntProperty_unknownKey_zero()
    {
        assertThat(HELPER.getIntProperty(PROP_UNKNOWN), is(CUSTOM_NULL_VALUE_PROVIDER.getIntValue()));
    }

    @Test
    void getLongProperty_anyKey_zero()
    {
        assertThat(HELPER.getLongProperty("prop.long"), is(9876543210L));
    }

    @Test
    void getLongProperty_unknownKey_zero()
    {
        assertThat(HELPER.getLongProperty(PROP_UNKNOWN), is(CUSTOM_NULL_VALUE_PROVIDER.getLongValue()));
    }

    @Test
    void getDoubleProperty_anyKey_zero()
    {
        assertThat(HELPER.getDoubleProperty("prop.double"), is(3.333));
    }

    @Test
    void getDoubleProperty_unknownKey_zero()
    {
        assertThat(HELPER.getDoubleProperty(PROP_UNKNOWN), is(CUSTOM_NULL_VALUE_PROVIDER.getDoubleValue()));
    }

    @Test
    void getSringProperty_exisingKey_empty()
    {
        assertThat(HELPER.getStringProperty("prop.string"), is("stringValue"));
    }

    @Test
    void getSringProperty_unknownKey_empty()
    {
        assertThat(HELPER.getStringProperty(PROP_UNKNOWN), is(CUSTOM_NULL_VALUE_PROVIDER.getStringValue()));
    }

}
