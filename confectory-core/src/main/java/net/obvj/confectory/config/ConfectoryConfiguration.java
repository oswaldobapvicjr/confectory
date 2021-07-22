package net.obvj.confectory.config;

import java.util.Objects;

import net.obvj.confectory.helper.provider.NullValueProvider;
import net.obvj.confectory.helper.provider.StandardNullValueProvider;

/**
 * An object that contains configuration data that is particular to the {@code Confectory}
 * project.
 *
 * @author oswaldo.bapvic.jr
 * @since 0.1.0
 */
public class ConfectoryConfiguration
{
    /**
     * The initial {@link NullValueProvider} to be applied by default
     */
    protected static final NullValueProvider INITIAL_NULL_VALUE_PROVIDER = StandardNullValueProvider.instance();

    private static final ConfectoryConfiguration INSTANCE = new ConfectoryConfiguration();

    private NullValueProvider defaultNullValueProvider;

    /*
     * Private constructor to hide the default, implicit one
     */
    private ConfectoryConfiguration()
    {
        reset();
    }

    /**
     * Resets {@code Confectory} configuration.
     */
    public void reset()
    {
        defaultNullValueProvider = INITIAL_NULL_VALUE_PROVIDER;
    }

    /**
     * @returns a reference to the the current {@link ConfectoryConfiguration} instance.
     */
    public static ConfectoryConfiguration getInstance()
    {
        return INSTANCE;
    }

    /**
     * Returns the {@link NullValueProvider} to be applied by default when no such provider
     * specified at {@code Configuration} level.
     *
     * @return the default {@code NullValueProvider}
     */
    public NullValueProvider getDefaultNullValueProvider()
    {
        return defaultNullValueProvider;
    }

    /**
     * Defines the {@link NullValueProvider} to be applied by default when no such provider
     * specified at {@code Configuration} level.
     *
     * @param provider the default {@code NullValueProvider} to set; not null
     * @throws NullPointerException if the specified provider is null
     */
    public void setDefaultNullValueProvider(NullValueProvider provider)
    {
        defaultNullValueProvider = Objects.requireNonNull(provider, "null is not allowed");
    }

}
