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

package net.obvj.confectory.helper;

import net.obvj.confectory.ConfigurationDataRetriever;
import net.obvj.confectory.helper.nullvalue.NullValueProvider;

/**
 * A base interface for Configuration Helper objects providing methods for retrieving
 * configuration data from different source types.
 *
 * @param <T> the source type which configuration data is to be retrieved
 *
 * @author oswaldo.bapvic.jr (Oswaldo Junior)
 * @since 0.1.0
 */
public interface ConfigurationHelper<T> extends ConfigurationDataRetriever<T>
{
    /**
     * Defines a {@link NullValueProvider} for invalid keys.
     *
     * @param provider the {@link NullValueProvider} to set; not null
     *
     * @throws NullPointerException if the specified provider is null
     */
    void setNullValueProvider(NullValueProvider provider);

    /**
     * @return the {@link NullValueProvider} associated with this helper.
     */
    NullValueProvider getNullValueProvider();

}
