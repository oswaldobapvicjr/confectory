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
import net.obvj.confectory.DataFetchStrategy;
import net.obvj.confectory.mapper.PropertiesMapper;
import net.obvj.confectory.settings.ConfectorySettings;
import net.obvj.confectory.source.StringSource;

public class ConfectoryTestDriveContainerLenient
{
    public static void main(String[] args)
    {
        ConfectorySettings.getInstance().setDefaultDataFetchStrategy(DataFetchStrategy.LENIENT);

        Configuration<Properties> config1 = Configuration.<Properties>builder()
                .namespace("test")
                .source(new StringSource<>("myFileName=config1\n myString=cust1"))
                .mapper(new PropertiesMapper()).build();

        Configuration<Properties> config2 = Configuration.<Properties>builder()
                .namespace("test1")
                .precedence(100)
                .source(new StringSource<>("myFileName=config2\n myLong=20000000200000002\n myString=cust2"))
                .mapper(new PropertiesMapper()).build();

        Configuration<Properties> config3 = Configuration.<Properties>builder()
                .namespace("test2")
                .precedence(101)
                .source(new StringSource<>("myFileName=config3\n"))
                .mapper(new PropertiesMapper()).build();

        ConfigurationContainer container = new ConfigurationContainer(config1, config2, config3);

        System.out.println(container.getString("myFileName"));
        System.out.println(container.getString("myString"));
        System.out.println(container.getLong("myLong"));
        System.out.println(container.getLong("myLong2"));

    }
}
