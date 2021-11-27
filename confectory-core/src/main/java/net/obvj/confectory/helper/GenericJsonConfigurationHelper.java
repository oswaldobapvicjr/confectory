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

import com.jayway.jsonpath.*;
import com.jayway.jsonpath.internal.filter.ValueNodes.JsonNode;
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
public class GenericJsonConfigurationHelper<J> implements ConfigurationHelper<J>
{
    protected final J json;
    protected final JsonProvider jsonProvider;
    protected final MappingProvider mappingProvider;
    protected final Configuration jsonPathConfiguration;
    protected final ParseContext jsonPathContext;
    protected final DocumentContext documentContext;

    /**
     * Creates a new helper for the given {@link JsonNode}.
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
     * Returns the {@code Boolean} object associated with the specified {@code jsonPath} in
     * the {@code JsonNode} in context, provided that the expression returns a single element
     * that can be mapped to {@code boolean}.
     *
     * @param jsonPath the path to read
     * @return the {@code Boolean} object associated with the specified {@code jsonPath};
     *         {@code null} if not found
     *
     * @throws InvalidPathException   if the {@code jsonPath} expression is not valid
     * @throws ConfigurationException if the {@code jsonPath} expression returns more than a
     *                                single element
     * @throws ClassCastException     if the {@code jsonPath} result cannot be assigned to
     *                                {@code boolean}
     */
    @Override
    public Boolean getBoolean(String jsonPath)
    {
        return getValue(jsonPath, Boolean.class, false);
    }

    /**
     * Returns the {@code Boolean} object associated with the specified {@code jsonPath} in
     * the {@code JsonNode} in context, provided that the expression returns a single element
     * that can be mapped to {@code boolean}.
     *
     * @param jsonPath the path to read
     * @return the {@code Boolean} object associated with the specified {@code jsonPath};
     *         never {@code null}
     *
     * @throws InvalidPathException   if the {@code jsonPath} expression is not valid
     * @throws ConfigurationException if not value found or the {@code jsonPath} expression
     *                                returns more than a single element
     * @throws ClassCastException     if the {@code jsonPath} result cannot be assigned to
     *                                {@code boolean}
     * @since 0.4.0
     */
    @Override
    public Boolean getMandatoryBoolean(String jsonPath)
    {
        return getValue(jsonPath, Boolean.class);
    }

    /**
     * Returns the {@code Integer} object associated with the specified {@code jsonPath} in
     * the {@code JsonNode} in context, provided that the expression returns a single element
     * that can be mapped to {@code int}.
     *
     * @param jsonPath the path to read
     * @return the {@code Integer} object associated with the specified {@code jsonPath};
     *         {@code null} if not found
     *
     * @throws InvalidPathException   if the {@code jsonPath} expression is not valid
     * @throws ConfigurationException if the {@code jsonPath} expression returns more than a
     *                                single element
     * @throws ClassCastException     if the {@code jsonPath} result cannot be assigned to
     *                                {@code int}
     */
    @Override
    public Integer getInteger(String jsonPath)
    {
        return getValue(jsonPath, Integer.class, false);
    }

    /**
     * Returns the {@code Integer} object associated with the specified {@code jsonPath} in
     * the {@code JsonNode} in context, provided that the expression returns a single element
     * that can be mapped to {@code int}.
     *
     * @param jsonPath the path to read
     * @return the {@code Integer} object associated with the specified {@code jsonPath};
     *         never {@code null}
     *
     * @throws InvalidPathException   if the {@code jsonPath} expression is not valid
     * @throws ConfigurationException if not value found or the {@code jsonPath} expression
     *                                returns more than a single element
     * @throws ClassCastException     if the {@code jsonPath} result cannot be assigned to
     *                                {@code int}
     * @since 0.4.0
     */
    @Override
    public Integer getMandatoryInteger(String jsonPath)
    {
        return getValue(jsonPath, Integer.class);
    }

    /**
     * Returns the {@code Long} object associated with the specified {@code jsonPath} in the
     * {@code JsonNode} in context, provided that the expression returns a single element that
     * can be mapped to {@code long}.
     *
     * @param jsonPath the path to read
     * @return the {@code Long} object associated with the specified {@code jsonPath};
     *         {@code null} if not found
     *
     * @throws InvalidPathException   if the {@code jsonPath} expression is not valid
     * @throws ConfigurationException if the {@code jsonPath} expression returns more than a
     *                                single element
     * @throws ClassCastException     if the {@code jsonPath} result cannot be assigned to
     *                                {@code long}
     */
    @Override
    public Long getLong(String jsonPath)
    {
        return getValue(jsonPath, Long.class, false);
    }

