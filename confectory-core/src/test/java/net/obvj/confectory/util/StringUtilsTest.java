package net.obvj.confectory.util;

import static net.obvj.junit.utils.matchers.AdvancedMatchers.containsAny;
import static net.obvj.junit.utils.matchers.AdvancedMatchers.instantiationNotAllowed;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for the {@link StringUtils} class.
 *
 * @author oswaldo.bapvic.jr
 */
class StringUtilsTest
{
    private static final String TEST = "test";
    private static final String TEST_$_UNKNOWN = "test=${unknown}";

    @Test
    void constructor_instantiationNotAllowed()
    {
        assertThat(StringUtils.class, instantiationNotAllowed());
    }

    @Test
    void expandVariables_stringWithoutVariables_sameString()
    {
        assertThat(StringUtils.expandEnvironmentVariables(TEST), equalTo(TEST));
    }

    @Test
    void expandVariables_stringWithUnknownVariable_sameString()
    {
        assertThat(StringUtils.expandEnvironmentVariables(TEST_$_UNKNOWN), equalTo(TEST_$_UNKNOWN));
    }

    @Test
    void expandVariables_stringWithKnownVariable_sameString()
    {
        assertThat(StringUtils.expandEnvironmentVariables("test=${PATH}"),
                (allOf(startsWith("test="), not(containsAny(("${PATH}"))))));
    }

}
