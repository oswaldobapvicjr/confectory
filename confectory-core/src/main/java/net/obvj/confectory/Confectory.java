package net.obvj.confectory;

/**
 * A default {@link ConfigurationContainer}
 * 
 * @author @Dautomne (Fernando Tiannamen)
 * @since 0.1.0
 */
public final class Confectory
{

    /**
     * The initial {@link ConfigurationContainer} to be loaded as default
     */
    protected static ConfigurationContainer DEFAULT_CONFIGURATION_CONTAINER;

    {
        DEFAULT_CONFIGURATION_CONTAINER = new ConfigurationContainer();
    }

    /**
     * Sets an already existent {@link ConfigurationContainer} as Default
     *
     * @param pConfigurationContainer the default {@link ConfigurationContainer} to be set
     */
    public Confectory(ConfigurationContainer pConfigurationContainer)
    {
        DEFAULT_CONFIGURATION_CONTAINER = pConfigurationContainer;
    }

    /**
     * Reset {@link ConfigurationContainer} configuration
     */
    public static void resetContainer()
    {
        DEFAULT_CONFIGURATION_CONTAINER = new ConfigurationContainer();
    }

    /**
     * Retrives the default {@link ConfigurationContainer}
     *
     * @return the default {@link ConfigurationContainer}
     */
    public static ConfigurationContainer container()
    {
        return DEFAULT_CONFIGURATION_CONTAINER;
    }

}