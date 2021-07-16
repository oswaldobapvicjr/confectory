package net.obvj.confectory.source;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

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
    public T load(Mapper<InputStream, T> mapper)
    {
        URL url = ClasspathFileSource.class.getClassLoader().getResource(super.path);
        if (url == null)
        {
            throw new ConfigurationSourceException("Classpath resource not found: %s", super.path);
        }
        return load(url, mapper);
    }

    /**
     * Gets the contents of the specified URL as String using default character encoding.
     *
     * @param url the URL to be loaded
     * @return the string content from the specified URL
     */
    private T load(URL url, Mapper<InputStream, T> mapper)
    {
        try (InputStream inputStream = url.openStream())
        {
            return mapper.apply(inputStream);
        }
        catch (IOException exception)
        {
            throw new ConfigurationSourceException(exception, "Unable to load classpath resource: %s", super.path);
        }
    }

}
