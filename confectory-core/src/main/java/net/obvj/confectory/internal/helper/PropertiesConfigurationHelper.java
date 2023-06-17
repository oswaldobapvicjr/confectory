/*
 * Copyright 2021 obvj.net
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.obvj.confectory.internal.helper;

import java.util.Map.Entry;
import java.util.Objects;
import java.util.Properties;
import java.util.stream.Collectors;

import net.obvj.confectory.ConfigurationException;
import net.obvj.confectory.merger.ConfigurationMerger;
import net.obvj.confectory.merger.PropertiesConfigurationMerger;

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
    public String getAsString()
    {
        return super.bean.entrySet().stream().map(Entry::toString)
                .collect(Collectors.joining("\n"));
    }

    @Override
    public Object get(String key)
    {
        validateKey(key);
        return super.bean.get(key);
    }

    @Override
    public String getString(String key)
    {
        return getValue(key);
    }

    @Override
    public String getMandatoryString(String key)
    {
        String value = getValue(key);
        if (value == null)
        {
            throw new ConfigurationException("No value found for the key: %s", key);
        }
        return value;
    }

    protected String getValue(String key)
    {
        validateKey(key);
        return super.bean.getProperty(key);
    }

    private void validateKey(String key)
    {
        Objects.requireNonNull(key, "The key must not be null");
    }

    @Override
    public ConfigurationMerger<Properties> configurationMerger()
    {
        return new PropertiesConfigurationMerger();
    }
}
