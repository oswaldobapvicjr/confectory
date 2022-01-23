package net.obvj.confectory.util;

import static net.obvj.junit.utils.matchers.AdvancedMatchers.instantiationNotAllowed;
import static net.obvj.junit.utils.matchers.AdvancedMatchers.throwsException;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for the {@link ParseFactory}.
 *
 * @author oswaldo.bapvic.jr
 * @since 1.2.0
 */
class ParseFactoryTest
{
    private static final String STR_TRUE = "true";
    private static final String STR_123 = "123";
    private static final String STR_A = "A";

    @Test
    void constructor_instantiationNotAllowed()
    {
        assertThat(ParseFactory.class, instantiationNotAllowed().throwing(UnsupportedOperationException.class));
    }

    @Test
    void pase_unknownType_unsupportedOperation()
    {
        assertThat(() -> ParseFactory.parse(Object.class, STR_123),
                throwsException(UnsupportedOperationException.class).withMessageContaining("Unsupported type"));
    }

    @Test
    void parse_primitiveTypes_success()
    {
        assertThat(ParseFactory.parse(boolean.class, STR_TRUE), equalTo(true));
        assertThat(ParseFactory.parse(int.class, STR_123), equalTo(123));
        assertThat(ParseFactory.parse(long.class, STR_123), equalTo(123l));
        assertThat(ParseFactory.parse(float.class, STR_123), equalTo(123f));
        assertThat(ParseFactory.parse(double.class, STR_123), equalTo(123.0));
        assertThat(ParseFactory.parse(char.class, STR_A), equalTo('A'));
    }

    @Test
    void parse_wrapperTypes_success()
    {
        assertThat(ParseFactory.parse(Boolean.class, STR_TRUE), equalTo(true));
        assertThat(ParseFactory.parse(Integer.class, STR_123), equalTo(123));
        assertThat(ParseFactory.parse(Long.class, STR_123), equalTo(123l));
        assertThat(ParseFactory.parse(Float.class, STR_123), equalTo(123f));
        assertThat(ParseFactory.parse(Double.class, STR_123), equalTo(123.0));
        assertThat(ParseFactory.parse(Character.class, STR_A), equalTo('A'));
    }

    @Test
    void parse_string_success()
    {
        assertThat(ParseFactory.parse(String.class, STR_123), sameInstance(STR_123));
    }

}
