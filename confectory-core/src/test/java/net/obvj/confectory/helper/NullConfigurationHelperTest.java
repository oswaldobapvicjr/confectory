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
    void getBooleanProperty_anyKey_false()
    {
        assertThat(helper.getBoolean(KEY1), is(false));
    }

    @Test
    void getIntProperty_anyKey_zero()
    {
        assertThat(helper.getInt(KEY1), is(0));
    }

    @Test
    void getLongProperty_anyKey_zero()
    {
        assertThat(helper.getLong(KEY1), is(0L));
    }

    @Test
    void getDoubleProperty_anyKey_zero()
    {
        assertThat(helper.getDouble(KEY1), is(0.0));
    }

    @Test
    void getSringProperty_anyKey_empty()
    {
        assertThat(helper.getString(KEY1), is(""));
    }

}
