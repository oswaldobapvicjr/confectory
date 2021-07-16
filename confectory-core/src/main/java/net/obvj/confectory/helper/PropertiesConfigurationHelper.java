package net.obvj.confectory.helper;

import java.util.Properties;

public class PropertiesConfigurationHelper extends BasicConfigurationHelper<Properties>
{
    public PropertiesConfigurationHelper(Properties properties)
    {
        super(properties);
    }

    @Override
    public String getStringProperty(String path)
    {
        String value = super.source.getProperty(path);
        return requireNonEmpty(value, () -> String.format("Property not found: '%s'", path));
    }

}
