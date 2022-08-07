package net.obvj.confectory;

import static net.obvj.junit.utils.matchers.AdvancedMatchers.containsAll;
import static net.obvj.junit.utils.matchers.AdvancedMatchers.throwsException;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import org.hamcrest.Matcher;
import org.junit.jupiter.api.Test;

import net.obvj.confectory.internal.helper.NullConfigurationHelper;
import net.obvj.confectory.mapper.PropertiesMapper;
import net.obvj.confectory.mapper.StringMapper;
import net.obvj.confectory.source.FileSource;
import net.obvj.confectory.source.Source;
import net.obvj.confectory.source.StringSource;

/**
 * Unit tests for the {@link Configuration} class.
 *
 * @author oswaldo.bapvic.jr (Oswaldo Junior)
 * @since 0.1.0
 */
class ConfigurationTest
{

    private static final Matcher<Runnable> CONFIGURATION_EXCEPTION_NO_VALUE_FOUND = throwsException(
            ConfigurationException.class).withMessageContaining("No value found");

    private static final String UNKNOWN = "unknown";
    private static final String NAMESPACE1 = "namespace1";
    private static final String NAMESPACE2 = "namespace2";
    private static final String STRING1 = "string1";
    private static final String STRING2 = "string2";
    private static final Source<String> SOURCE_STRING1 = new StringSource<>(STRING1);
    private static final Source<String> SOURCE_STRING2 = new StringSource<>(STRING2);

    private static final Configuration<String> CONFIG_NS1_STRING_1 = Configuration.<String>builder()
            .namespace(NAMESPACE1).precedence(1).required().source(SOURCE_STRING1).mapper(new StringMapper()).build();

    // CONFIG_NS1_STRING_1B - Should be considered equal to CONFIG_NS1_STRING_1
    private static final Configuration<String> CONFIG_NS1_STRING_1B = Configuration.<String>builder()
            .namespace(NAMESPACE1).precedence(2).optional().source(SOURCE_STRING1).mapper(new StringMapper()).build();

    private static final Configuration<String> CONFIG_NS1_STRING_2 = Configuration.<String>builder()
            .namespace(NAMESPACE1).source(SOURCE_STRING2).mapper(new StringMapper()).build();

    private static final Configuration<String> CONFIG_NS2_STRING_1 = Configuration.<String>builder()
            .namespace(NAMESPACE2).source(SOURCE_STRING1).mapper(new StringMapper()).build();

    private static final Configuration<Properties> CONFIG_PROPERTIES_1 = Configuration.<Properties>builder()
            .source(new StringSource<>("myKey=myValue\nmyBool=true\nmyInt=9\nmyLong=9876543210\nmyDouble=7.89"))
            .precedence(1).mapper(new PropertiesMapper()).build();

    private static final Configuration<Properties> CONFIG_PROPERTIES_2 = Configuration.<Properties>builder()
            .source(new StringSource<>("myKey=myValue2\nmyInt=10"))
            .precedence(5).mapper(new PropertiesMapper()).build();

    private static final Configuration<Properties> CONFIG_PROPERTIES_OPTIONAL = Configuration.<Properties>builder()
            .source(new FileSource<>("unknown.properties")).optional()
            .precedence(9).mapper(new PropertiesMapper()).build();

    @Test
    void equals_sameObjectAndSimilarObject_true()
    {
        assertEquals(CONFIG_NS1_STRING_1, CONFIG_NS1_STRING_1);
        assertEquals(CONFIG_NS1_STRING_1, CONFIG_NS1_STRING_1B);
    }

    @Test
    void equals_sameTypeButDifferentAttributes_false()
    {
        assertNotEquals(CONFIG_NS1_STRING_1, CONFIG_NS1_STRING_2);
        assertNotEquals(CONFIG_NS1_STRING_1, CONFIG_NS2_STRING_1);
    }

    @Test
    void equals_differentTypes_false()
    {
        assertNotEquals(CONFIG_NS1_STRING_1, (Configuration<String>) null);
        assertNotEquals(CONFIG_NS1_STRING_1, NAMESPACE1);
    }

