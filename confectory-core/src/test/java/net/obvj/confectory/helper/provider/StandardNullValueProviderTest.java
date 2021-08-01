package net.obvj.confectory.helper.provider;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import net.obvj.confectory.helper.nullvalue.NullValueProvider;
import net.obvj.confectory.helper.nullvalue.StandardNullValueProvider;

/**
 * Unit tests for the {@link StandardNullValueProvider}.
 *
 * @author oswaldo.bapvic.jr (Oswaldo Junior)
 * @since 0.1.0
 */
class StandardNullValueProviderTest
{
    private NullValueProvider provider = StandardNullValueProvider.instance();

    @Test
    void isNull_booleans_success()
    {
        assertTrue(provider.isNull(false));
        assertFalse(provider.isNull(true));
    }

    @Test
    void isNull_ints_success()
    {
        assertTrue(provider.isNull(0));
        assertFalse(provider.isNull(-1));
    }

    @Test
    void isNull_longs_success()
    {
        assertTrue(provider.isNull(0L));
        assertFalse(provider.isNull(-1L));
    }

    @Test
    void isNull_doubles_success()
    {
        assertTrue(provider.isNull(0.0));
        assertFalse(provider.isNull(-1.0));
    }

    @Test
    void isNull_strings_success()
    {
        assertTrue(provider.isNull(""));
        assertFalse(provider.isNull("null"));
    }

}
