package net.obvj.confectory.source;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import net.obvj.confectory.mapper.Mapper;

/**
 * Unit tests for the {@link AbstractSource} class.
 *
 * @author oswaldo.bapvic.jr (Oswaldo Junior)
 * @since 0.1.0
 */
class AbstractSourceTest
{
    private static final String PARAM1 = "param1";
    private static final String PARAM2 = "param2";

    private static final AbstractSource<String> SOURCE1 = new AbstractSource<String>(PARAM1)
    {
        @Override
        public String load(Mapper<String> mapper)
        {
            return null;
        }
    };

    private static final StringSource<String> SOURCE_STRING1 = new StringSource<>(PARAM1);
    private static final StringSource<String> SOURCE_STRING1B = new StringSource<>(PARAM1);
    private static final StringSource<String> SOURCE_STRING2 = new StringSource<>(PARAM2);

    @Test
    void equals_sameObjectAndSimilarObject_true()
    {
        assertEquals(SOURCE1, SOURCE1);
        assertEquals(SOURCE_STRING1, SOURCE_STRING1B);
    }

    @Test
    void equals_sameTypeButDifferentAttributes_false()
    {
        assertNotEquals(SOURCE_STRING1, SOURCE_STRING2);
    }

    @Test
    void equals_differentTypes_false()
    {
        assertNotEquals(SOURCE_STRING1, SOURCE1);
        assertNotEquals(SOURCE_STRING1, (Source<String>) null);
        assertNotEquals(SOURCE_STRING1, PARAM1);
    }

    @Test
    void equalsAndHashCode_twoSimilarObjectsPlacedInAHashSet_sameObject()
    {
        Set<?> set = new HashSet<>(Arrays.asList(SOURCE_STRING1, SOURCE_STRING1B));
        assertEquals(1, set.size());
    }

    @Test
    void equalsAndHashCode_notSimilarObjectsPlacedInAHashSet_differentObject()
    {
        Set<?> set = new HashSet<>(Arrays.asList(SOURCE_STRING1, SOURCE_STRING2, SOURCE1));
        assertEquals(3, set.size());
    }

}
