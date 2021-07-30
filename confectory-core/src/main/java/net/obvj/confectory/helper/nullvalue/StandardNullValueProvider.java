package net.obvj.confectory.helper.nullvalue;

/**
 * An object that provides "smart-nulls" for Java types (specially primitive ones) when a
 * configuration key is not found.
 *
 * @author oswaldo.bapvic.jr (Oswaldo Junior)
 * @since 0.1.0
 */
public class StandardNullValueProvider extends AbstractNullValueProvider implements NullValueProvider
{
    private static final NullValueProvider INSTANCE = new StandardNullValueProvider();

    /**
     * Returns pre-built, shared instance for this {@code NullValueProvider}.
     *
     * @return a {@code StandardNullValueProvider} instance
     */
    public static NullValueProvider instance()
    {
        return INSTANCE;
    }

    /**
     * Returns {@code false}.
     */
    @Override
    public boolean getBooleanValue()
    {
        return false;
    }

    /**
     * Returns {@code 0}.
     */
    @Override
    public int getIntValue()
    {
        return 0;
    }

    /**
     * Returns {@code 0L}.
     */
    @Override
    public long getLongValue()
    {
        return 0L;
    }

    /**
     * Returns {@code 0.0}.
     */
    @Override
    public double getDoubleValue()
    {
        return 0.0;
    }

    /**
     * Returns an empty {@code String}.
     */
    @Override
    public String getStringValue()
    {
        return "";
    }

}
