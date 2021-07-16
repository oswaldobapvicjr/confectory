package net.obvj.confectory.source;

import java.io.InputStream;
import java.util.Optional;

import net.obvj.confectory.mapper.Mapper;

/**
 * The base interface for a configuration source.
 *
 * @param <T> the configuration data type returned by this {@code Source}
 *
 * @author oswaldo.bapvic.jr (Oswaldo Junior)
 */
public interface Source<T>
{

    /**
     * Applies a specific configuration loading strategy and returns the retrieved data.
     *
     * @param mapper the {@link Mapper} to be applied on the source input stream
     * @return the loaded configuration data
     */
    T load(Mapper<InputStream, T> mapper);

    /**
     * Applies a specific configuration loading strategy and returns an {@link Optional},
     * possibly containing the retrieved data, or {@link Optional#empty()} in the occurrence
     * of an exception.
     *
     * @return the loaded configuration data, or an empty object if unable to load the source
     */
    Optional<T> loadQuietly(Mapper<InputStream, T> mapper);

}
