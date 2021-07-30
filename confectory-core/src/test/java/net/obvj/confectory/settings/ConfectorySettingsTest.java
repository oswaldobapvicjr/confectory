package net.obvj.confectory.settings;

import static net.obvj.confectory.settings.ConfectorySettings.*;
import static net.obvj.junit.utils.matchers.AdvancedMatchers.throwsException;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import net.obvj.confectory.DataFetchStrategy;
import net.obvj.confectory.helper.nullvalue.NullValueProvider;

/**
 * Unit tests for the {@link ConfectorySettings}.
 *
 * @author oswaldo.bapvic.jr (Oswaldo Junior)
 * @since 0.1.0
 */
@ExtendWith(MockitoExtension.class)
class ConfectorySettingsTest
{
    @Mock
    private NullValueProvider nullValueProvider;
    @Mock
    private DataFetchStrategy dataFetchStrategy;

    private ConfectorySettings settings = ConfectorySettings.getInstance();

    @AfterEach
    void reset()
    {
        settings.reset();
    }

    @Test
    void setDefaultNullValueProvider_null_exceptionAndNoChangePerformed()
    {
        assertThat(settings.getDefaultNullValueProvider(), is(INITIAL_NULL_VALUE_PROVIDER));
        assertThat(() -> settings.setDefaultNullValueProvider(null),
                throwsException(NullPointerException.class).withMessage("null is not allowed"));
        assertThat(settings.getDefaultNullValueProvider(), is(INITIAL_NULL_VALUE_PROVIDER));
    }

    @Test
    void setDefaultNullValueProvider_valid_success()
    {
        assertThat(settings.getDefaultNullValueProvider(), is(INITIAL_NULL_VALUE_PROVIDER));
        settings.setDefaultNullValueProvider(nullValueProvider);
        assertThat(settings.getDefaultNullValueProvider(), is(nullValueProvider));
    }

    @Test
    void setDefaultDataFetchStrategy_null_exceptionAndNoChangePerformed()
    {
        assertThat(settings.getDefaultDataFetchStrategy(), is(INITIAL_DATA_FETCH_STRATEGY));
        assertThat(() -> settings.setDefaultDataFetchStrategy(null), throwsException(NullPointerException.class)
                .withMessageContaining("DataFetchStrategy must not be null"));
        assertThat(settings.getDefaultDataFetchStrategy(), is(INITIAL_DATA_FETCH_STRATEGY));
    }

    @Test
    void setDefaultDataFetchStrategy_valid_success()
    {
        assertThat(settings.getDefaultDataFetchStrategy(), is(INITIAL_DATA_FETCH_STRATEGY));
        settings.setDefaultDataFetchStrategy(dataFetchStrategy);
        assertThat(settings.getDefaultDataFetchStrategy(), is(dataFetchStrategy));
    }

}
