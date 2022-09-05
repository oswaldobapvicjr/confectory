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

import static net.obvj.jsonmerge.JsonMergeOption.onPath;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import net.obvj.confectory.Configuration;
import net.obvj.confectory.mapper.GsonJsonObjectMapper;

public class ConfectoryTestDriveGsonMergeTwoConfigs
{
    public static void main(String[] args)
    {
        Configuration<JsonObject> config1 = Configuration.<JsonObject>builder()
                .source("testfiles/agents.json")
                .precedence(100)
                .mapper(new GsonJsonObjectMapper())
                .build();

        Configuration<JsonObject> config2 = Configuration.<JsonObject>builder()
                .source("testfiles/agents2.json")
                .precedence(99)
                .mapper(new GsonJsonObjectMapper())
                .build();

        Configuration<JsonObject> config = config1.merge(config2,
                onPath("$.agents").findObjectsIdentifiedBy("class")
                        .thenPickTheHighestPrecedenceOne());

        System.out.println(new GsonBuilder().setPrettyPrinting().create().toJson(config.getBean()));
        System.out.println(config.getBoolean("enabled"));
        System.out.println(config.get("$.agents[?(@.class=='Agent1')]"));
        System.out.println(config.get("$.agents[?(@.class=='Agent2')]"));
    }
}
