package net.obvj.confectory.helper;

import java.util.Optional;

/**
 * A "no-op" Configuration Helper object for situations where an optional
 * {@code Configuration} object is not available.
 *
 * @param <T> the source type which configuration data is to be retrieved
 *
 * @author oswaldo.bapvic.jr (Oswaldo Junior)
 * @since 0.1.0
 */
public class NullConfigurationHelper<T> extends AbstractBasicConfigurationHelper<T>
{

    /**
     * @return {@link Optional#empty()}, always
     */
    @Override
    public Optional<T> getBean()
    {
        return Optional.empty();
    }

    /**
     * @return the smart-null value for {@code boolean}, always
     */
    @Override
    public boolean getBooleanProperty(String key)
    {
        return nullValueProvider.getBooleanValue();
    }

    /**
     * @return the smart-null value for {@code int}, always
     */
    @Override
    public int getIntProperty(String key)
    {
        return nullValueProvider.getIntValue();
    }

    /**
     * @return the smart-null value for {@code long}, always
     */
    @Override
    public long getLongProperty(String key)
    {
        return nullValueProvider.getLongValue();
    }

    /**
     * @return the smart-null value for {@code double}, always
     */
    @Override
    public double getDoubleProperty(String key)
    {
        return nullValueProvider.getDoubleValue();
    }

    /**
     * @return the smart-null value for {@code String}, always
     */
    @Override
    public String getStringProperty(String key)
    {
        return nullValueProvider.getStringValue();
    }

}
