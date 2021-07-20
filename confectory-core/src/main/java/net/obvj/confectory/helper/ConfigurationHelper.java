package net.obvj.confectory.helper;

import net.obvj.confectory.ConfigurationDataRetriever;

/**
 * A base interface for Configuration Helper objects providing methods for retrieving
 * configuration data from different source types.
 *
 * @param <T> the source type which configuration data is to be retrieved
 *
 * @author oswaldo.bapvic.jr (Oswaldo Junior)
 * @since 0.1.0
 */
public interface ConfigurationHelper<T> extends ConfigurationDataRetriever<T>
{
    // Marker interface
}
