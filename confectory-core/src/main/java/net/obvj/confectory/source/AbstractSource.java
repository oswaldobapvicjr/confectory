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

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.obvj.confectory.mapper.Mapper;

/**
 * An abstract configuration source, with common infrastructure.
 *
 * @param <T> the configuration data type returned by this {@code Source}
 *
 * @author oswaldo.bapvic.jr (Oswaldo Junior)
 * @since 0.1.0
 */
public abstract class AbstractSource<T> implements Source<T>
{
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractSource.class);

    protected final String parameter;

    /**
     * Builds a new configuration source with the specified path.
     *
     * @param parameter the source parameter (the meaning varies depending on the concrete
     *                  implementation)
     */
    protected AbstractSource(String parameter)
    {
        this.parameter = Objects.requireNonNull(parameter, "The source parameter must not be null");
    }

    /**
     * Loads the specified input stream with the specified mapper.
     *
     * @param inputStream the input stream to be loaded
     * @param mapper      the {@link Mapper} to be applied on the input stream
     * @return the loaded configuration data
     * @throws IOException in the event of a failure reading from the specified input stream
     */
    protected T load(InputStream inputStream, Mapper<T> mapper) throws IOException
    {
        return mapper.apply(inputStream);
    }

    @Override
    public Optional<T> load(Mapper<T> mapper, boolean optional)
    {
        try
        {
            T content = load(mapper);
            return Optional.ofNullable(content);
        }
        catch (Exception exception)
        {
            if (optional)
            {
                LOGGER.warn("Unable to load optional source: {}", this);
                LOGGER.debug("Exception details:", exception);
                return Optional.empty();
            }
            throw exception;
        }
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(getClass(), parameter);
    }

    /**
     * Indicates whether some other object is "equal to" this one.
     * <p>
     * Two {@code Source} objects can be considered equal if both share the same
     * implementation and parameter/path.
     *
     * @param other the other object with which to compare
     *
     * @return {@code true} if this object is the same as the one specified in the argument;
     *         {@code false} otherwise.
     */
    @Override
    public boolean equals(Object other)
    {
        if (this == other)
        {
            return true;
        }
        if (other == null)
        {
            return false;
        }
        if (getClass() != other.getClass())
        {
            return false;
        }
        AbstractSource<?> otherSource = (AbstractSource<?>) other;
        return Objects.equals(parameter, otherSource.parameter);
    }

    @Override
    public String toString()
    {
        return new StringBuilder().append(getClass().getSimpleName()).append("(").append(parameter).append(")").toString();
    }

}
