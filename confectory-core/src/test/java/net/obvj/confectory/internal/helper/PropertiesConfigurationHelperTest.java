package net.obvj.confectory.internal.helper;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;

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
    private static final String PROP_UNKNOWN = "prop.unknown";

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
    void getBean_null()
    {
        assertThat(HELPER.getBean(), is(sameInstance(PROPERTIES)));
    }

    @Test
    void getBoolean_existingKey_valid()
    {
        assertThat(HELPER.getBoolean("prop.boolean"), is(true));
    }

    @Test
    void getBoolean_unknownKey_null()
    {
        assertNull(HELPER.getBoolean(PROP_UNKNOWN));
    }

    @Test
    void getInteger_existingKey_valid()
    {
        assertThat(HELPER.getInteger("prop.int"), is(2015));
    }

    @Test
    void getInteger_unknownKey_null()
    {
        assertNull(HELPER.getInteger(PROP_UNKNOWN));
    }

    @Test
    void getLong_existingKey_valid()
    {
        assertThat(HELPER.getLong("prop.long"), is(9876543210L));
    }

    @Test
    void getLong_unknownKey_null()
    {
        assertNull(HELPER.getLong(PROP_UNKNOWN));
    }

    @Test
    void getDouble_existingKey_valid()
    {
        assertThat(HELPER.getDouble("prop.double"), is(3.333));
    }

    @Test
    void getDouble_unknownKey_null()
    {
        assertNull(HELPER.getDouble(PROP_UNKNOWN));
    }

    @Test
    void getString_existingKey_valid()
    {
        assertThat(HELPER.getString("prop.string"), is("stringValue"));
    }

    @Test
    void getString_unknownKey_null()
    {
        assertNull(HELPER.getString(PROP_UNKNOWN));
    }

}
