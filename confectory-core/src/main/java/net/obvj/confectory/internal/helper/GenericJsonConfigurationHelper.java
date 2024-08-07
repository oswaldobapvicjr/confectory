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

import com.jayway.jsonpath.*;
import com.jayway.jsonpath.spi.json.JsonProvider;
import com.jayway.jsonpath.spi.mapper.MappingProvider;

import net.obvj.confectory.ConfigurationException;

/**
 * A generic Configuration Helper that retrieves data from a JSON document, with JSONPath
 * capabilities.
 *
 * @author oswaldo.bapvic.jr (Oswaldo Junior)
 * @since 0.3.0
 */
public abstract class GenericJsonConfigurationHelper<J> extends AbstractConfigurationHelper<J> implements ConfigurationHelper<J>
{
    protected final J json;
    protected final JsonProvider jsonProvider;
    protected final MappingProvider mappingProvider;
    protected final Configuration jsonPathConfiguration;
    protected final ParseContext jsonPathContext;
    protected final DocumentContext documentContext;

    /**
     * Creates a new helper for the given JSON.
     *
     * @param json            the JSON document to set
     * @param jsonProvider    the {@link JsonProvider} to set
     * @param mappingProvider the JSON {@link MappingProvider} to set
     */
    protected GenericJsonConfigurationHelper(J json, JsonProvider jsonProvider, MappingProvider mappingProvider)
    {
        this.json = json;
        this.jsonProvider = jsonProvider;
        this.mappingProvider = mappingProvider;

        jsonPathConfiguration = Configuration.builder().jsonProvider(jsonProvider).mappingProvider(mappingProvider)
                .options(Option.SUPPRESS_EXCEPTIONS, Option.ALWAYS_RETURN_LIST).build();
        jsonPathContext = JsonPath.using(jsonPathConfiguration);
        documentContext = jsonPathContext.parse(json);
    }

    /**
     * @return the JSON document in context
     */
    @Override
    public J getBean()
    {
        return json;
    }

    /**
     * Returns the value associated with the specified {@code jsonPath} in the JSON document
     * in context, provided that the expression returns a single element that can be mapped to
     * the specified class type.
     *
     * @param jsonPath   the path to read
     * @param targetType the type the expression result should be mapped to
     *
     * @return the mapped value associated with the specified {@code jsonPath}
     *
     * @throws IllegalArgumentException if {@code jsonPath} is null or empty
     * @throws InvalidPathException     if the {@code jsonPath} expression is not valid
     * @throws ConfigurationException   if the {@code jsonPath} expression returns more than a
     *                                  single element
     * @throws ClassCastException       if the {@code jsonPath} result cannot be assigned to
     *                                  the specified {@code targetType}
     */
    @Override
    protected <T> T getValue(String jsonPath, Class<T> targetType, boolean mandatory)
    {
        Object result = get(jsonPath);
        switch (jsonProvider.length(result))
        {
        case 0:
            if (mandatory)
            {
                throw new ConfigurationException("No value found for path: %s", jsonPath);
            }
            return null;
        case 1:
            Object element = jsonProvider.getArrayIndex(result, 0);
            return mappingProvider.map(element, targetType, jsonPathConfiguration);
        default:
            throw new ConfigurationException("Multiple values found for path: %s", jsonPath);
        }
    }

    /**
     * Returns the object associated with the specified @code jsonPath} in the JSON document
     * in context.
     * <p>
     * <b>Note:</b> The actual return type may vary depending on the JSON implementation, but
     * usually it should be an array.
     *
     * @param jsonPath the path to read
     *
     * @return the object associated with the specified {@code key}; or an empty array if the
     *         path is not found
     *
     * @throws IllegalArgumentException if {@code jsonPath} is null or empty
     * @throws InvalidPathException     if the {@code jsonPath} expression is not valid
     *
     * @since 1.2.0
     */
    @Override
    public Object get(String jsonPath)
    {
        return documentContext.read(jsonPath);
    }

}
