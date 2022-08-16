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

import org.json.JSONObject;

import net.obvj.confectory.Configuration;
import net.obvj.confectory.mapper.JsonOrgXMLToJSONObjectMapper;

public class ConfectoryTestDriveXMLToJSONObject
{
    public static void main(String[] args)
    {
        Configuration<JSONObject> config = Configuration.<JSONObject>builder()
                .namespace("test")
                .source("testfiles/agents.xml")
                .mapper(new JsonOrgXMLToJSONObjectMapper()).build();

        System.out.println(config.getBean().toString(2));
        System.out.println(config.getBoolean("conf.enabled"));
        System.out.println(config.getString("**.agent[0].interval"));
        System.out.println(config.getString("$.conf.agents.agent[?(@.class=='Agent2')].interval"));
        System.out.println(config.get("$.conf.agents"));
        System.out.println(config.get("$.conf.agents.**.class"));
    }
}
