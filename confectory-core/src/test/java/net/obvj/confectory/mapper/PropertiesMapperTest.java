package net.obvj.confectory.mapper;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Properties;

import org.junit.jupiter.api.Test;

import net.obvj.confectory.helper.PropertiesConfigurationHelper;

/**
 * Unit tests for the {@link PropertiesMapper} class.
 *
 * @author oswaldo.bapvic.jr (Oswaldo Junior)
 * @since 0.1.0
 */
class PropertiesMapperTest
{
    private static final String TEST_PROPERTIES_CONTENT = "web.host=localhost\nweb.port=1910";

    private PropertiesMapper mapper = new PropertiesMapper();

    @Test
    void apply_validInputStream_loadedSuccessfully() throws IOException
    {
        Properties properties = mapper.apply(new ByteArrayInputStream(TEST_PROPERTIES_CONTENT.getBytes()));
        assertThat(properties.getProperty("web.host"), equalTo("localhost"));
        assertThat(properties.getProperty("web.port"), equalTo("1910"));
    }

    @Test
    void configurationHelper_propertiesConfigurationHelper()
    {
        assertThat(mapper.configurationHelper(new Properties()).getClass(),
                equalTo(PropertiesConfigurationHelper.class));
    }
}
