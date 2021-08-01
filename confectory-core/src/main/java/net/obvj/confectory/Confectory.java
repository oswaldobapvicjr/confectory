package net.obvj.confectory;

import java.util.Objects;

import net.obvj.confectory.settings.ConfectorySettings;

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
     * The global settings for the {@code Confectory} project.
     *
     * @return a referece to the {@link ConfectorySettings} instance
     */
    public static ConfectorySettings settings()
    {
        return ConfectorySettings.getInstance();
    }

    /**
     * Sets an already existent {@link ConfigurationContainer} as Default
     *
     * @param configurationContainer the default {@link ConfigurationContainer} to
     *                               be set
     * 
     * @throws NullPointerException if the {@code configurationContainer} parameters
     *                              is missing
     */
    public static void setDefaultContainer(ConfigurationContainer configurationContainer)
    {
        Objects.requireNonNull(configurationContainer, "The Configuration Container must not be null.");
        globalConfigurationContainer = configurationContainer;
    }

}
