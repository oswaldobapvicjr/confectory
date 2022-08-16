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

package net.obvj.confectory.merger;

import com.fasterxml.jackson.databind.JsonNode;

import net.obvj.confectory.Configuration;
import net.obvj.jsonmerge.provider.JacksonJsonNodeJsonProvider;

/**
 * A specialized {@code ConfigurationMerger} that combines two {@link Configuration}
 * objects of type {@link JsonNode} ({@code Jackson} implementation) into a single one.
 * <p>
 * For additional information, refer to the superclass
 * {@link GenericJsonConfigurationMerger}.
 *
 * @see GenericJsonConfigurationMerger
 * @author oswaldo.bapvic.jr (Oswaldo Junior)
 * @since 2.2.0
 */
public class JacksonJsonNodeConfigurationMerger extends GenericJsonConfigurationMerger<JsonNode>
{

    /**
     * Creates a new JSON Configuration Merger for {@code Jackson}'s {@link JsonNode}
     * implementation.
     */
    public JacksonJsonNodeConfigurationMerger()
    {
        super(new JacksonJsonNodeJsonProvider());
    }
}
