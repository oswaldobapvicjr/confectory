package net.obvj.confectory.helper;

import java.util.Optional;

/**
 * A "no-op" Configuration Helper object for situations where an optional configuration
 * object is not available.
 *
 * @param <T> the source type which configuration data is to be retrieved
 *
 * @author oswaldo.bapvic.jr (Oswaldo Junior)
 * @since 0.1.0
 */
public class NullConfigurationHelper<T> implements ConfigurationHelper<T>
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
     * @return {@code false}, always
     */
    @Override
    public boolean getBooleanProperty(String key)
    {
        return false;
    }

    /**
     * @return {@code 0}, always
     */
    @Override
    public int getIntProperty(String key)
    {
        return 0;
    }

    /**
     * @return {@code 0L}, always
     */
    @Override
    public long getLongProperty(String key)
    {
        return 0L;
    }

    /**
     * @return {@code 0D}, always
     */
    @Override
    public double getDoubleProperty(String key)
    {
        return 0.0;
    }

    /**
     * @return an empty string ({@code ""}), always
     */
    @Override
    public String getStringProperty(String key)
    {
        return "";
    }

    /**
     * @return {@link Optional#empty()}, always
     */
    @Override
    public Optional<String> getOptionalStringProperty(String key)
    {
        return Optional.empty();
    }

}
