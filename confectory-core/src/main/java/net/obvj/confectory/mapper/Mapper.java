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

import net.obvj.confectory.helper.ConfigurationHelper;

/**
 * The base interface for a configuration mapper.
 *
 * @param <T> the configuration output type
 *
 * @author oswaldo.bapvic.jr (Oswaldo Junior)
 * @since 0.1.0
 */
public interface Mapper<T>
{
    /**
     * Applies this {@code Mapper} into the given input.
     *
     * @param input the input stream to be mapped
     * @return the mapped object
     *
     * @throws IOException if a low-level I/O problem (such and unexpected end-of-input, or
     *                     network error) occurs
     */
    T apply(InputStream input) throws IOException;

    /**
     * Creates a new {@link ConfigurationHelper} instance recommended by this {@code Mapper}.
     *
     * @param bean the configuration object to be used by the helper
     * @return a new {@link ConfigurationHelper} instance
     */
    ConfigurationHelper<T> configurationHelper(T bean);

}
