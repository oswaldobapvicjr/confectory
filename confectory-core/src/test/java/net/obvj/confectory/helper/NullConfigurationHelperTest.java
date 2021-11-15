package net.obvj.confectory.helper;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for the {@link NullConfigurationHelper}.
 *
 * @author oswaldo.bapvic.jr (Oswaldo Junior)
 * @since 0.1.0
 */
class NullConfigurationHelperTest
{
    private static final String KEY1 = "key1";

    private NullConfigurationHelper<Object> helper = new NullConfigurationHelper<>();

    @Test
    void getBean_empty()
    {
        assertThat(helper.getBean(), is(Optional.empty()));
    }

    @Test
    void getBoolean_anyKey_false()
    {
        assertThat(helper.getBoolean(KEY1), is(false));
    }

    @Test
    void getInt_anyKey_zero()
    {
        assertThat(helper.getInt(KEY1), is(0));
    }

    @Test
    void getLong_anyKey_zero()
    {
        assertThat(helper.getLong(KEY1), is(0L));
    }

    @Test
    void getDouble_anyKey_zero()
    {
        assertThat(helper.getDouble(KEY1), is(0.0));
    }

    @Test
    void getSring_anyKey_empty()
    {
        assertThat(helper.getString(KEY1), is(""));
    }

    @Test
    void getMandatoryBoolean_anyKey_false()
    {
        assertThat(helper.getMandatoryBoolean(KEY1), is(false));
    }

    @Test
    void getMandatoryInt_anyKey_zero()
    {
        assertThat(helper.getMandatoryInt(KEY1), is(0));
    }

    @Test
    void getMandatoryLong_anyKey_zero()
    {
        assertThat(helper.getMandatoryLong(KEY1), is(0L));
    }

    @Test
    void getMandatoryDouble_anyKey_zero()
    {
        assertThat(helper.getMandatoryDouble(KEY1), is(0.0));
    }

    @Test
    void getMandatorySring_anyKey_empty()
    {
        assertThat(helper.getMandatoryString(KEY1), is(""));
    }

}
