package net.obvj.confectory.source;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import net.obvj.performetrics.Counter;
import net.obvj.performetrics.Stopwatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.obvj.confectory.ConfigurationSourceException;
import net.obvj.confectory.mapper.Mapper;

/**
 * A specialized configuration source implementation for loading a local file resource
 * from the classpath.
 *
 * @author oswaldo.bapvic.jr (Oswaldo Junior)
 */
public class ClasspathFileSource<T> extends AbstractSource<T> implements Source<T>
{
    private static final Logger LOGGER = LoggerFactory.getLogger(ClasspathFileSource.class);

    /**
     * Builds a new configuration source for specific local file resource in the classpath.
     *
     * @param path the classpath file name (or relative path) for this configuration source.
     */
    public ClasspathFileSource(String path)
    {
        super(path);
    }

    @Override
    public T load(Mapper<T> mapper)
    {
        LOGGER.info("Searching file: {}", super.source);

        URL url = ClasspathFileSource.class.getClassLoader().getResource(super.source);
        if (url == null)
        {
            String message = String.format("File not found: %s", super.source);
            LOGGER.warn(message);
            throw new ConfigurationSourceException(message);
        }

        return load(url, mapper);
    }

    /**
     * Gets the contents of the specified URL using default character encoding.
     *
     * @param url    the URL to be loaded
     * @param mapper the {@link Mapper} to be applied on the file input stream
     * @return the string content from the specified URL
     */
    protected T load(URL url, Mapper<T> mapper)
    {
        try (InputStream inputStream = url.openStream())
        {
            LOGGER.info("Loading file {} with mapper: <{}>", super.source, mapper.getClass().getSimpleName());
            Stopwatch stopwatch = Stopwatch.createStarted();
            T mappedObject = mapper.apply(inputStream);
            stopwatch.stop();
            double elapsedTime = stopwatch.elapsedTime(Counter.Type.WALL_CLOCK_TIME, TimeUnit.MILLISECONDS);

            LOGGER.info("File {} loaded successfully", super.source);
            LOGGER.info(String.format("File loaded in %d ms", (int)Math.round(elapsedTime)), super.source);
            return mappedObject;
        }
        catch (IOException exception)
        {
            throw new ConfigurationSourceException(exception, "Unable to load classpath resource: %s", super.source);
        }
    }

}
