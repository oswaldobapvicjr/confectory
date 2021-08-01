package net.obvj.confectory;

import static net.obvj.junit.utils.matchers.AdvancedMatchers.containsAll;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import net.obvj.confectory.mapper.StringMapper;
import net.obvj.confectory.source.StringSource;

/**
 * Unit tests for the {@link Configuration} class.
 *
 * @author oswaldo.bapvic.jr (Oswaldo Junior)
 * @since 0.1.0
 */
class ConfigurationTest
{
    private static final String NAMESPACE1 = "namespace1";
    private static final String NAMESPACE2 = "namespace2";
    private static final String STRING1 = "string1";
    private static final String STRING2 = "string2";
    private static final StringSource<String> SOURCE_STRING1 = new StringSource<>(STRING1);
    private static final StringSource<String> SOURCE_STRING2 = new StringSource<>(STRING2);

    private static final Configuration<String> CONFIG_NS1_STRING_1 = Configuration.<String>builder()
            .namespace(NAMESPACE1).precedence(1).required().source(SOURCE_STRING1).mapper(new StringMapper()).build();

    // CONFIG_NS1_STRING_1B - Should be considered equal to CONFIG_NS1_STRING_1
    private static final Configuration<String> CONFIG_NS1_STRING_1B = Configuration.<String>builder()
            .namespace(NAMESPACE1).precedence(2).optional().source(SOURCE_STRING1).mapper(new StringMapper()).build();

    private static final Configuration<String> CONFIG_NS1_STRING_2 = Configuration.<String>builder()
            .namespace(NAMESPACE1).source(SOURCE_STRING2).mapper(new StringMapper()).build();

    private static final Configuration<String> CONFIG_NS2_STRING_1 = Configuration.<String>builder()
            .namespace(NAMESPACE2).source(SOURCE_STRING1).mapper(new StringMapper()).build();

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
                containsAll("namespace:" + NAMESPACE1, "source:" + SOURCE_STRING1, "precedence:1"));

        assertThat(CONFIG_NS1_STRING_1B.toString().replaceAll("\"", ""),
                containsAll("namespace:" + NAMESPACE1, "source:" + SOURCE_STRING1, "precedence:2"));
    }

}
