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
import net.obvj.confectory.ConfigurationContainer;
import net.obvj.confectory.mapper.JacksonYAMLToJsonNodeMapper;
import net.obvj.confectory.source.SourceFactory;

public class ConfectoryTestDriveYamlContainer
{
    public static void main(String[] args)
    {
        String yaml1 = "enabled: false\n"
                     + "string: value1\n";
        String yaml2 = "enabled: true\n"
                     + "int: 77\n";

        ConfigurationContainer config = new ConfigurationContainer(
                Configuration.<JsonNode>builder().source(SourceFactory.stringSource(yaml1))
                        .mapper(new JacksonYAMLToJsonNodeMapper()).lazy()
                        .precedence(10).build(),
                Configuration.<JsonNode>builder().source(SourceFactory.stringSource(yaml2))
                        .mapper(new JacksonYAMLToJsonNodeMapper()).lazy()
                        .precedence(5).build());

        System.out.println(config.getBoolean("enabled"));
        System.out.println(config.getString("string"));
        System.out.println(config.getInteger("int"));

    }
}
