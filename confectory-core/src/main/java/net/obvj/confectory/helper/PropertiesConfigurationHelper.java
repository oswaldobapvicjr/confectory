package net.obvj.confectory.helper;

import java.util.Properties;

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

    @Override
    public String getStringProperty(String key)
    {
        String value = super.bean.getProperty(key);
        return value == null ? nullValueProvider.getStringValue() : value;
    }

}
