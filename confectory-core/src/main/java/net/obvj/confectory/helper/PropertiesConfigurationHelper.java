package net.obvj.confectory.helper;

import java.util.Optional;
import java.util.Properties;

import net.obvj.confectory.ConfigurationException;

/**
 * A specialized Configuration Helper that retrieves data from a {@link Properties}
 * object.
 *
 * @author oswaldo.bapvic.jr (Oswaldo Junior)
 * @since 0.1.0
 */
public class PropertiesConfigurationHelper extends BasicConfigurationHelper<Properties>
{
    /**
     * Builds a new dedicated instance from a specific {@link Properties} object.
     *
     * @param properties the {@link Properties} to be maintained by this helper
     */
    public PropertiesConfigurationHelper(Properties properties)
    {
        super(properties);
    }

    /**
     * @throws ConfigurationException if no property found with the specified key
     */
    @Override
    public String getStringProperty(String key)
    {
        Optional<String> value = getOptionalStringProperty(key);
        if (value.isPresent())
        {
            return value.get();
        }
        throw new ConfigurationException("Property not found: '%s", key);
    }

    @Override
    public Optional<String> getOptionalStringProperty(String key)
    {
        return Optional.ofNullable(super.source.getProperty(key));
    }

}
