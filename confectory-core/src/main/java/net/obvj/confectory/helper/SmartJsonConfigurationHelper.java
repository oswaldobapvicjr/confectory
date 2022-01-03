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

package net.obvj.confectory.helper;

import com.jayway.jsonpath.spi.json.JsonSmartJsonProvider;
import com.jayway.jsonpath.spi.mapper.JsonSmartMappingProvider;

import net.minidev.json.JSONObject;

/**
 * A specialized Configuration Helper that retrieves data from {@code net.minidev}'s
 * (Smart) {@link JSONObject}, with JSONPath capabilities.
 *
 * @author oswaldo.bapvic.jr (Oswaldo Junior)
 * @since 1.0.1
 */
public class SmartJsonConfigurationHelper extends GenericJsonConfigurationHelper<JSONObject>
{

    /**
     * Creates a new helper for the given {@link JSONObject}.
     *
     * @param jsonObject the JSON document to be set
     */
    public SmartJsonConfigurationHelper(JSONObject jsonObject)
    {
        super(jsonObject, new JsonSmartJsonProvider(), new JsonSmartMappingProvider());
    }

}
