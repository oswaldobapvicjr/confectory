package net.obvj.confectory;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import net.obvj.confectory.helper.ConfigurationHelper;
import net.obvj.confectory.helper.NullConfigurationHelper;
import net.obvj.confectory.mapper.Mapper;
import net.obvj.confectory.source.Source;

/**
 * Unit tests for the {@link ConfigurationService} class.
 *
 * @author oswaldo.bapvic.jr (Oswaldo Junior)
 * @since 0.4.0
 */
@ExtendWith(MockitoExtension.class)
class ConfigurationServiceTest
{

    @Mock
    private Source<String> source;
    @Mock
    private Mapper<String> mapper;
    @Mock
    private ConfigurationHelper<String> helper;

    private Optional<String> bean = Optional.of("test");

    @Test
    void getConfigurationHelper_success_validConfigurationHelper()
    {
        when(mapper.configurationHelper(bean.get())).thenReturn(helper);
        ConfigurationHelper<String> result = ConfigurationService.getConfigurationHelper(bean, mapper);
        assertThat(result, equalTo(helper));
    }

    @Test
    void getConfigurationHelper_empty_nullConfigurationHelper()
    {
        ConfigurationHelper<String> result = ConfigurationService.getConfigurationHelper(Optional.empty(), mapper);
        assertThat(result.getClass(), equalTo(NullConfigurationHelper.class));
    }

}
