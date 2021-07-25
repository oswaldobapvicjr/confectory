package net.obvj.confectory.config;

import static net.obvj.junit.utils.matchers.AdvancedMatchers.throwsException;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static net.obvj.confectory.config.ConfectoryConfiguration.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import net.obvj.confectory.helper.provider.NullValueProvider;

/**
 * Unit tests for the {@link ConfectoryConfiguration}.
 *
 * @author oswaldo.bapvic.jr (Oswaldo Junior)
 * @since 0.1.0
 */
@ExtendWith(MockitoExtension.class)
class ConfectoryConfigurationTest
{
    @Mock
    private NullValueProvider nullValueProvider;

    private ConfectoryConfiguration configuration = ConfectoryConfiguration.getInstance();

    @AfterEach
    void reset()
    {
        configuration.reset();
    }

    @Test
    void setDefaultNullValueProvider_null_exceptionAndNoChangePerformed()
    {
        assertThat(configuration.getDefaultNullValueProvider(), is(INITIAL_NULL_VALUE_PROVIDER));
        assertThat(() -> configuration.setDefaultNullValueProvider(null),
                throwsException(NullPointerException.class).withMessage("null is not allowed"));
        assertThat(configuration.getDefaultNullValueProvider(), is(INITIAL_NULL_VALUE_PROVIDER));
    }

    @Test
    void setDefaultNullValueProvider_valid_success()
    {
        assertThat(configuration.getDefaultNullValueProvider(), is(INITIAL_NULL_VALUE_PROVIDER));
        configuration.setDefaultNullValueProvider(nullValueProvider);
        assertThat(configuration.getDefaultNullValueProvider(), is(nullValueProvider));
    }

}
