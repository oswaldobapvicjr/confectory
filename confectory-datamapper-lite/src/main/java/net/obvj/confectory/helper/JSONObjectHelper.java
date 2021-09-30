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
import java.util.Optional;
import java.util.function.Supplier;

import org.json.JSONArray;
import org.json.JSONObject;

import com.jayway.jsonpath.*;
import com.jayway.jsonpath.spi.json.JsonOrgJsonProvider;
import com.jayway.jsonpath.spi.mapper.JsonOrgMappingProvider;

import net.obvj.confectory.ConfigurationException;

/**
 * A specialized Configuration Helper that retrieves data from a {@link JSONObject}.
 *
 * @author oswaldo.bapvic.jr (Oswaldo Junior)
 * @since 0.2.0
 */
public class JSONObjectHelper extends AbstractBasicConfigurationHelper<JSONObject>
{
    private final JSONObject jsonObject;

    private final JsonOrgMappingProvider mappingProvider;
    private final Configuration jsonPathConfiguration;
    private final ParseContext jsonPathContext;
    private final DocumentContext documentContext;

    public JSONObjectHelper(JSONObject jsonObject)
    {
        this.jsonObject = jsonObject;
        mappingProvider = new JsonOrgMappingProvider();
        jsonPathConfiguration = Configuration.builder().jsonProvider(new JsonOrgJsonProvider())
                .mappingProvider(mappingProvider).options(Option.SUPPRESS_EXCEPTIONS, Option.ALWAYS_RETURN_LIST).build();
        jsonPathContext = JsonPath.using(jsonPathConfiguration);
        documentContext = jsonPathContext.parse(jsonObject);
    }

    @Override
    public Optional<JSONObject> getBean()
    {
        return Optional.ofNullable(jsonObject);
    }

    @Override
    public boolean getBoolean(String jsonPath)
    {
        return getValue(jsonPath, boolean.class, () -> nullValueProvider.getBooleanValue());
    }

    @Override
    public int getInt(String jsonPath)
    {
        return getValue(jsonPath, int.class, () -> nullValueProvider.getIntValue());
    }

    @Override
    public long getLong(String jsonPath)
    {
        return getValue(jsonPath, long.class, () -> nullValueProvider.getLongValue());
    }

    @Override
    public double getDouble(String jsonPath)
    {
        return getValue(jsonPath, BigDecimal.class, () -> BigDecimal.valueOf(nullValueProvider.getDoubleValue()))
                .doubleValue();
    }

    @Override
    public String getString(String jsonPath)
    {
        return getValue(jsonPath, String.class, () -> nullValueProvider.getStringValue());
    }

    private <T> T getValue(String jsonPath, Class<T> clazz, Supplier<T> defaultSupplier)
    {
        JSONArray result = documentContext.read(jsonPath);
        switch (result.length())
        {
        case 0:
            return defaultSupplier.get();
        case 1:
            return mappingProvider.map(result.get(0), clazz, jsonPathConfiguration);
        default:
            throw new ConfigurationException("The specified JSONPath returned more than one element: %s", jsonPath);
        }
    }
}
