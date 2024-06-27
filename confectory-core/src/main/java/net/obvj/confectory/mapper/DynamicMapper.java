/*
 * Copyright 2024 obvj.net
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
import java.util.Map;
import java.util.function.Supplier;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.obvj.confectory.internal.helper.ConfigurationHelper;
import net.obvj.confectory.util.Exceptions;

/**
 * A dynamic {@link Mapper} which automatically assigns a concrete mapping class
 * (from the default core mappers) depending on a given file extension:
 * <ul>
 * <li><code>"ini"</code> assigns the {@link INIToJSONObjectMapper}
 * <li><code>"json"</code> assigns the {@link JSONObjectMapper}
 * <li><code>"properties"</code> assigns the {@link PropertiesMapper}
 * <li><code>"txt"</code> assigns the {@link StringMapper}
 * <li><code>"xml"</code> assigns the {@link DocumentMapper}
 * </ul>
 *
 * @author oswaldo.bapvic.jr (Oswaldo Junior)
 * @since 2.6.0
 */
public final class DynamicMapper extends AbstractBeanMapper<Object> implements Mapper<Object>
{
    private static final Map<String, Supplier<Mapper<?>>> MAPPERS_BY_EXTENSION = Map.of(
            "ini", INIToJSONObjectMapper::new,
            "json", JSONObjectMapper::new,
            "properties", PropertiesMapper::new,
            "txt", StringMapper::new,
            "xml", DocumentMapper::new
    );

    private static final Logger LOGGER = LoggerFactory.getLogger(DynamicMapper.class);

    private final Mapper<Object> actualMapper;

    /**
     * Builds a new dynamic configuration mapper for the specified extension
     *
     * @param extension the file extension; not null
     * @throws IllegalArgumentException if the extension is empty or unknown
     */
    public DynamicMapper(String extension)
    {
        this.actualMapper = findMapper(extension);
    }

    private static Mapper<Object> findMapper(String extension)
    {
        Supplier<Mapper<?>> supplier = MAPPERS_BY_EXTENSION.get(StringUtils.lowerCase(extension));
        if (supplier == null)
        {
            throw Exceptions.illegalArgument(
                    "No default mapper available for the extension: \"%s\"", extension);
        }
        return (Mapper<Object>) supplier.get();
    }

    @Override
    public Object apply(InputStream inputStream) throws IOException
    {
        LOGGER.debug("Applying mapper {}", actualMapper.getClass());
        return actualMapper.apply(inputStream);
    }

    @Override
    public ConfigurationHelper<Object> configurationHelper(Object bean)
    {
        return actualMapper.configurationHelper(bean);
    }

    /**
     * @return The actual mapper
     */
    public Mapper<Object> getActualMapper()
    {
        return actualMapper;
    }

}
