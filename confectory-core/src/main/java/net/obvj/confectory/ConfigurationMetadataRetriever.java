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

package net.obvj.confectory;

import net.obvj.confectory.mapper.Mapper;
import net.obvj.confectory.source.Source;

/**
 * A base interface for objects that retrieve configuration metadata, such as
 * {@code Source}, {@code Mapper}, and other attributes.
 *
 * @param <T> the target configuration data type
 *
 * @author oswaldo.bapvic.jr (Oswaldo Junior)
 * @since 0.1.0
 */
public interface ConfigurationMetadataRetriever<T>
{

    /**
     * Returns the namespace defined for this {@code Configuration} object.
     *
     * @return the namespace defined for this {@code Configuration}
     */
    String getNamespace();

    /**
     * Returns the precedence value defined for this {@code Configuration} object.
     * <p>
     * In a common container, objects with higher-precedence may be selected first in case of
     * key collision.
     *
     * @return an integer number representing the order of importance given to this
     *         {@code Configuration}
     */
    int getPrecedence();

    /**
     * Returns the {@code Source} object associated with this {@code Configuration}.
     *
     * @return a {@link Source} instance
     */
    Source<T> getSource();

    /**
     * Returns the {@code Mapper} object associated with this {@code Configuration}.
     *
     * @return a {@link Mapper} instance
     */
    Mapper<T> getMapper();

    /**
     * Returns a flag indicating whether this {@code Configuration} is optional.
     * <p>
     * An optional {@code Configuration} object may behave quietly in the event of a failure
     * to load the data.
     *
     * @return {@code true} if this {@code Configuration} setup is optional; {@code false},
     *         otherwise
     */
    boolean isOptional();

    /**
     * Returns a flag indicating whether this {@code Configuration} is lazy, i.e., not loaded
     * until needed.
     *
     * @return {@code true} if this {@code Configuration} is lazy; {@code false}, otherwise
     *
     * @since 0.4.0
     */
    boolean isLazy();

}
