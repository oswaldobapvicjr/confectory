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
import net.obvj.confectory.mapper.GsonJsonToObjectMapper;
import net.obvj.confectory.testdrive.model.MyBean;

public class ConfectoryTestDriveGsonJsonToObject
{
    public static void main(String[] args)
    {
        Configuration<MyBean> config = Configuration.<MyBean>builder()
                .namespace("test")
                .source("testfiles/agents.json")
                .mapper(new GsonJsonToObjectMapper<>(MyBean.class))
                .build();

        MyBean myBean = config.getBean();
        System.out.println(myBean);
        System.out.println(myBean.isEnabled());
        System.out.println(myBean.getAgents().get(0).getClazz());
        System.out.println(myBean.getAgents().get(0).getInterval());
        System.out.println(myBean.getAgents().get(1).getClazz());
        System.out.println(myBean.getAgents().get(1).getInterval());
    }
}
