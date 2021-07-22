package net.obvj.confectory.mapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import net.obvj.confectory.helper.ConfigurationHelper;
import net.obvj.confectory.helper.PropertiesConfigurationHelper;

/**
 * A specialized {@code Mapper} that loads {@link Properties} from an {@link InputStream}.
 *
 * @author oswaldo.bapvic.jr (Oswaldo Junior)
 * @since 0.1.0
 */
public class PropertiesMapper implements Mapper<Properties>
{

    @Override
    public Properties apply(InputStream inputStream) throws IOException
    {
        Properties properties = new Properties();
        properties.load(inputStream);
        return properties;
    }

    @Override
    public ConfigurationHelper<Properties> configurationHelper(Properties properties)
    {
        return new PropertiesConfigurationHelper(properties);
    }

}
