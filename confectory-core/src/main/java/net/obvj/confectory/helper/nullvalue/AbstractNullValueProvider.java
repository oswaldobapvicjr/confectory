package net.obvj.confectory.helper.nullvalue;

/**
 * An abstract {@link NullValueProvider} with common methods implemented.
 *
 * @author oswaldo.bapvic.jr (Oswaldo Junior)
 * @since 0.1.0
 */
public abstract class AbstractNullValueProvider implements NullValueProvider
{

    @Override
    public boolean isNull(boolean value)
    {
        return value == getBooleanValue();
    }

    @Override
    public boolean isNull(int value)
    {
        return value == getIntValue();
    }

    @Override
    public boolean isNull(long value)
    {
        return value == getLongValue();
    }

    @Override
    public boolean isNull(double value)
    {
        return value == getDoubleValue();
    }

    @Override
    public boolean isNull(String string)
    {
        return getStringValue().equals(string);
    }

}
