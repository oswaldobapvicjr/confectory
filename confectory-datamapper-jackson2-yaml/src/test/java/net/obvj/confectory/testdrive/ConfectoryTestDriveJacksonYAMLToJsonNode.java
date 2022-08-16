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

import com.fasterxml.jackson.databind.JsonNode;

import net.obvj.confectory.Configuration;
import net.obvj.confectory.mapper.JacksonYAMLToJsonNodeMapper;
import net.obvj.confectory.source.SourceFactory;

public class ConfectoryTestDriveJacksonYAMLToJsonNode
{
    public static void main(String[] args)
    {
        String yaml = "root:\n"
                    + "  foo: bar\n";

        Configuration<JsonNode> config = Configuration.<JsonNode>builder()
                .namespace("test")
                .source(SourceFactory.stringSource(yaml))
                .mapper(new JacksonYAMLToJsonNodeMapper())
                .build();

        System.out.println(config.getBean().toPrettyString());
        System.out.println(config.getString("$.root.foo"));
    }
}