    @Test
    void equalsAndHashCode_twoSimilarObjectsPlacedInAHashSet_sameObject()
    {
        Set<?> set = new HashSet<>(Arrays.asList(CONFIG_NS1_STRING_1, CONFIG_NS1_STRING_1B));
        assertEquals(1, set.size());
    }

    @Test
    void equalsAndHashCode_notSimilarObjectsPlacedInAHashSet_differentObject()
    {
        Set<?> set = new HashSet<>(Arrays.asList(CONFIG_NS1_STRING_1, CONFIG_NS2_STRING_1, CONFIG_NS1_STRING_2));
        assertEquals(3, set.size());
    }

    @Test
    void toString_validString()
    {
        assertThat(CONFIG_NS1_STRING_1.toString().replaceAll("\"", ""),
                containsAll("namespace:" + NAMESPACE1, "source:StringSource", "precedence:1"));

        assertThat(CONFIG_NS1_STRING_1B.toString().replaceAll("\"", ""),
                containsAll("namespace:" + NAMESPACE1, "source:StringSource", "precedence:2"));
    }

    @Test
    void get_validKey_value()
    {
        // java.util.Properties doesn't convert strings
        assertThat(CONFIG_PROPERTIES_1.get("myBool"), equalTo("true"));
    }

    @Test
    void get_invalidKey_null()
    {
        assertThat(CONFIG_PROPERTIES_1.get(UNKNOWN), equalTo(null));
    }

    @Test
    void getString_validKey_value()
    {
        assertThat(CONFIG_PROPERTIES_1.getString("myKey"), equalTo("myValue"));
    }

    @Test
    void getString_invalidKey_null()
    {
        assertThat(CONFIG_PROPERTIES_1.getString(UNKNOWN), equalTo(null));
    }

    @Test
    void getMandatoryString_validKey_value()
    {
        assertThat(CONFIG_PROPERTIES_1.getMandatoryString("myKey"), equalTo("myValue"));
    }

    @Test
    void getMandatoryString_invalidKey_configurationException()
    {
        assertThat(() -> CONFIG_PROPERTIES_1.getMandatoryString(UNKNOWN), CONFIGURATION_EXCEPTION_NO_VALUE_FOUND);
    }

    @Test
    void getBoolean_validKey_value()
    {
        assertThat(CONFIG_PROPERTIES_1.getBoolean("myBool"), equalTo(true));
    }

    @Test
    void getBoolean_invalidKey_null()
    {
        assertThat(CONFIG_PROPERTIES_1.getBoolean(UNKNOWN), equalTo(null));
    }

    @Test
    void getMandatoryBoolean_validKey_value()
    {
        assertThat(CONFIG_PROPERTIES_1.getMandatoryBoolean("myBool"), equalTo(true));
    }

    @Test
    void getMandatoryBoolean_invalidKey_configurationException()
    {
        assertThat(() -> CONFIG_PROPERTIES_1.getMandatoryBoolean(UNKNOWN), CONFIGURATION_EXCEPTION_NO_VALUE_FOUND);
    }

    @Test
    void getInteger_validKey_value()
    {
        assertThat(CONFIG_PROPERTIES_1.getInteger("myInt"), equalTo(9));
    }

    @Test
    void getInteger_invalidKey_null()
    {
        assertThat(CONFIG_PROPERTIES_1.getInteger(UNKNOWN), equalTo(null));
    }

    @Test
    void getMandatoryInteger_validKey_value()
    {
        assertThat(CONFIG_PROPERTIES_1.getMandatoryInteger("myInt"), equalTo(9));
    }

    @Test
    void getMandatoryInteger_invalidKey_configurationException()
    {
        assertThat(() -> CONFIG_PROPERTIES_1.getMandatoryInteger(UNKNOWN), CONFIGURATION_EXCEPTION_NO_VALUE_FOUND);
    }

    @Test
    void getLong_validKey_value()
    {
        assertThat(CONFIG_PROPERTIES_1.getLong("myLong"), equalTo(9876543210L));
    }

