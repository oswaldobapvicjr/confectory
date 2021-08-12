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

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;

import net.obvj.confectory.util.ConfigurationComparator;

/**
 * Enumerates the supported data-fetch strategies for use with a
 * {@code ConfigurationContainer}.
 *
 * @author oswaldo.bapvic.jr (Oswaldo Junior)
 * @since 0.1.0
 *
 * @see ConfigurationContainer
 */
public enum DataFetchStrategy
{
    /**
     * Retrieves data from {@code Configuration} objects declared with a specific namespace.
     * <p>
     * If no namespace is specified during data fetch, only {@link Configuration} objects
     * <b>without a namespace defined</b> will be searched.
     */
    STRICT
    {
        @Override
        protected Stream<Configuration<?>> getConfigurationStream(String namespace,
                Map<String, Set<Configuration<?>>> configMap)
        {
            return configMap.getOrDefault(parseNamespace(namespace), Collections.emptySet()).stream()
                    .sorted(new ConfigurationComparator());
        }
    },

    /**
     * Retrieves data from all {@code Configuration} objects regardless of their namespaces
     * <b>when no namespace specified</b> during data fetch.
     */
    LENIENT
    {
        @Override
        protected Stream<Configuration<?>> getConfigurationStream(String namespace,
                Map<String, Set<Configuration<?>>> configMap)
        {
            if (StringUtils.isEmpty(namespace))
            {
                // Flatten and re-order the configuration list
                return configMap.values().stream()
                        .flatMap(Collection::stream)
                        .sorted(new ConfigurationComparator());
            }
            return STRICT.getConfigurationStream(namespace, configMap);
        }
    };

    /**
     * Retrieves a stream of sorted {@link Configuration} objects based on the specified
     * {@code namespace}.
     * <p>
     * The {@link Configuration} objects may be sorted by precedence (highest to lowest).
     *
     * @param namespace the namespace to be searched
     * @param configMap the source map
     * @return a stream of {@link Configuration} objects according to the selected strategy,
     *         never {@code null}
     */
    protected abstract Stream<Configuration<?>> getConfigurationStream(String namespace,
            Map<String, Set<Configuration<?>>> configMap);

    /**
     * Returns either the passed namespace, or a default value, if the passed argument is
     * empty or null
     *
     * @param namespace the namespace to the checked
     * @return the passed namespace, or the value of
     *         {@link ConfigurationContainer#DEFAULT_NAMESPACE}, never {@code null}
     */
    private static String parseNamespace(String namespace)
    {
        return StringUtils.defaultString(namespace, ConfigurationContainer.DEFAULT_NAMESPACE);
    }
}
