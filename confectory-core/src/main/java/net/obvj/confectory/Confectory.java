package net.obvj.confectory;

/**
 * A Facade for common operations in the {@code Confectory} project.
 * 
 * @author FernandoNSC (Fernando Tiannamen)
 * @since 0.1.0
 */
public final class Confectory
{

    /**
     * The current {@link ConfigurationContainer} to be loaded as default
     */
    private static ConfigurationContainer globalConfigurationContainer = new ConfigurationContainer();

    private Confectory()
    {
        throw new IllegalStateException("Instantiation not allowed");
    }

    /**
     * Retrives the default {@link ConfigurationContainer}
     *
     * @return the default {@link ConfigurationContainer}
     */
    public static ConfigurationContainer container()
    {
        return globalConfigurationContainer;
    }

    /**
     * Sets an already existent {@link ConfigurationContainer} as Default
     *
     * @param configurationContainer the default {@link ConfigurationContainer} to be set
     */
    public static void setDefaultContainer(ConfigurationContainer configurationContainer)
    {
        globalConfigurationContainer = configurationContainer;
    }

}
