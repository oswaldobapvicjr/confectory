package net.obvj.confectory.helper;

import net.obvj.confectory.util.Exceptions;

public class UnsupportedConfigurationHelper<T> extends BasicConfigurationHelper<T>
{
    public UnsupportedConfigurationHelper(T source)
    {
        super(source);
    }

    @Override
    public String getStringProperty(String path)
    {
        throw Exceptions.configuration("Operation not supported for configuration of type '%s'",
                source.getClass().getName());
    }

}
