package net.obvj.confectory.util;

import static net.obvj.junit.utils.TestUtils.assertException;
import static net.obvj.junit.utils.matchers.AdvancedMatchers.instantiationNotAllowed;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for the {@link Exceptions} class.
 *
 * @author Oswaldo Junior (oswaldo.bapvic.jr)
 */
class ExceptionsTest
{
    private static final String MSG_PATTERN = "arg1=%s,arg2=%s";
    private static final String ARG1 = "abc";
    private static final String ARG2 = "123";
    private static final String EXPECTED_MSG = "arg1=abc,arg2=123";

    @Test
    void constructor_instantiationNotAllowed()
    {
        assertThat(Exceptions.class, instantiationNotAllowed().throwing(IllegalStateException.class));
    }

    @Test
    void illegalArgument_messageAndParams_validMessage()
    {
        assertException(IllegalArgumentException.class, EXPECTED_MSG,
                Exceptions.illegalArgument(MSG_PATTERN, ARG1, ARG2));
    }

    @Test
    void illegalArgument_messageAndParamsAndCause_validMessageAndCause()
    {
        assertException(IllegalArgumentException.class, EXPECTED_MSG, NullPointerException.class,
                Exceptions.illegalArgument(new NullPointerException(), MSG_PATTERN, ARG1, ARG2));
    }

    @Test
    void illegalState_messageAndParams_validMessage()
    {
        assertException(IllegalStateException.class, EXPECTED_MSG, Exceptions.illegalState(MSG_PATTERN, ARG1, ARG2));
    }

    @Test
    void illegalState_messageAndParamsAndCause_validMessageAndCause()
    {
        assertException(IllegalStateException.class, EXPECTED_MSG, NullPointerException.class,
                Exceptions.illegalState(new NullPointerException(), MSG_PATTERN, ARG1, ARG2));
    }

}
