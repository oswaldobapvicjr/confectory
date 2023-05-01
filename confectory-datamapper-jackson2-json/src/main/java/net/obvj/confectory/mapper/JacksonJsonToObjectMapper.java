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

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;

import net.obvj.confectory.internal.helper.BeanConfigurationHelper;
import net.obvj.confectory.internal.helper.ConfigurationHelper;

/**
 * A specialized {@code Mapper} that loads the contents of a valid JSON {@code Source}
 * (e.g.: file, URL, string) into POJO (Plain Old Java Object), using Jackson's
 * {@link JsonMapper}.
 * <p>
 * Additional details may be found at Jackson's official documentation.
 * <p>
 * Since version 2.4.0, this class supports lookup and registration of Jackson modules by
 * <b>default</b>. However, since modules lookup is considered a potentially expensive
 * operation, it can be disabled by setting the {@code disableModules} flag in the
 * constructor:
 * <blockquote>{@code new JacksonJsonToObjectMapper(Class<?>, boolean)}</blockquote>
 * <p>
 * <b>Note:</b> To avoid a performance overhead, Jackson modules lookup happens
 * automatically at the first instantiation of a {@code JacksonJsonToObjectMapper} with
 * enable support for modules.
 *
 * @param <T> the target type to be produced by this {@code Mapper} (the target class may
 *            contain Jackson annotations for due mapping -- e.g.:
 *            {@code @JsonProperty, @JsonIgnore})
 *
 * @author oswaldo.bapvic.jr (Oswaldo Junior)
 * @since 0.3.0
 */
public class JacksonJsonToObjectMapper<T> implements Mapper<T>
{
    private static List<com.fasterxml.jackson.databind.Module> cachedModules;

    protected Class<T> targetType;
    private boolean disableModules;

    /**
     * Builds a new {@code JacksonJsonToObjectMapper} instance with the specified target type
     * and support for Jackson modules enabled by default.
     *
     * @param targetType the target type to be produced by this {@code Mapper}
     */
    public JacksonJsonToObjectMapper(Class<T> targetType)
    {
        this(targetType, false);
    }

    /**
     * Builds a new {@code JacksonJsonToObjectMapper} instance with the specified target type.
     *
     * @param targetType     the target type to be produced by this {@code Mapper}
     * @param disableModules disable Jackson modules; useful for an optimized processing if
     *                       Jackson add-ons are NOT required
     * @since 2.4.0
     */
    public JacksonJsonToObjectMapper(Class<T> targetType, boolean disableModules)
    {
        this.targetType = targetType;
        this.disableModules = disableModules;
    }

    @Override
    public T apply(InputStream inputStream) throws IOException
    {
        return apply(inputStream, new JsonMapper());
    }

    /**
     * Applies the specified {@link ObjectMapper} into the given input.
     * <p>
     * The {@link ObjectMapper} may be filled with Jackson modules by this method if the
     * {@code disableModules} flag is not set.
     * <p>
     * <strong>Note:</strong> The input stream must be closed by the caller after the mapping
     * operation.
     *
     * @param input  the input stream to be mapped
     * @param mapper the {@link ObjectMapper} to be used
     * @return the mapped object
     *
     * @throws IOException if a low-level I/O problem (such and unexpected end-of-input, or
     *                     network error) occurs
     * @since 2.4.0
     */
    protected T apply(InputStream inputStream, ObjectMapper mapper) throws IOException
    {
        if (!disableModules)
        {
            mapper.registerModules(reloadModulesIfNull());
        }
        return mapper.readValue(inputStream, targetType);
    }

    private static List<com.fasterxml.jackson.databind.Module> reloadModulesIfNull()
    {
        if (cachedModules == null)
        {
            reloadModulesCache();
        }
        return cachedModules;
    }

    /**
     * Reload Jackson modules for use by new or existing {@code JacksonJsonToObjectMapper}
     * instances with Jackson modules enabled.
     * <p>
     * <b>Note:</b> According to Jackson documentation, calls to
     * {@link ObjectMapper#findModules()} are considered potentially expensive, so the lookup
     * happens automatically at the first instantiation of {@code JacksonJsonToObjectMapper}
     * with enabled support for modules.
     *
     * @since 2.4.0
     */
    public static void reloadModulesCache()
    {
        cachedModules = Collections.unmodifiableList(ObjectMapper.findModules());
    }

    /**
     * Reset Jackson modules for reload in the next use of {@code JacksonJsonToObjectMapper}
     * instances with Jackson modules enabled.
     *
     * @since 2.4.0
     */
    public static void resetModulesCache()
    {
        cachedModules = null;
    }

    /**
     * @returns the list of modules in cache
     * @since 2.4.0
     */
    static List<com.fasterxml.jackson.databind.Module> getCachedModules()
    {
        return cachedModules;
    }

    @Override
    public ConfigurationHelper<T> configurationHelper(T object)
    {
        return new BeanConfigurationHelper<>(object);
    }

}
