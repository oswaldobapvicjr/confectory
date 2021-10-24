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

import java.math.BigDecimal;

import org.json.JSONObject;

import com.jayway.jsonpath.spi.json.JsonOrgJsonProvider;
import com.jayway.jsonpath.spi.mapper.JsonOrgMappingProvider;

/**
 * A specialized Configuration Helper that retrieves data from JSON.org's
 * {@link JSONObject}, with JSONPath capabilities.
 *
 * @author oswaldo.bapvic.jr (Oswaldo Junior)
 * @since 0.2.0
 */
public class JSONObjectHelper extends GenericJsonConfigurationHelper<JSONObject>
{

    /**
     * Creates a new helper for the given {@link JSONObject}.
     *
     * @param jsonObject the JSON object to be set
     */
    public JSONObjectHelper(JSONObject jsonObject)
    {
        super(jsonObject, new JsonOrgJsonProvider(), new JsonOrgMappingProvider());
    }

    @Override
    public double getDouble(String jsonPath)
    {
        // JSON-Java parses double values as BigDecimals by default
        BigDecimal bigDecimal = getValue(jsonPath, BigDecimal.class,
                () -> BigDecimal.valueOf(super.nullValueProvider.getDoubleValue()));
        return bigDecimal.doubleValue();
    }

}
