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

package net.obvj.confectory.testdrive;

import net.obvj.confectory.Configuration;
import net.obvj.confectory.mapper.YAMLToObjectMapper;
import net.obvj.confectory.testdrive.model.Customer;

public class ConfectoryTestDriveYAMLToObject
{
    public static void main(String[] args)
    {
        Configuration<Customer> config = Configuration.<Customer>builder()
                .source("testfiles/person.yaml")
                .mapper(new YAMLToObjectMapper<>(Customer.class)).build();

        System.out.println(config.getBean());
        System.out.println(config.getBean().getFirstName());
        System.out.println(config.getBean().getHeight());
        System.out.println(config.getBean().getContactDetails().get(0).getNumber());
        System.out.println(config.getBean().getContactDetails().get(1).getNumber());
        System.out.println(config.getBean().getHomeAddress().getZip());
    }
}
