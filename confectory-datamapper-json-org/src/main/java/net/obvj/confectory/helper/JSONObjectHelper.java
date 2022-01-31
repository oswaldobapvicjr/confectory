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
    public Double getDouble(String jsonPath)
    {
        BigDecimal bigDecimal = getBigDecimal(jsonPath, false);
        return bigDecimal == null ? null : bigDecimal.doubleValue();
    }

    @Override
    public Double getMandatoryDouble(String jsonPath)
    {
        BigDecimal bigDecimal = getBigDecimal(jsonPath, true);
        return bigDecimal.doubleValue();
    }

    private BigDecimal getBigDecimal(String jsonPath, boolean optional)
    {
        // JSON-Java parses double values as BigDecimals by default
        return getValue(jsonPath, BigDecimal.class, optional);
    }

}
