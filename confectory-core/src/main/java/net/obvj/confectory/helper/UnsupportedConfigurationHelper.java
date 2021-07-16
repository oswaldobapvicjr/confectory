package net.obvj.confectory.helper;

import java.util.Optional;

import net.obvj.confectory.ConfigurationException;

/**
 * A "no-op" Configuration Helper implementation.
 *
 * @param <T> the source type, for exception/reporting purposes
 *
 * @author oswaldo.bapvic.jr (Oswaldo Junior)
 * @since 0.1.0
 */
public class UnsupportedConfigurationHelper<T> extends BasicConfigurationHelper<T>
{
    /**
     * Builds a new Configuration Helper instance with a specific source.
     *
     * @param source the source bean, mainly for exception/reporting purposes
     */
    public UnsupportedConfigurationHelper(T source)
    {
        super(source);
    }

    /**
     * @throws ConfigurationException always
     */
    @Override
    public String getStringProperty(String key)
    {
        throw new ConfigurationException("Operation not supported for bean of type '%s'",
                super.source.getClass().getName());
    }

    /**
     * Returns {@link Optional#empty}, always.
     *
     * @param key not used since this implementation is a "no-op"
     * @return {@link Optional#empty}
     */
    @Override
    public Optional<String> getOptionalStringProperty(String key)
    {
        return Optional.empty();
    }

}
