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

import net.obvj.confectory.internal.helper.ConfigurationHelper;
import net.obvj.confectory.source.Source;

/**
 * The base interface for a configuration mapper.
 * <p>
 * A {@code Mapper} is an object that carries the business logic for parsing the input
 * stream open by a {@link Source} and producing the final object, which can be typically
 * an "object container" (such as a HashMap or JSON object, for example), or even POJOs
 * (user-defined beans), depending on the selected implementation.
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
     * <p>
     * <strong>Note:</strong> The input stream must be closed by the caller after the mapping
     * operation.
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
