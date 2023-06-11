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

package net.obvj.confectory.mapper;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;

import net.obvj.confectory.internal.helper.ConfigurationHelper;
import net.obvj.confectory.internal.helper.JacksonJsonNodeHelper;

/**
 * A specialized {@code Mapper} that loads the contents of a valid YAML {@code Source}
 * (e.g.: file, URL, string) and converts it into a {@link JsonNode}, using Jackson's
 * {@link YAMLMapper}.
 * <p>
 * This allows fetching the contents using <b>JSONPath</b> expressions.
 * <p>
 * Additional details may be found at Jackson's official documentation.
 * <p>
 * <b>Note:</b> Support for Jackson modules lookup is disabled for this type of mapper.
 *
 * @author oswaldo.bapvic.jr (Oswaldo Junior)
 * @since 0.3.0
 */
public class JacksonYAMLToJsonNodeMapper extends JacksonYAMLToObjectMapper<JsonNode> implements Mapper<JsonNode>
{

    /**
     * Builds a new YAML-to-JSON mapper.
     */
    public JacksonYAMLToJsonNodeMapper()
    {
        super(JsonNode.class, true);
    }

    @Override
    public ConfigurationHelper<JsonNode> configurationHelper(JsonNode jsonNode)
    {
        return new JacksonJsonNodeHelper(jsonNode);
    }

}
