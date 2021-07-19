package net.obvj.confectory.helper;

import java.util.Optional;

import net.obvj.confectory.ConfigurationException;

/**
 * A Configuration Helper implementation for user-defined beans.
 *
 * @param <T> the configuration bean type
 *
 * @author oswaldo.bapvic.jr (Oswaldo Junior)
 * @since 0.1.0
 */
public class BeanConfigurationHelper<T> extends BasicConfigurationHelper<T>
{
    /**
     * Builds a new Configuration Helper instance with a specific source.
     *
     * @param source the source bean, mainly for exception/reporting purposes
     */
    public BeanConfigurationHelper(T source)
    {
        super(source);
    }

    /**
     * @param key not used since this method implementation is a "no-op"
     * @throws ConfigurationException always, since the data for this type of helper should be
     *                                handled by the user-defined bean
     */
    @Override
    public String getStringProperty(String key)
    {
        throw new ConfigurationException("Operation not supported for bean of type '%s'",
                super.source.getClass().getName());
    }

    /**
     * Returns {@link Optional#empty}, since the data for this type of helper should be
     * handled by the user-defined bean
     *
     * @param key not used since this method implementation is a "no-op"
     * @return {@link Optional#empty}
     */
    @Override
    public Optional<String> getOptionalStringProperty(String key)
    {
        return Optional.empty();
    }

}
