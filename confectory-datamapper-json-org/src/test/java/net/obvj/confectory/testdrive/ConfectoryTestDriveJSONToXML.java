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

import org.w3c.dom.Document;

import net.obvj.confectory.Configuration;
import net.obvj.confectory.mapper.JsonOrgJSONToXMLMapper;
import net.obvj.confectory.source.StringSource;

public class ConfectoryTestDriveJSONToXML
{
    public static void main(String[] args)
    {
        String json = "{\n"
                    + "  \"enabled\": true,\n"
                    + "  \"agents\": [\n"
                    + "    {\n"
                    + "      \"interval\": \"*/2 * * * *\",\n"
                    + "      \"class\": \"Agent1\"\n"
                    + "    },\n"
                    + "    {\n"
                    + "      \"interval\": \"10s\",\n"
                    + "      \"class\": \"Agent2\"\n"
                    + "    },\n"
                    + "    {\n"
                    + "      \"interval\": \"1h\",\n"
                    + "      \"class\": \"Agent3\"\n"
                    + "    }\n"
                    + "  ]\n"
                    + "}\n";

        Configuration<Document> config = Configuration.<Document>builder()
                .source(new StringSource<>(json))
                .mapper(new JsonOrgJSONToXMLMapper())
                .build();

        System.out.println(config.getAsString());
        System.out.println(config.getBoolean("/root/enabled"));
        System.out.println(config.getString("/root/agents[1]/interval"));
        System.out.println(config.getString("//agents[class='Agent2']/interval"));
        System.out.println(config.getString("//agents[interval='1h']/class"));
    }
}
