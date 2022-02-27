package net.obvj.confectory.internal.helper;

import static net.obvj.junit.utils.matchers.AdvancedMatchers.throwsException;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.hamcrest.Matcher;
import org.junit.jupiter.api.Test;

import net.obvj.confectory.ConfigurationException;

/**
 * Unit tests for the {@link NullConfigurationHelper}.
 *
 * @author oswaldo.bapvic.jr (Oswaldo Junior)
 * @since 0.1.0
 */
class NullConfigurationHelperTest
{
    private static final String KEY1 = "key1";

    private static final Matcher<Runnable> EXCEPTION_NOT_FOUND = throwsException(ConfigurationException.class)
            .withMessageContaining("Not found");

    private NullConfigurationHelper<Object> helper = new NullConfigurationHelper<>();

    @Test
    void getBean_null()
    {
        assertNull(helper.getBean());
    }

    @Test
    void get_anyKey_null()
    {
        assertNull(helper.get(KEY1));
    }

    @Test
    void getBoolean_anyKey_null()
    {
        assertNull(helper.getBoolean(KEY1));
    }

    @Test
    void getInteger_anyKey_null()
    {
        assertNull(helper.getInteger(KEY1));
    }

    @Test
    void getLong_anyKey_null()
    {
        assertNull(helper.getLong(KEY1));
    }

    @Test
    void getDouble_anyKey_null()
    {
        assertNull(helper.getDouble(KEY1));
    }

    @Test
    void getString_anyKey_null()
    {
        assertNull(helper.getString(KEY1));
    }

    @Test
    void getMandatoryBoolean_anyKey_exception()
    {
        assertThat(() -> helper.getMandatoryBoolean(KEY1), EXCEPTION_NOT_FOUND);
    }

    @Test
    void getMandatoryInteger_anyKey_exception()
    {
        assertThat(() -> helper.getMandatoryInteger(KEY1), EXCEPTION_NOT_FOUND);
    }

    @Test
    void getMandatoryLong_anyKey_exception()
    {
        assertThat(() -> helper.getMandatoryLong(KEY1), EXCEPTION_NOT_FOUND);
    }

    @Test
    void getMandatoryDouble_anyKey_exception()
    {
        assertThat(() -> helper.getMandatoryDouble(KEY1), EXCEPTION_NOT_FOUND);
    }

    @Test
    void getMandatoryString_anyKey_exception()
    {
        assertThat(() -> helper.getMandatoryString(KEY1), EXCEPTION_NOT_FOUND);
    }

}
