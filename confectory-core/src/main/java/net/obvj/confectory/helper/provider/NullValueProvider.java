package net.obvj.confectory.helper.provider;

/**
 * An object that provides "logic-nulls" for Java types (especially primitive ones) when a
 * configuration key is not found.
 *
 * @author oswaldo.bapvic.jr (Oswaldo Junior)
 * @since 0.1.0
 */
public interface NullValueProvider
{
    /**
     * Checks if the {@code boolean} is equal to {@link NullValueProvider#getBooleanValue()}.
     *
     * @param value the value to be checked
     * @return {@code true} if the specified {@code boolean} is null
     */
    boolean isNull(boolean value);

    /**
     * Checks if the {@code int} is equal to {@link NullValueProvider#getIntValue()}.
     *
     * @param value the value to be checked
     * @return {@code true} if the specified {@code int} is considered null
     */
    boolean isNull(int value);

    /**
     * Checks if the {@code long} is equal to {@link NullValueProvider#getLongValue()}.
     *
     * @param value the value to be checked
     * @return {@code true} if the specified {@code long} is considered null
     */
    boolean isNull(long value);

    /**
     * Checks if the {@code double} is equal to {@link NullValueProvider#getDoubleValue()}.
     *
     * @param value the value to be checked
     * @return {@code true} if the specified {@code long} is null
     */
    boolean isNull(double value);

    /**
     * Checks if the {@code String} is equal to {@link NullValueProvider#getStringValue()}.
     *
     * @param string the string to be checked
     * @return {@code true} if the specified {@code String} is considered null
     */
    boolean isNull(String string);

    /**
     * @return a custom "logic-null" value for {@code boolean}
     */
    boolean getBooleanValue();

    /**
     * @return a custom "logic-null" value for {@code int}
     */
    int getIntValue();

    /**
     * @return a custom "logic-null" value for {@code long}
     */
    long getLongValue();

    /**
     * @return a custom "logic-null" value for {@code double}
     */
    double getDoubleValue();

    /**
     * @return a custom "logic-null" value for {@code String}
     */
    String getStringValue();

}
