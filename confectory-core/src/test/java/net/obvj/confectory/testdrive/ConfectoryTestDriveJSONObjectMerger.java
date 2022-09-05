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

import static net.obvj.jsonmerge.JsonMergeOption.onPath;

import net.minidev.json.JSONObject;
import net.obvj.confectory.Configuration;
import net.obvj.confectory.mapper.JSONObjectMapper;
import net.obvj.confectory.source.StringSource;

public class ConfectoryTestDriveJSONObjectMerger
{
    public static void main(String[] args)
    {
        Configuration<JSONObject> config1 = Configuration.<JSONObject>builder()
                .namespace("test").precedence(10)
                .source(new StringSource<>("{\n"
                        + "  \"agents\": [\n"
                        + "    {\n"
                        + "      \"class\": \"Agent1\",\n"
                        + "      \"interval\": \"*/2 * * * *\"\n"
                        + "    },\n"
                        + "    {\n"
                        + "      \"class\": \"Agent2\",\n"
                        + "      \"interval\": \"90s\"\n"
                        + "    }\n"
                        + "  ]\n"
                        + "}n"))
                .mapper(new JSONObjectMapper()).build();

        Configuration<JSONObject> config2 = Configuration.<JSONObject>builder()
                .namespace("test").precedence(20)
                .source(new StringSource<>("{\n"
                        + "  \"enabled\": true,\n"
                        + "  \"agents\": [\n"
                        + "    {\n"
                        + "      \"class\": \"Agent2\",\n"
                        + "      \"interval\": \"10s\"\n"
                        + "    }\n"
                        + "  ]\n"
                        + "}"))
                .mapper(new JSONObjectMapper()).build();


        Configuration<JSONObject> config = config1.merge(config2,
                onPath("$.agents").findObjectsIdentifiedBy("class")
                        .thenPickTheHighestPrecedenceOne());

        System.out.println(config.getBean());
    }
}
