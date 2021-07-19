package net.obvj.confectory.source;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

import net.obvj.confectory.ConfigurationSourceException;
import net.obvj.confectory.mapper.Mapper;

/**
 * A specialized {@code Source} that loads the contents of a {@code String}.
 *
 * @author oswaldo.bapvic.jr (Oswaldo Junior)
 * @since 0.1.0
 */
public class StringSource<T> extends AbstractSource<T> implements Source<T>
{
    /**
     * Builds a new configuration source from the specified string.
     *
     * @param source the string to be loaded.
     */
    public StringSource(String source)
    {
        super(Objects.requireNonNull(source, "The source string must not be null"));
    }

    @Override
    public T load(Mapper<InputStream, T> mapper)
    {
        try
        {
            InputStream inputStream = new ByteArrayInputStream(super.source.getBytes());
            return load(inputStream, mapper);
        }
        catch (IOException exception)
        {
            throw new ConfigurationSourceException(exception, "Unable to load string: \"%s\"", super.source);
        }
    }

}
