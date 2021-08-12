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

package net.obvj.confectory.mapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import net.obvj.confectory.helper.ConfigurationHelper;
import net.obvj.confectory.helper.PropertiesConfigurationHelper;

/**
 * A specialized {@code Mapper} that loads {@link Properties} from an {@link InputStream}.
 *
 * @author oswaldo.bapvic.jr (Oswaldo Junior)
 * @since 0.1.0
 */
public class PropertiesMapper implements Mapper<Properties>
{

    @Override
    public Properties apply(InputStream inputStream) throws IOException
    {
        Properties properties = new Properties();
        properties.load(inputStream);
        return properties;
    }

    @Override
    public ConfigurationHelper<Properties> configurationHelper(Properties properties)
    {
        return new PropertiesConfigurationHelper(properties);
    }

}
