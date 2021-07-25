package net.obvj.confectory.helper;

import java.util.Optional;

/**
 * A basic, abstract Configuration Helper object providing common infrastructure for
 * concrete implementations.
 *
 * @param <T> the source type which configuration data is to be retrieved
 *
 * @author oswaldo.bapvic.jr (Oswaldo Junior)
 * @since 0.1.0
 */
public abstract class BasicConfigurationHelper<T> extends AbstractBasicConfigurationHelper<T>
{
    protected final T bean;

    protected BasicConfigurationHelper(T bean)
    {
        this.bean = bean;
    }

    @Override
    public Optional<T> getBean()
    {
        return Optional.ofNullable(bean);
    }

    @Override
    public boolean getBooleanProperty(String key)
    {
        String value = getStringProperty(key);
        return nullValueProvider.isNull(value) ? nullValueProvider.getBooleanValue() : Boolean.parseBoolean(value);
    }

    @Override
    public int getIntProperty(String key)
    {
        String value = getStringProperty(key);
        return nullValueProvider.isNull(value) ? nullValueProvider.getIntValue() : Integer.parseInt(value);
    }

    @Override
    public long getLongProperty(String key)
    {
        String value = getStringProperty(key);
        return nullValueProvider.isNull(value) ? nullValueProvider.getLongValue() : Long.parseLong(value);
    }

    @Override
    public double getDoubleProperty(String key)
    {
        String value = getStringProperty(key);
        return nullValueProvider.isNull(value) ? nullValueProvider.getDoubleValue() : Double.parseDouble(value);
    }

}
