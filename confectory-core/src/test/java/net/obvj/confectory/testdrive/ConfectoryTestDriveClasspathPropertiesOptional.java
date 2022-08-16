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

package net.obvj.confectory.testdrive;

import java.util.Properties;

import net.obvj.confectory.Configuration;
import net.obvj.confectory.ConfigurationBuilder;
import net.obvj.confectory.mapper.PropertiesMapper;
import net.obvj.confectory.source.SourceFactory;

public class ConfectoryTestDriveClasspathPropertiesOptional
{
    public static void main(String[] args)
    {
        Configuration<Properties> config = new ConfigurationBuilder<Properties>()
                .namespace("test")
                .source(SourceFactory.classpathFileSource("testfiles/nofile.properties"))
                .mapper(new PropertiesMapper())
                .optional()
                .build();

        System.out.println(config.getBean());
        System.out.println(config.getBoolean("web.enable"));
        System.out.println(config.getString("web.host"));
        System.out.println(config.getInteger("web.port"));
        System.out.println(config.getDouble("web.price"));
        System.out.println(config.getInteger("web.test"));

    }
}
