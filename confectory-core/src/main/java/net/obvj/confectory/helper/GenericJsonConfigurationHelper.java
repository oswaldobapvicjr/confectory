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

import java.util.Optional;
import java.util.function.*;

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
public class GenericJsonConfigurationHelper<J> extends AbstractBasicConfigurationHelper<J>
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
    public Optional<J> getBean()
    {
        return Optional.ofNullable(json);
    }

    /**
     * Returns the {@code boolean} value associated with the specified {@code jsonPath} in the
     * {@code JsonNode} in context, provided that the expression returns a single element that
     * can be mapped to {@code boolean}.
     *
     * @param jsonPath the path to read
     * @return the {@code boolean} value associated with the specified {@code jsonPath}
     *
     * @throws InvalidPathException   if the {@code jsonPath} expression is not valid
     * @throws ConfigurationException if the {@code jsonPath} expression returns more than a
     *                                single element
     * @throws ClassCastException     if the {@code jsonPath} result cannot be assigned to
     *                                {@code boolean}
     */
    @Override
    public boolean getBoolean(String jsonPath)
    {
        return getValue(jsonPath, boolean.class, nullValueProvider::getBooleanValue);
    }

    /**
     * Returns the {@code boolean} value associated with the specified {@code jsonPath} in the
     * {@code JsonNode} in context, provided that the expression returns a single element that
     * can be mapped to {@code boolean}.
     *
     * @param jsonPath the path to read
     * @return the {@code boolean} value associated with the specified {@code jsonPath}
     *
     * @throws InvalidPathException   if the {@code jsonPath} expression is not valid
     * @throws ConfigurationException if not value found or the {@code jsonPath} expression
     *                                returns more than a single element
     * @throws ClassCastException     if the {@code jsonPath} result cannot be assigned to
     *                                {@code boolean}
     * @since 0.4.0
     */
    @Override
    public boolean getMandatoryBoolean(String jsonPath)
    {
        return getValue(jsonPath, boolean.class);
    }

    /**
     * Returns the {@code int} value associated with the specified {@code jsonPath} in the
     * {@code JsonNode} in context, provided that the expression returns a single element that
     * can be mapped to {@code int}.
     *
     * @param jsonPath the path to read
     * @return the {@code int} value associated with the specified {@code jsonPath}
     *
     * @throws InvalidPathException   if the {@code jsonPath} expression is not valid
     * @throws ConfigurationException if the {@code jsonPath} expression returns more than a
     *                                single element
     * @throws ClassCastException     if the {@code jsonPath} result cannot be assigned to
     *                                {@code int}
     */
    @Override
    public int getInt(String jsonPath)
    {
        return getValue(jsonPath, int.class, nullValueProvider::getIntValue);
    }

    /**
     * Returns the {@code boolean} value associated with the specified {@code jsonPath} in the
     * {@code JsonNode} in context, provided that the expression returns a single element that
     * can be mapped to {@code boolean}.
     *
     * @param jsonPath the path to read
     * @return the {@code boolean} value associated with the specified {@code jsonPath}
     *
     * @throws InvalidPathException   if the {@code jsonPath} expression is not valid
     * @throws ConfigurationException if not value found or the {@code jsonPath} expression
     *                                returns more than a single element
     * @throws ClassCastException     if the {@code jsonPath} result cannot be assigned to
     *                                {@code boolean}
     * @since 0.4.0
     */
    @Override
    public int getMandatoryInt(String jsonPath)
    {
        return getValue(jsonPath, int.class);
    }

    /**
     * Returns the {@code long} value associated with the specified {@code jsonPath} in the
     * {@code JsonNode} in context, provided that the expression returns a single element that
     * can be mapped to {@code long}.
     *
     * @param jsonPath the path to read
     * @return the {@code long} value associated with the specified {@code jsonPath}
     *
     * @throws InvalidPathException   if the {@code jsonPath} expression is not valid
     * @throws ConfigurationException if the {@code jsonPath} expression returns more than a
     *                                single element
     * @throws ClassCastException     if the {@code jsonPath} result cannot be assigned to
     *                                {@code long}
     */
    @Override
    public long getLong(String jsonPath)
    {
        return getValue(jsonPath, long.class, nullValueProvider::getLongValue);
    }

    /**
     * Returns the {@code boolean} value associated with the specified {@code jsonPath} in the
     * {@code JsonNode} in context, provided that the expression returns a single element that
     * can be mapped to {@code boolean}.
     *
     * @param jsonPath the path to read
     * @return the {@code boolean} value associated with the specified {@code jsonPath}
     *
     * @throws InvalidPathException   if the {@code jsonPath} expression is not valid
     * @throws ConfigurationException if not value found or the {@code jsonPath} expression
     *                                returns more than a single element
     * @throws ClassCastException     if the {@code jsonPath} result cannot be assigned to
     *                                {@code boolean}
     * @since 0.4.0
     */
    @Override
    public long getMandatoryLong(String jsonPath)
    {
        return getValue(jsonPath, long.class);
    }

    /**
     * Returns the {@code double} value associated with the specified {@code jsonPath} in the
     * {@code JsonNode} in context, provided that the expression returns a single element that
     * can be mapped to {@code double}.
     *
     * @param jsonPath the path to read
     * @return the {@code double} value associated with the specified {@code jsonPath}
     *
     * @throws InvalidPathException   if the {@code jsonPath} expression is not valid
     * @throws ConfigurationException if the {@code jsonPath} expression returns more than a
     *                                single element
     * @throws ClassCastException     if the {@code jsonPath} result cannot be assigned to
     *                                {@code double}
     */
    @Override
    public double getDouble(String jsonPath)
    {
        return getValue(jsonPath, double.class, nullValueProvider::getDoubleValue);
    }

    /**
     * Returns the {@code boolean} value associated with the specified {@code jsonPath} in the
     * {@code JsonNode} in context, provided that the expression returns a single element that
     * can be mapped to {@code boolean}.
     *
     * @param jsonPath the path to read
     * @return the {@code boolean} value associated with the specified {@code jsonPath}
     *
     * @throws InvalidPathException   if the {@code jsonPath} expression is not valid
     * @throws ConfigurationException if not value found or the {@code jsonPath} expression
     *                                returns more than a single element
     * @throws ClassCastException     if the {@code jsonPath} result cannot be assigned to
     *                                {@code boolean}
     * @since 0.4.0
     */
    @Override
    public double getMandatoryDouble(String jsonPath)
    {
        return getValue(jsonPath, double.class);
    }

    /**
     * Returns the {@code String} value associated with the specified {@code jsonPath} in the
     * {@code JsonNode} in context, provided that the expression returns a single element.
     *
     * @param jsonPath the path to read
     * @return the {@code String} value associated with the specified {@code jsonPath}
     *
     * @throws InvalidPathException   if the {@code jsonPath} expression is not valid
     * @throws ConfigurationException if the {@code jsonPath} expression returns more than a
     *                                single element
     */
    @Override
    public String getString(String jsonPath)
    {
        return getValue(jsonPath, String.class, nullValueProvider::getStringValue);
    }

    /**
     * Returns the {@code boolean} value associated with the specified {@code jsonPath} in the
     * {@code JsonNode} in context, provided that the expression returns a single element that
     * can be mapped to {@code boolean}.
     *
     * @param jsonPath the path to read
     * @return the {@code boolean} value associated with the specified {@code jsonPath}
     *
     * @throws InvalidPathException   if the {@code jsonPath} expression is not valid
     * @throws ConfigurationException if not value found or the {@code jsonPath} expression
     *                                returns more than a single element
     * @throws ClassCastException     if the {@code jsonPath} result cannot be assigned to
     *                                {@code boolean}
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
     * @throws ClassCastException     if the {@code jsonPath} result cannot be assigned to
     *                                {@code double}
     * @since 0.4.0
     */
    protected <T> T getValue(String jsonPath, Class<T> targetType)
    {
        return getValue(jsonPath, targetType, null);
    }

    /**
     * Returns the value associated with the specified {@code jsonPath} in the JSON document
     * in context, provided that the expression returns a single element that can be mapped to
     * the specified class type.
     *
     * @param jsonPath        the path to read
     * @param targetType      the type the expression result should be mapped to
     * @param defaultSupplier the supplier to be used if JSONPath not found
     *
     * @return the mapped value associated with the specified {@code jsonPath}
     *
     * @throws InvalidPathException   if the {@code jsonPath} expression is not valid
     * @throws ConfigurationException if the {@code jsonPath} expression returns more than a
     *                                single element
     * @throws ClassCastException     if the {@code jsonPath} result cannot be assigned to
     *                                {@code double}
     */
    protected <T> T getValue(String jsonPath, Class<T> targetType, Supplier<T> defaultSupplier)
    {
        Object result = documentContext.read(jsonPath);
        switch (jsonProvider.length(result))
        {
        case 0:
            return applyDefault(defaultSupplier, jsonPath);
        case 1:
            Object element = jsonProvider.getArrayIndex(result, 0);
            return mappingProvider.map(element, targetType, jsonPathConfiguration);
        default:
            throw new ConfigurationException("Multiple values found for path: %s", jsonPath);
        }
    }

    private <T> T applyDefault(Supplier<T> defaultSupplier, String jsonPath)
    {
        if (defaultSupplier != null)
        {
            return defaultSupplier.get();
        }
        throw new ConfigurationException("No value found for path: %s", jsonPath);
    }

}
