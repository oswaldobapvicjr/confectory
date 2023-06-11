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

package net.obvj.confectory.internal.helper;

import java.util.Objects;

import net.obvj.confectory.Configuration;
import net.obvj.confectory.ConfigurationDataRetriever;
import net.obvj.confectory.mapper.Mapper;
import net.obvj.confectory.merger.ConfigurationMerger;

/**
 * An abstraction for objects that retrieve data from previously loaded
 * {@link Configuration} objects.
 *
 * @param <T> the source type which configuration data is to be retrieved
 *
 * @author oswaldo.bapvic.jr (Oswaldo Junior)
 * @since 0.1.0
 */
public interface ConfigurationHelper<T> extends ConfigurationDataRetriever<T>
{

    /**
     * Creates a new {@code ConfigurationHelper} for the given {@link Mapper}.
     * <p>
     * If the specified {@code bean} is {@code null}, a {@link NullConfigurationHelper} will
     * be instantiated.
     *
     * @param <T>    the bean type which configuration data is to be retrieved
     * @param bean   the bean to be evaluated; may be {@code null}
     * @param mapper the {@link Mapper} whose helper should be instantiated; not {@code null}
     *
     * @return a new {@code ConfigurationHelper} instance for the specified {@link Mapper}
     * @throws NullPointerException if the specified mapper is {@code null}
     *
     * @since 2.2.0
     */
    static <T> ConfigurationHelper<T> newInstance(T bean, Mapper<T> mapper)
    {
        Objects.requireNonNull(mapper, () -> "The mapper must not be null");
        ConfigurationHelper<T> helper = mapper.configurationHelper(bean);
        return bean != null ? helper : new NullConfigurationHelper<>(helper);
    }

    /**
     * Creates the applicable {@link ConfigurationMerger} object for the type associated with
     * this {@code ConfigurationHelper}.
     *
     * @return a new {@link ConfigurationMerger} instance
     * @since 2.2.0
     */
    ConfigurationMerger<T> configurationMerger();

}
