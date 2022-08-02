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

package net.obvj.confectory.source;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.UUID;

import net.obvj.confectory.ConfigurationSourceException;
import net.obvj.confectory.mapper.Mapper;

/**
 * A specialized {@code Source} that loads the contents of a {@code String}.
 *
 * @author oswaldo.bapvic.jr (Oswaldo Junior)
 * @since 0.1.0
 */
public class StringSource<T> extends AbstractSource<T> implements Source<T>
{
    private final String uuid;
    /**
     * Builds a new configuration source from the specified string.
     *
     * @param source the string to be loaded.
     */
    public StringSource(String source)
    {
        super(Objects.requireNonNull(source, "The source string must not be null"));
        uuid = UUID.randomUUID().toString();
    }

    @Override
    public T load(Mapper<T> mapper)
    {
        try
        {
            InputStream inputStream = new ByteArrayInputStream(super.parameter.getBytes());
            return load(inputStream, mapper);
        }
        catch (IOException exception)
        {
            throw new ConfigurationSourceException(exception, "Unable to load string: \"%s\"", super.parameter);
        }
    }

    @Override
    public String toString()
    {
        return new StringBuilder().append(getClass().getSimpleName()).append("(").append(uuid).append(")")
              .toString();
    }
}
