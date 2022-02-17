package net.obvj.confectory.util;

import static net.obvj.junit.utils.matchers.AdvancedMatchers.instantiationNotAllowed;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for the {@link ReflectionUtils} class.
 *
 * @author oswaldo.bapvic.jr
 */
class ReflectionUtilsTest
{

    @Test
    void constructor_instantiationNotAllowed()
    {
        assertThat(ReflectionUtils.class, instantiationNotAllowed().withMessage("Instantiation not allowed"));
    }

}