    /**
     * Returns the {@code Long} object associated with the specified {@code jsonPath} in the
     * {@code JsonNode} in context, provided that the expression returns a single element that
     * can be mapped to {@code long}.
     *
     * @param jsonPath the path to read
     * @return the {@code Long} object associated with the specified {@code jsonPath}; never
     *         {@code null}
     *
     * @throws InvalidPathException   if the {@code jsonPath} expression is not valid
     * @throws ConfigurationException if not value found or the {@code jsonPath} expression
     *                                returns more than a single element
     * @throws ClassCastException     if the {@code jsonPath} result cannot be assigned to
     *                                {@code long}
     * @since 0.4.0
     */
    @Override
    public Long getMandatoryLong(String jsonPath)
    {
        return getValue(jsonPath, Long.class);
    }

    /**
     * Returns the {@code Double} object associated with the specified {@code jsonPath} in the
     * {@code JsonNode} in context, provided that the expression returns a single element that
     * can be mapped to {@code double}.
     *
     * @param jsonPath the path to read
     * @return the {@code Double} object associated with the specified {@code jsonPath};
     *         {@code null} if not found
     *
     * @throws InvalidPathException   if the {@code jsonPath} expression is not valid
     * @throws ConfigurationException if the {@code jsonPath} expression returns more than a
     *                                single element
     * @throws ClassCastException     if the {@code jsonPath} result cannot be assigned to
     *                                {@code double}
     */
    @Override
    public Double getDouble(String jsonPath)
    {
        return getValue(jsonPath, Double.class, false);
    }

    /**
     * Returns the {@code Double} object associated with the specified {@code jsonPath} in the
     * {@code JsonNode} in context, provided that the expression returns a single element that
     * can be mapped to {@code double}.
     *
     * @param jsonPath the path to read
     * @return the {@code Double} object associated with the specified {@code jsonPath}; never
     *         {@code null}
     *
     * @throws InvalidPathException   if the {@code jsonPath} expression is not valid
     * @throws ConfigurationException if not value found or the {@code jsonPath} expression
     *                                returns more than a single element
     * @throws ClassCastException     if the {@code jsonPath} result cannot be assigned to
     *                                {@code double}
     * @since 0.4.0
     */
    @Override
    public Double getMandatoryDouble(String jsonPath)
    {
        return getValue(jsonPath, Double.class);
    }

    /**
     * Returns the {@code String} object associated with the specified {@code jsonPath} in the
     * {@code JsonNode} in context, provided that the expression returns a single element.
     *
     * @param jsonPath the path to read
     * @return the {@code String} object associated with the specified {@code jsonPath};
     *         {@code null} if not found
     *
     * @throws InvalidPathException   if the {@code jsonPath} expression is not valid
     * @throws ConfigurationException if the {@code jsonPath} expression returns more than a
     *                                single element
     */
    @Override
    public String getString(String jsonPath)
    {
        return getValue(jsonPath, String.class, false);
    }

    /**
     * Returns the {@code String} object associated with the specified {@code jsonPath} in the
     * {@code JsonNode} in context, provided that the expression returns a single element.
     *
     * @param jsonPath the path to read
     * @return the {@code String} value associated with the specified {@code jsonPath}; never
     *         {@code null}
     *
     * @throws InvalidPathException   if the {@code jsonPath} expression is not valid
     * @throws ConfigurationException if not value found or the {@code jsonPath} expression
     *                                returns more than a single element
     * @since 0.4.0
     */
    @Override
    public String getMandatoryString(String jsonPath)
    {
        return getValue(jsonPath, String.class);
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
     * @throws InvalidPathException   if the {@code jsonPath} expression is not valid
     * @throws ConfigurationException if not value found or the {@code jsonPath} expression
     *                                returns more than a single element
     * @throws ClassCastException     if the {@code jsonPath} result cannot be assigned to the
     *                                specified {@code targetType}
     * @since 0.4.0
     */
    protected <T> T getValue(String jsonPath, Class<T> targetType)
    {
        return getValue(jsonPath, targetType, true);
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
     * @throws InvalidPathException   if the {@code jsonPath} expression is not valid
     * @throws ConfigurationException if the {@code jsonPath} expression returns more than a
     *                                single element
     * @throws ClassCastException     if the {@code jsonPath} result cannot be assigned to the
     *                                specified {@code targetType}
     */
    protected <T> T getValue(String jsonPath, Class<T> targetType, boolean mandatory)
    {
        Object result = documentContext.read(jsonPath);
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

}
