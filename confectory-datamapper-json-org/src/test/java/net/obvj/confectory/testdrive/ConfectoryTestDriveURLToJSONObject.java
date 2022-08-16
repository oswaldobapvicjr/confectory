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
import net.obvj.confectory.mapper.JsonOrgJSONObjectMapper;

public class ConfectoryTestDriveURLToJSONObject
{
    public static void main(String[] args)
    {
        Configuration<JSONObject> config = Configuration.<JSONObject>builder()
                .namespace("test")
                .source("http://ip.jsontest.com")
                .mapper(new JsonOrgJSONObjectMapper())
                .build();

        System.out.println(config.getBean().toString(2));
        System.out.println(config.getString("$.ip"));
    }
}
