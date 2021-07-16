package net.obvj.confectory.mapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import net.obvj.confectory.ConfigurationMappingException;
import net.obvj.confectory.helper.ConfigurationHelper;
import net.obvj.confectory.helper.PropertiesConfigurationHelper;

public class PropertiesMapper implements Mapper<InputStream, Properties>
{

    @Override
    public Properties apply(InputStream inputStream)
    {
        try
        {
            Properties properties = new Properties();
            properties.load(inputStream);
            return properties;
        }
        catch (IOException exception)
        {
            throw new ConfigurationMappingException("Unable to parse the content of the properties file", exception);
        }
    }


    @Override
    public ConfigurationHelper<Properties> configurationHelper(Properties source)
    {
        return new PropertiesConfigurationHelper(source);
    }

}
