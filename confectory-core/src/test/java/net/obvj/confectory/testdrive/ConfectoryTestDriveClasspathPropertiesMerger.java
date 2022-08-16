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
import net.obvj.confectory.mapper.PropertiesMapper;
import net.obvj.confectory.source.StringSource;

public class ConfectoryTestDriveClasspathPropertiesMerger
{
    public static void main(String[] args)
    {
        Configuration<Properties> config1 = Configuration.<Properties>builder()
                .namespace("test").precedence(10)
                .source(new StringSource<>("booleanValue=false\n"
                                         + "stringValue=string1\n"
                                         + "intValue=9\n"))
                .mapper(new PropertiesMapper()).build();

        Configuration<Properties> config2 = Configuration.<Properties>builder()
                .namespace("test").precedence(20)
                .source(new StringSource<>("booleanValue=true\n"
                                         + "stringValue=string2\n"
                                         + "doubleValue=9.88\n"))
                .mapper(new PropertiesMapper()).build();

        Configuration<Properties> config = config1.merge(config2);

        System.out.println(config1);
        System.out.println(config2);
        System.out.println(config);
        System.out.println(config.getBoolean("booleanValue"));
        System.out.println(config.getString("stringValue"));
        System.out.println(config.getInteger("intValue"));
        System.out.println(config.getDouble("doubleValue"));
    }
}
