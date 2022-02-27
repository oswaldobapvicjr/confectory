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

import com.google.gson.JsonObject;
import com.jayway.jsonpath.internal.filter.ValueNodes.JsonNode;

import net.obvj.confectory.internal.helper.ConfigurationHelper;
import net.obvj.confectory.internal.helper.GsonJsonObjectHelper;

/**
 * A specialized {@code Mapper} that loads the contents of a valid JSON {@code Source}
 * (e.g.: file, URL, string) as a {@link JsonNode}, using Gson.
 * <p>
 * Additional details may be found at Gson's official documentation.
 *
 * @author oswaldo.bapvic.jr (Oswaldo Junior)
 * @since 0.4.0
 */
public class GsonJsonObjectMapper extends GsonJsonToObjectMapper<JsonObject> implements Mapper<JsonObject>
{

    /**
     * Builds a new JSON mapper.
     */
    public GsonJsonObjectMapper()
    {
        super(JsonObject.class);
    }

    @Override
    public ConfigurationHelper<JsonObject> configurationHelper(JsonObject jsonObject)
    {
        return new GsonJsonObjectHelper(jsonObject);
    }

}
