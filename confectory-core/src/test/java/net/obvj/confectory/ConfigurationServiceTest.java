package net.obvj.confectory;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

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

    private String bean = "test";

    @Test
    void getConfigurationHelper_success_validConfigurationHelper()
    {
        when(mapper.configurationHelper(bean)).thenReturn(helper);
        ConfigurationHelper<String> result = ConfigurationService.getConfigurationHelper(bean, mapper);
        assertThat(result, equalTo(helper));
    }

    @Test
    void getConfigurationHelper_null_nullConfigurationHelper()
    {
        ConfigurationHelper<String> result = ConfigurationService.getConfigurationHelper(null, mapper);
        assertThat(result.getClass(), equalTo(NullConfigurationHelper.class));
    }

}
