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

import net.obvj.confectory.Configuration;
import net.obvj.confectory.DataFetchStrategy;
import net.obvj.confectory.TypeSafeConfigurationContainer;
import net.obvj.confectory.mapper.PropertiesToObjectMapper;
import net.obvj.confectory.testdrive.model.MyProperties;

public class ConfectoryTestDriveTypeSafeContainer
{
    public static void main(String[] args)
    {
        Configuration<MyProperties> config1 = Configuration.<MyProperties>builder()
                .source("classpath://testfiles/my-props.properties")
                .mapper(new PropertiesToObjectMapper<>(MyProperties.class))
                .namespace("my-props1")
                .precedence(100)
                .build();

        Configuration<MyProperties> config2 = Configuration.<MyProperties>builder()
                .source("classpath://testfiles/my-props2.properties")
                .mapper(new PropertiesToObjectMapper<>(MyProperties.class))
                .namespace("my-props2")
                .precedence(50)
                .build();

        Configuration<MyProperties> config3 = Configuration.<MyProperties>builder()
                .source("classpath://testfiles/notfound.properties") // Non-existent
                .mapper(new PropertiesToObjectMapper<>(MyProperties.class))
                .namespace("not-found")
                .precedence(300)
                .optional()
                .build();

        TypeSafeConfigurationContainer<MyProperties> container = new TypeSafeConfigurationContainer<>(
                config1, config2, config3);


        System.out.println();
        System.out.println("config1            ==> " + config1.getBean());
        System.out.println("config2            ==> " + config2.getBean());
        System.out.println("config3            ==> " + config3.getBean());
        System.out.println("getBean()          ==> " + container.getBean());
        System.out.println("getBean(my-props1) ==> " + container.getBean("my-props1"));
        System.out.println("getBean(my-props2) ==> " + container.getBean("my-props2"));
        System.out.println("getBean(my-props2) ==> " + container.getBean("my-props3"));

    }
}
