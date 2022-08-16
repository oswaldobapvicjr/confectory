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
import net.obvj.confectory.mapper.PropertiesToObjectMapper;
import net.obvj.confectory.testdrive.model.MyProperties;

public class ConfectoryTestDriveClasspathPropertiesToObject
{
    public static void main(String[] args)
    {
        Configuration<MyProperties> config = Configuration.<MyProperties>builder()
                .namespace("test")
                .source("testfiles/my-props.properties")
                .mapper(new PropertiesToObjectMapper<>(MyProperties.class))
                .lazy()
                .build();

        MyProperties myProperties = config.getBean();
        System.out.println(myProperties.isEnabled());
        System.out.println(myProperties.getHost());
        System.out.println(myProperties.getPort());
        System.out.println(myProperties.getPrice());
        System.out.println(myProperties.getMyString());
        System.out.println(myProperties.getMyOtherString());

    }
}
