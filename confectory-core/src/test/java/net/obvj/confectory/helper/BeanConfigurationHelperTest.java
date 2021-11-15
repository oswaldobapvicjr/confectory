package net.obvj.confectory.helper;

import static net.obvj.junit.utils.matchers.AdvancedMatchers.throwsException;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;

import net.obvj.confectory.ConfigurationException;
import net.obvj.junit.utils.matchers.ExceptionMatcher;

/**
 * Unit tests for the {@link BeanConfigurationHelper}.
 *
 * @author oswaldo.bapvic.jr (Oswaldo Junior)
 * @since 0.4.0
 */
class BeanConfigurationHelperTest
{
    private static final String KEY = "key";
    private static final String OBJECT = "test";
    private static final ConfigurationHelper<String> HELPER = new BeanConfigurationHelper<>(OBJECT);

    private static final ExceptionMatcher CONFIGURATION_EXCEPTION_TYPE_NOT_SUPPORTED = throwsException(
            ConfigurationException.class).withMessage("Operation not supported for bean of type 'java.lang.String'");

    @Test
    void getBean_notEmpty()
    {
        assertThat(HELPER.getBean().get(), is(sameInstance(OBJECT)));
    }

    @Test
    void getBoolean_existingKey_true()
    {
        assertThat(() -> HELPER.getBoolean(KEY), CONFIGURATION_EXCEPTION_TYPE_NOT_SUPPORTED);
    }

    @Test
    void getInt_existingKey_zero()
    {
        assertThat(() -> HELPER.getInt(KEY), CONFIGURATION_EXCEPTION_TYPE_NOT_SUPPORTED);
    }

    @Test
    void getLong_existingKey_zero()
    {
        assertThat(() -> HELPER.getLong(KEY), CONFIGURATION_EXCEPTION_TYPE_NOT_SUPPORTED);
    }

    @Test
    void getDouble_existingKey_zero()
    {
        assertThat(() -> HELPER.getDouble(KEY), CONFIGURATION_EXCEPTION_TYPE_NOT_SUPPORTED);
    }

    @Test
    void getSring_existingKey_empty()
    {
        assertThat(() -> HELPER.getString(KEY), CONFIGURATION_EXCEPTION_TYPE_NOT_SUPPORTED);
    }

    @Test
    void getMandatorySring_existingKey_empty()
    {
        assertThat(() -> HELPER.getMandatoryString(KEY), CONFIGURATION_EXCEPTION_TYPE_NOT_SUPPORTED);
    }

}
