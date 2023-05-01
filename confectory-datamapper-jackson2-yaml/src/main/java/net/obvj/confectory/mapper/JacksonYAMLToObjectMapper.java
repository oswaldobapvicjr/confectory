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

import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;

/**
 * A specialized {@code Mapper} that loads the contents of a valid YAML {@code Source}
 * (e.g.: file, URL, string) into POJO (Plain Old Java Object), using Jackson's
 * {@link YAMLMapper}.
 * <p>
 * Additional details may be found at Jackson's official documentation.
 * <p>
 * Since version 2.4.0, this class supports lookup and registration of Jackson modules by
 * <b>default</b>. However, since modules lookup is considered a potentially expensive
 * operation, it can be disabled by setting the {@code disableModules} flag in the
 * constructor:
 * <blockquote>{@code new JacksonYAMLToObjectMapper(Class<?>, boolean)}</blockquote>
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
public class JacksonYAMLToObjectMapper<T> extends JacksonJsonToObjectMapper<T> implements Mapper<T>
{

    /**
     * Builds a new YAML mapper with the specified target type and support for Jackson modules
     * enabled by default.
     *
     * @param targetType the target type to be produced by this {@code Mapper}
     */
    public JacksonYAMLToObjectMapper(Class<T> targetType)
    {
        super(targetType);
    }

    /**
     * Builds a new YAML mapper with the specified target type.
     *
     * @param targetType     the target type to be produced by this {@code Mapper}
     * @param disableModules disable Jackson modules; useful for an optimized processing if
     *                       Jackson add-ons are NOT required
     * @since 2.4.0
     */
    public JacksonYAMLToObjectMapper(Class<T> targetType, boolean disableModules)
    {
        super(targetType, disableModules);
    }

    @Override
    public T apply(InputStream inputStream) throws IOException
    {
        return super.apply(inputStream, new YAMLMapper());
    }

}
