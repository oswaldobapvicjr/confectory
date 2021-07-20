package net.obvj.confectory.helper;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.Optional;
import java.util.Properties;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Unit tests for the {@link PropertiesConfigurationHelper}.
 *
 * @author oswaldo.bapvic.jr (Oswaldo Junior)
 * @since 0.1.0
 */
@ExtendWith(MockitoExtension.class)
class PropertiesConfigurationHelperTest
{
    private static final Properties PROPERTIES = new Properties();

    private static final PropertiesConfigurationHelper HELPER = new PropertiesConfigurationHelper(PROPERTIES);

    @BeforeAll
    static void setupProperties()
    {
        PROPERTIES.put("prop.boolean", "true");
        PROPERTIES.put("prop.int", "2015");
        PROPERTIES.put("prop.long", "9876543210");
        PROPERTIES.put("prop.double", "3.333");
        PROPERTIES.put("prop.string", "stringValue");
    }

    @Test
    void getBean_empty()
    {
        assertThat(HELPER.getBean().get(), is(sameInstance(PROPERTIES)));
    }

    @Test
    void getBooleanProperty_exisitngKey_false()
    {
        assertThat(HELPER.getBooleanProperty("prop.boolean"), is(true));
    }

    @Test
    void getIntProperty_existingKey_zero()
    {
        assertThat(HELPER.getIntProperty("prop.int"), is(2015));
    }

    @Test
    void getLongProperty_anyKey_zero()
    {
        assertThat(HELPER.getLongProperty("prop.long"), is(9876543210L));
    }

    @Test
    void getDoubleProperty_anyKey_zero()
    {
        assertThat(HELPER.getDoubleProperty("prop.double"), is(3.333));
    }

    @Test
    void getSringProperty_exisingKey_empty()
    {
        assertThat(HELPER.getStringProperty("prop.string"), is("stringValue"));
    }

    @Test
    void getOptionalSringProperty_unknownKey_empty()
    {
        assertThat(HELPER.getOptionalStringProperty("prop.unknown"), is(Optional.empty()));
    }

    @Test
    void getOptionalStringProperty_existingKey_zero()
    {
        assertThat(HELPER.getOptionalStringProperty("prop.string").get(), is("stringValue"));
    }
}
