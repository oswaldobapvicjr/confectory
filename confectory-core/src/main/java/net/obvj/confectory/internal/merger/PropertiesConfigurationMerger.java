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

package net.obvj.confectory.internal.merger;

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

    @Override
    Properties doMerge(Configuration<Properties> config1, Configuration<Properties> config2)
    {
        Properties config1Map = config1.getBean();
        Properties config2Map = config2.getBean();

        if (isEmpty(config1Map)) return config2Map;
        if (isEmpty(config2Map)) return config1Map;

        Properties result = new Properties();

        // First iterate through the first map
        config1Map.forEach((key, value) ->
        {
            // The actual value must be the one from the highest-precedence map
            Object actualValue = config2Map.containsKey(key)
                    && config2.getPrecedence() > config1.getPrecedence() ? config2Map.get(key) : value;
            result.put(key, actualValue);
        });

        // Then iterate through the second map to find additional keys
        config2Map.forEach(result::putIfAbsent);
        return result;
    }

    private boolean isEmpty(Properties properties)
    {
        return properties == null || properties.isEmpty();
    }

}
