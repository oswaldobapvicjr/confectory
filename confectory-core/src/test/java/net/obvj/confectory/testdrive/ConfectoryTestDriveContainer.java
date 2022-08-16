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
import net.obvj.confectory.ConfigurationContainer;
import net.obvj.confectory.mapper.PropertiesMapper;
import net.obvj.confectory.source.StringSource;

public class ConfectoryTestDriveContainer
{
    public static void main(String[] args)
    {
        Configuration<Properties> config1 = Configuration.<Properties>builder()
                .namespace("test")
                .source(new StringSource<>("myFileName=config1\n myString=cust1"))
                .mapper(new PropertiesMapper()).build();

        Configuration<Properties> config2 = Configuration.<Properties>builder()
                .namespace("test")
                .precedence(100)
                .source(new StringSource<>("myFileName=config2\n myLong=20000000200000002\n myString=cust2"))
                .mapper(new PropertiesMapper()).build();

        Configuration<Properties> config3 = Configuration.<Properties>builder()
                .namespace("test")
                .precedence(101)
                .source(new StringSource<>("myFileName=config3\n"))
                .mapper(new PropertiesMapper()).build();

        ConfigurationContainer container = new ConfigurationContainer(config1, config2, config3);

        System.out.println(container.getString("test", "myFileName"));
        System.out.println(container.getString("test", "myString"));
        System.out.println(container.getLong("test", "myLong"));
        System.out.println(container.getLong("test", "myLong2"));

        System.out.println("*****");

        Configuration<Properties> config4 = Configuration.<Properties>builder()
                .namespace("test")
                .precedence(102)
                .source(new StringSource<>("myString=cust4\n"))
                .mapper(new PropertiesMapper()).build();

        container.add(config4);

        System.out.println(container.getString("test", "myFileName"));
        System.out.println(container.getString("test", "myString"));
        System.out.println(container.getLong("test", "myLong"));
        System.out.println(container.getLong("test", "myLong2"));

        System.out.println("*****");

        container.clear();

        System.out.println(container.getString("test", "myFileName"));
        System.out.println(container.getString("test", "myString"));
        System.out.println(container.getLong("test", "myLong"));
        System.out.println(container.getLong("test", "myLong2"));
    }
}
