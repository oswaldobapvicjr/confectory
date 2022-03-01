package net.obvj.confectory.source;

import static net.obvj.junit.utils.matchers.AdvancedMatchers.throwsException;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import net.obvj.confectory.ConfigurationException;
import net.obvj.confectory.mapper.Mapper;

/**
 * Unit tests for the {@link DummySource} class.
 *
 * @author oswaldo.bapvic.jr (Oswaldo Junior)
 * @since 2.1.0
 */
@ExtendWith(MockitoExtension.class)
class DummySourceTest
{
    private Source<Object> source1 = new DummySource<>();
    private Source<Object> source2 = new DummySource<>();

    @Mock
    private Mapper<Object> mapper;

    @Test
    void equals_differentObject_false()
    {
        assertNotEquals(source1, source2);
    }

    @Test
    void equalsAndHashCode_twoObjectsPlacedInAHashSet_differentObject()
    {
        Set<?> set = new HashSet<>(Arrays.asList(source1, source2));
        assertEquals(2, set.size());
    }

    @Test
    void load_configurationException()
    {
        assertThat(() -> source1.load(mapper),
                throwsException(ConfigurationException.class).withMessage("Dummy source"));
    }

}
