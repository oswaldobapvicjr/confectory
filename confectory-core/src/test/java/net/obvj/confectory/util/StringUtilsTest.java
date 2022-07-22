package net.obvj.confectory.util;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static net.obvj.junit.utils.matchers.AdvancedMatchers.*;

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
    private static final String MESSAGE = "message";

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

    @Test
    void requireNonBlank_validString_validString()
    {
        assertThat(StringUtils.requireNonBlankAndTrim(TEST, MESSAGE), equalTo(TEST));
    }

    @Test
    void requireNonBlank_validStringContainingBlankCharacter_trimmed()
    {
        assertThat(StringUtils.requireNonBlankAndTrim(" string ", MESSAGE), equalTo("string"));
    }

    @Test
    void requireNonBlank_null_illegalArgumentException()
    {
        assertThat(() -> StringUtils.requireNonBlankAndTrim(null, MESSAGE),
                throwsException(IllegalArgumentException.class).withMessage(MESSAGE));
    }

    @Test
    void requireNonBlank_empty_illegalArgumentException()
    {
        assertThat(() -> StringUtils.requireNonBlankAndTrim("", MESSAGE),
                throwsException(IllegalArgumentException.class).withMessage(MESSAGE));
    }

    @Test
    void requireNonBlank_blank_illegalArgumentException()
    {
        assertThat(() -> StringUtils.requireNonBlankAndTrim("\t", MESSAGE),
                throwsException(IllegalArgumentException.class).withMessage(MESSAGE));
    }
}
