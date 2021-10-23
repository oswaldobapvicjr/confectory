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

package net.obvj.confectory.helper;

import java.util.function.Supplier;

import com.fasterxml.jackson.databind.JsonNode;
import com.jayway.jsonpath.spi.json.JacksonJsonNodeJsonProvider;
import com.jayway.jsonpath.spi.mapper.JacksonMappingProvider;

import net.obvj.confectory.ConfigurationException;

/**
 * A specialized Configuration Helper that retrieves data from Jackson's {@link JsonNode},
 * with JSONPath capabilities.
 *
 * @author oswaldo.bapvic.jr (Oswaldo Junior)
 * @since 0.3.0
 */
public class JacksonJsonNodeHelper extends AbstractJsonPathHelper<JsonNode>
{

    /**
     * Creates a new helper for the given {@link JsonNode}.
     *
     * @param jsonNode the JSON document to be set
     */
    public JacksonJsonNodeHelper(JsonNode jsonNode)
    {
        super(jsonNode, new JacksonJsonNodeJsonProvider(), new JacksonMappingProvider());
    }

    @Override
    protected <T> T getValue(String jsonPath, Class<T> targetType, Supplier<T> defaultSupplier)
    {
        JsonNode result = documentContext.read(jsonPath);
        switch (result.size())
        {
        case 0:
            return defaultSupplier.get();
        case 1:
            return mappingProvider.map(result.get(0), targetType, jsonPathConfiguration);
        default:
            throw new ConfigurationException("The specified JSONPath returned more than one element: %s", jsonPath);
        }
    }

}
