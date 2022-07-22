/*
 * Copyright 2022 obvj.net
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

package net.obvj.confectory.merger;

import java.util.Properties;

import net.obvj.confectory.Configuration;

/**
 * A specialized {@code ConfigurationMerger} that combines two {@link Configuration}
 * objects of type {@link Properties} into a single one.
 *
 * @see ConfigurationMerger
 * @author oswaldo.bapvic.jr (Oswaldo Junior)
 * @since 2.1.0
 */
public class PropertiesConfigurationMerger extends AbstractConfigurationMerger<Properties>
{

    /**
     * @param mergeOptions not in use for this merger
     */
    @Override
    Properties doMerge(Configuration<Properties> config1, Configuration<Properties> config2,
            MergeOption... mergeOptions)
    {
        Properties properties1 = getPropertiesSafely(config1);
        Properties properties2 = getPropertiesSafely(config2);

        if (properties1.isEmpty()) return properties2;
        if (properties2.isEmpty()) return properties1;

        Properties result = new Properties();

        // First iterate through the first map
        properties1.forEach((key, value) ->
        {
            // The actual value must be the one from the highest-precedence map
            Object actualValue = properties2.containsKey(key)
                    && config2.getPrecedence() > config1.getPrecedence() ? properties2.get(key) : value;
            result.put(key, actualValue);
        });

        // Then iterate through the second map to find additional keys
        properties2.forEach(result::putIfAbsent);
        return result;
    }

    /**
     * Returns the {@link Properties} object associated with the specified
     * {@link Configuration}, or an empty {@link Properties} if the bean is null.
     *
     * @param config the {@link Configuration} which {@link Properties} are to be retrieved
     * @return a {@link Properties} object, not null
     */
    private Properties getPropertiesSafely(Configuration<Properties> config)
    {
        return getBeanSafely(config, Properties::new);
    }

}
