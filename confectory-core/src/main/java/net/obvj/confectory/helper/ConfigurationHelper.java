package net.obvj.confectory.helper;

import net.obvj.confectory.ConfigurationDataRetriever;
import net.obvj.confectory.helper.nullvalue.NullValueProvider;

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
    /**
     * Defines a {@link NullValueProvider} for invalid keys.
     * @param provider the {@link NullValueProvider} to set; not null
     * @throws NullPointerException if the specified provider is null
     */
    void setNullValueProvider(NullValueProvider provider);
}
