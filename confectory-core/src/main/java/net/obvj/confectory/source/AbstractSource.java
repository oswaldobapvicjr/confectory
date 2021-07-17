package net.obvj.confectory.source;

import java.io.InputStream;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.obvj.confectory.mapper.Mapper;

/**
 * An abstract configuration source, with common infrastructure.
 *
 * @param <T> the configuration data type returned by this {@code Source}
 *
 * @author oswaldo.bapvic.jr (Oswaldo Junior)
 */
public abstract class AbstractSource<T> implements Source<T>
{
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractSource.class);

    protected final String path;

    /**
     * Builds a new configuration source with the specified path.
     *
     * @param path the path of the the configuration source to be loaded
     */
    protected AbstractSource(String path)
    {
        this.path = path;
    }

    @Override
    public Optional<T> loadOptionally(Mapper<InputStream, T> mapper)
    {
        try
        {
            T content = load(mapper);
            return Optional.ofNullable(content);
        }
        catch (Exception exception)
        {
            LOGGER.warn("Unable to load optional source: {}", this);
            LOGGER.debug("Exception details:", exception);
            return Optional.empty();
        }
    }

    @Override
    public String toString()
    {
        return new StringBuilder().append(getClass().getSimpleName()).append("(").append(path).append(")").toString();
    }

}
