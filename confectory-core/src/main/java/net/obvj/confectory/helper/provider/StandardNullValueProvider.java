package net.obvj.confectory.helper.provider;

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

    @Override
    public boolean getBooleanValue()
    {
        return false;
    }

    @Override
    public int getIntValue()
    {
        return 0;
    }

    @Override
    public long getLongValue()
    {
        return 0L;
    }

    @Override
    public double getDoubleValue()
    {
        return 0.0;
    }

    @Override
    public String getStringValue()
    {
        return "";
    }

}
