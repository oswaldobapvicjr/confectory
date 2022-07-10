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

import net.obvj.confectory.ConfigurationSourceException;
import net.obvj.confectory.mapper.Mapper;

/**
 * The base interface for a configuration source.
 * <p>
 * A {@code Source} is an object that basically defines the configuration input (e.g.:
 * file, URL) and contains built-in logic for data access.
 *
 * @param <T> the configuration data type returned by this {@code Source}
 *
 * @author oswaldo.bapvic.jr (Oswaldo Junior)
 * @since 0.1.0
 */
public interface Source<T>
{

    /**
     * Applies a specific configuration loading strategy and returns a mapped bean containing
     * the retrieved data, throwing an exception if the operation fails.
     *
     * @param mapper the {@link Mapper} to be applied on the source input stream
     * @return the loaded configuration data
     * @throws ConfigurationSourceException in an event of failure to load the configuration
     *                                      source
     */
    T load(Mapper<T> mapper);

    /**
     * Applies a specific configuration loading strategy and returns a mapped bean containing
     * the retrieved data. If the {@code optional} parameter is {@code true} and the operation
     * fails, this method returns {@code null}.
     *
     * @param mapper   the {@link Mapper} to be applied on the source input stream
     * @param optional a flag indicating whether or not an exception should be thrown in an
     *                 event of failure to load the configuration source
     * @return the loaded configuration data, or {@code null} in an event of failure if the
     *         {@code optional} flag set as {@code true}
     *
     * @throws ConfigurationSourceException in an event of failure to load the configuration
     *                                      source (only if the {@code optional} flag set as
     *                                      {@code false})
     */
    T load(Mapper<T> mapper, boolean optional);

}
