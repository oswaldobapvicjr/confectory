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
import java.util.function.Supplier;

import org.json.JSONArray;
import org.json.JSONObject;

import com.jayway.jsonpath.spi.json.JsonOrgJsonProvider;
import com.jayway.jsonpath.spi.mapper.JsonOrgMappingProvider;

import net.obvj.confectory.ConfigurationException;

/**
 * A specialized Configuration Helper that retrieves data from JSON.org's
 * {@link JSONObject}, with JSONPath capabilities.
 *
 * @author oswaldo.bapvic.jr (Oswaldo Junior)
 * @since 0.2.0
 */
public class JSONObjectHelper extends AbstractJsonPathHelper<JSONObject>
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

    /**
     * Returns the {@code double} value associated with the specified {@code jsonPath} in the
     * {@code JSONObject} in context, provided that the expression returns a single element
     * that can be mapped to {@code double}.
     *
     * @param jsonPath the path to read
     * @return the {@code double} value associated with the specified {@code jsonPath}
     *
     * @throws ConfigurationException if the {@code jsonPath} expression returns more than a
     *                                single element
     * @throws ClassCastException     if the {@code jsonPath} result cannot be assigned to
     *                                {@code double}
     */
    @Override
    public double getDouble(String jsonPath)
    {
        BigDecimal bigDecimal = getValue(jsonPath, BigDecimal.class,
                () -> BigDecimal.valueOf(nullValueProvider.getDoubleValue()));
        return bigDecimal.doubleValue();
    }

    /**
     * Returns the value associated with the specified {@code jsonPath} in the
     * {@code JSONObject} in context, provided that the expression returns a single element
     * that can be mapped to the specified class type.
     *
     * @param jsonPath   the path to read
     * @param targetType the type the expression result should be mapped to
     * @return the mapped value associated with the specified {@code jsonPath}
     *
     * @throws ConfigurationException if the {@code jsonPath} expression returns more than a
     *                                single element
     * @throws ClassCastException     if the {@code jsonPath} result cannot be assigned to
     *                                {@code double}
     */
    @Override
    protected <T> T getValue(String jsonPath, Class<T> targetType, Supplier<T> defaultSupplier)
    {
        JSONArray result = documentContext.read(jsonPath);
        switch (result.length())
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
