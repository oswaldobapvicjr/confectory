package net.obvj.confectory.helper;

import java.util.Optional;

/**
 * An abstract Configuration Helper objects providing common infrastructure for concrete
 * implementations.
 *
 * @param <T> the source type which configuration data is to be retrieved
 *
 * @author oswaldo.bapvic.jr (Oswaldo Junior)
 * @since 0.1.0
 */
public abstract class BasicConfigurationHelper<T> implements ConfigurationHelper<T>
{
    protected final T source;

    protected BasicConfigurationHelper(T source)
    {
        this.source = source;
    }

    @Override
    public Optional<T> getBean()
    {
        return Optional.ofNullable(source);
    }

    @Override
    public boolean getBooleanProperty(String key)
    {
        return Boolean.parseBoolean(getStringProperty(key));
    }

    @Override
    public int getIntProperty(String key)
    {
        return Integer.parseInt(getStringProperty(key));
    }

    @Override
    public long getLongProperty(String key)
    {
        return Long.parseLong(getStringProperty(key));
    }

    @Override
    public double getDoubleProperty(String key)
    {
        return Double.parseDouble(getStringProperty(key));
    }

}