    @Test
    void getLong_invalidKey_null()
    {
        assertThat(CONFIG_PROPERTIES_1.getLong(UNKNOWN), equalTo(null));
    }

    @Test
    void getMandatoryLong_validKey_value()
    {
        assertThat(CONFIG_PROPERTIES_1.getMandatoryLong("myLong"), equalTo(9876543210L));
    }

    @Test
    void getMandatoryLong_invalidKey_configurationException()
    {
        assertThat(() -> CONFIG_PROPERTIES_1.getMandatoryLong(UNKNOWN), CONFIGURATION_EXCEPTION_NO_VALUE_FOUND);
    }

    @Test
    void getDouble_validKey_value()
    {
        assertThat(CONFIG_PROPERTIES_1.getDouble("myDouble"), equalTo(7.89));
    }

    @Test
    void getDouble_invalidKey_null()
    {
        assertThat(CONFIG_PROPERTIES_1.getDouble(UNKNOWN), equalTo(null));
    }

    @Test
    void getMandatoryDouble_validKey_value()
    {
        assertThat(CONFIG_PROPERTIES_1.getMandatoryDouble("myDouble"), equalTo(7.89));
    }

    @Test
    void getMandatoryDouble_invalidKey_configurationException()
    {
        assertThat(() -> CONFIG_PROPERTIES_1.getMandatoryDouble(UNKNOWN), CONFIGURATION_EXCEPTION_NO_VALUE_FOUND);
    }

    @Test
    void merge_validConfig_newCombinedConfig()
    {
        Configuration<Properties> result = CONFIG_PROPERTIES_1.merge(CONFIG_PROPERTIES_2);
        assertThat(result.getString("myKey"),    equalTo("myValue2"));  // CONFIG_PROPERTIES_2
        assertThat(result.getInteger("myInt"),   equalTo(10));          // CONFIG_PROPERTIES_2
        assertThat(result.getBoolean("myBool"),  equalTo(true));        // CONFIG_PROPERTIES_1
        assertThat(result.getLong("myLong"),     equalTo(9876543210L)); // CONFIG_PROPERTIES_1
        assertThat(result.getDouble("myDouble"), equalTo(7.89));        // CONFIG_PROPERTIES_1
    }

    /**
     * This test is to verify the expected merger is selected even when the
     * {@link NullConfigurationHelper} is in use.
     */
    @Test
    void merge_nullWithValidConfig_newCombinedConfig()
    {
        Configuration<Properties> result = CONFIG_PROPERTIES_OPTIONAL.merge(CONFIG_PROPERTIES_1);
        assertThat(result.getString("myKey"), equalTo("myValue")); // CONFIG_PROPERTIES_1
        assertThat(result.getInteger("myInt"), equalTo(9)); // CONFIG_PROPERTIES_1
        assertThat(result.getBoolean("myBool"), equalTo(true)); // CONFIG_PROPERTIES_1
        assertThat(result.getLong("myLong"), equalTo(9876543210L)); // CONFIG_PROPERTIES_1
        assertThat(result.getDouble("myDouble"), equalTo(7.89)); // CONFIG_PROPERTIES_1

        assertNotSame(CONFIG_PROPERTIES_1, result);
    }

    @Test
    void merge_validConfigWithNullConfig_newCombinedConfig()
    {
        Configuration<Properties> result = CONFIG_PROPERTIES_1.merge(CONFIG_PROPERTIES_OPTIONAL);
        assertThat(result.getString("myKey"),    equalTo("myValue"));   // CONFIG_PROPERTIES_1
        assertThat(result.getInteger("myInt"),   equalTo(9));           // CONFIG_PROPERTIES_1
        assertThat(result.getBoolean("myBool"),  equalTo(true));        // CONFIG_PROPERTIES_1
        assertThat(result.getLong("myLong"),     equalTo(9876543210L)); // CONFIG_PROPERTIES_1
        assertThat(result.getDouble("myDouble"), equalTo(7.89));        // CONFIG_PROPERTIES_1

        assertNotSame(CONFIG_PROPERTIES_1, result);
    }

}
