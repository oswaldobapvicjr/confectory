package net.obvj.confectory.mapper;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import org.apache.commons.io.IOUtils;

import net.obvj.confectory.ConfigurationMappingException;
import net.obvj.confectory.helper.ConfigurationHelper;
import net.obvj.confectory.helper.UnsupportedConfigurationHelper;

public class StringMapper implements Mapper<InputStream, String>
{

    @Override
    public String apply(InputStream inputStream)
    {
        try
        {
            return IOUtils.toString(inputStream, Charset.defaultCharset());
        }
        catch (IOException exception)
        {
            throw new ConfigurationMappingException("Unable to parse the input as string", exception);
        }
    }

    @Override
    public ConfigurationHelper<String> configurationHelper(String source)
    {
        return new UnsupportedConfigurationHelper<>(source);
    }

}
