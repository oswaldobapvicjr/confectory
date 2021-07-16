package net.obvj.confectory.helper;

import java.util.Optional;
import java.util.function.Supplier;

import net.obvj.confectory.ConfigurationException;

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
    public boolean getBooleanProperty(String path)
    {
        return Boolean.parseBoolean(getStringProperty(path));
    }

    @Override
    public int getIntProperty(String path)
    {
        return Integer.parseInt(getStringProperty(path));
    }

    @Override
    public long getLongProperty(String path)
    {
        return Long.parseLong(getStringProperty(path));
    }

    @Override
    public float getFloatProperty(String path)
    {
        return Float.parseFloat(getStringProperty(path));
    }

    @Override
    public double getDoubleProperty(String path)
    {
        return Double.parseDouble(getStringProperty(path));
    }

    /**
     * Checks that the specified object is not {@code null} and throws a customized
     * {@link ConfigurationException} if it is. This method is designed primarily for doing
     * parameter validation as demonstrated below:
     *
     * <blockquote>
     *
     * <pre>
     * public Foo(String bar)
     * {
     *     this.bar = BasicConfiguration.requireNonNull(bar, "bar must not be null");
     * }
     * </pre>
     *
     * </blockquote>
     *
     * @param object  the string to check for nullity/emptiness
     * @param message detail message to be used in the event that a {@code
     *                IllegalArgumentException} is thrown
     * @return {@code string} if not {@code null}
     * @throws ConfigurationException if {@code string} is {@code null}
     */
    public static String requireNonEmpty(String string, Supplier<String> message)
    {
        if (isEmpty(string))
        {
            throw new ConfigurationException(message.get());
        }
        return string;
    }

    /**
     * Checks if a String is empty ("") or {@code null}.
     * <p>
     * Examples:
     *
     * <pre>
     * BasicConfiguration.isEmpty(null)  = true
     * BasicConfiguration.isEmpty("")    = true
     * BasicConfiguration.isEmpty(" ")   = false
     * BasicConfiguration.isEmpty("bob") = false
     * </pre>
     *
     * @param string the string to be checked; may be null
     * @return {@code true} if the string is empty or null
     */
    public static boolean isEmpty(String string)
    {
        return string == null || string.isEmpty();
    }
}
