package net.obvj.confectory.mapper;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import org.apache.commons.io.IOUtils;

import net.obvj.confectory.ConfigurationMappingException;

/**
 * A specialized {@code Mapper} that loads the contents of an {@link InputStream} as
 * {@code String}, typically for testing/troubleshooting or manual handling purposes.
 *
 * @author oswaldo.bapvic.jr (Oswaldo Junior)
 * @since 0.1.0
 */
public class StringMapper extends AbstractBeanMapper<String> implements Mapper<InputStream, String>
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

}
