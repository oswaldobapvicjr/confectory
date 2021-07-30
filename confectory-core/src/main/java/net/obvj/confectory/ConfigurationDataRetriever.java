package net.obvj.confectory;

import java.util.Optional;

/**
 * A base interface for objects that retrieve configuration data.
 *
 * @param <T> the target type which configuration data is to be retrieved
 *
 * @author oswaldo.bapvic.jr (Oswaldo Junior)
 * @since 0.1.0
 */
public interface ConfigurationDataRetriever<T>
{

    /**
     * Returns the target configuration object used by this data retriever, typically for
     * manual handling and/or troubleshooting purposes.
     *
     * @return an {@link Optional}, possibly containing the target configuration object
     */
    Optional<T> getBean();

    /**
     * Returns the {@code boolean} value associated with the specified {@code key}.
     *
     * @param key the property key
     * @return the {@code boolean} value associated with the specified {@code key}
     */
    boolean getBooleanProperty(String key);

    /**
     * Returns the {@code int} value associated with the specified {@code key}.
     *
     * @param key the property key
     * @return the {@code int} value associated with the specified {@code key}
     * @throws NumberFormatException if the value is not a parsable {@code int}.
     */
    int getIntProperty(String key);

    /**
     * Returns the {@code long} value associated with the specified {@code key}.
     *
     * @param key the property key
     * @return the {@code long} value associated with the specified {@code key}
     * @throws NumberFormatException if the value is not a parsable {@code long}.
     */
    long getLongProperty(String key);

    /**
     * Returns the {@code double} value associated with the specified {@code key}.
     *
     * @param key the property key
     * @return the {@code double} value associated with the specified {@code key}
     * @throws NumberFormatException if the value is not a parsable {@code double}.
     */
    double getDoubleProperty(String key);

    /**
     * Returns the {@code String} value associated with the specified {@code key}.
     *
     * @param key the property key
     * @return the {@code String} value associated with the specified {@code key}
     */
    String getStringProperty(String key);

}
