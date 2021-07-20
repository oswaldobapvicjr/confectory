package net.obvj.confectory.mapper;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import org.apache.commons.io.IOUtils;

/**
 * A specialized {@code Mapper} that loads the contents of an {@link InputStream} as
 * {@code String}, typically for testing/troubleshooting or manual handling purposes.
 *
 * @author oswaldo.bapvic.jr (Oswaldo Junior)
 * @since 0.1.0
 */
public class StringMapper extends AbstractBeanMapper<String> implements Mapper<String>
{

    @Override
    public String apply(InputStream inputStream) throws IOException
    {
        return IOUtils.toString(inputStream, Charset.defaultCharset());
    }

}
