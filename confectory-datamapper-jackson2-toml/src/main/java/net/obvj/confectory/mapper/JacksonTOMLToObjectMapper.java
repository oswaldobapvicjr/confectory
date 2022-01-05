/*
 * Copyright 2022 obvj.net
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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.toml.TomlMapper;

/**
 * A specialized {@code Mapper} that loads the contents of a valid TOML {@code Source}
 * (e.g.: file, URL, string) into POJO (Plain Old Java Object), using Jackson's
 * {@link TomlMapper}.
 * <p>
 * Additional details may be found at Jackson's official documentation.
 *
 * @param <T> the target type to be produced by this {@code Mapper} (the target class may
 *            contain Jackson annotations for due mapping -- e.g.:
 *            {@code @JsonProperty, @JsonIgnore})
 *
 * @author oswaldo.bapvic.jr (Oswaldo Junior)
 * @since 1.1.0
 */
public class JacksonTOMLToObjectMapper<T> extends JacksonJsonToObjectMapper<T> implements Mapper<T>
{

    /**
     * Builds a new TOML mapper with the specified target type.
     *
     * @param targetType the target type to be produced by this {@code Mapper}
     */
    public JacksonTOMLToObjectMapper(Class<T> targetType)
    {
        super(targetType);
    }

    @Override
    public T apply(InputStream inputStream) throws IOException
    {
        ObjectMapper mapper = new TomlMapper();
        return mapper.readValue(inputStream, super.targetType);
    }

}
