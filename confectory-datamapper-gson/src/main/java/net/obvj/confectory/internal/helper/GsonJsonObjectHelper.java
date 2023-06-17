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

package net.obvj.confectory.internal.helper;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.jayway.jsonpath.spi.json.GsonJsonProvider;
import com.jayway.jsonpath.spi.mapper.GsonMappingProvider;

import net.obvj.confectory.merger.ConfigurationMerger;
import net.obvj.confectory.merger.GsonJsonObjectConfigurationMerger;

/**
 * A specialized Configuration Helper that retrieves data from Gson's {@link JsonObject},
 * with JSONPath capabilities.
 *
 * @author oswaldo.bapvic.jr (Oswaldo Junior)
 * @since 0.4.0
 */
public class GsonJsonObjectHelper extends GenericJsonConfigurationHelper<JsonObject>
{

    /**
     * Creates a new helper for the given {@link JsonObject}.
     *
     * @param jsonObject the JSON document to be set
     */
    public GsonJsonObjectHelper(JsonObject jsonObject)
    {
        super(jsonObject, new GsonJsonProvider(), new GsonMappingProvider());
    }

    @Override
    public ConfigurationMerger<JsonObject> configurationMerger()
    {
        return new GsonJsonObjectConfigurationMerger();
    }

    @Override
    public String getAsString()
    {
        return new GsonBuilder().setPrettyPrinting().create().toJson(json);
    }

}
