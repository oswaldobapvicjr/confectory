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
 * @author FernandoNSC (Fernando Tiannamen)
 * @since 0.1.0
 *
 * @see ConfigurationContainer
 */
public enum DataFetchStrategy
{
    /**
     * Retrieves <b>sorted</b> data from {@code Configuration} objects declared with a
     * specific namespace.
     * <p>
     * If no namespace is specified during data fetch, only {@link Configuration} objects
     * <b>without a namespace defined</b> will be searched.
     * <p>
     * {@code Configuration} objects are sorted from the highest to the lowest precedence
     * attribute, as determined by the {@link ConfigurationComparator}.
     */
    STRICT
    {
        @Override
        Stream<Configuration<?>> getConfigurationStream(String namespace,
                Map<String, Set<Configuration<?>>> configMap)
        {
            return STRICT_UNSORTED.getConfigurationStream(namespace, configMap)
                    .sorted(new ConfigurationComparator());
        }
    },

    /**
     * Retrieves <b>unsorted</b> data from {@code Configuration} objects declared with a
     * specific namespace.
     * <p>
     * If no namespace is specified during data fetch, only {@link Configuration} objects
     * <b>without a namespace defined</b> will be searched.
     *
     * @since 0.2.0
     */
    STRICT_UNSORTED
    {
        @Override
        Stream<Configuration<?>> getConfigurationStream(String namespace,
                Map<String, Set<Configuration<?>>> configMap)
        {
            return configMap.getOrDefault(parseNamespace(namespace), Collections.emptySet())
                    .stream();
        }
    },

    /**
     * Retrieves <b>sorted</b> data from all {@code Configuration} objects regardless of their
     * namespaces <b>when no namespace specified</b> during data fetch.
     * <p>
     * {@code Configuration} objects are sorted from the highest to the lowest precedence
     * attribute, as determined by the {@link ConfigurationComparator}.
     */
    LENIENT
    {
        @Override
        Stream<Configuration<?>> getConfigurationStream(String namespace,
                Map<String, Set<Configuration<?>>> configMap)
        {
            if (StringUtils.isEmpty(namespace))
            {
                // Flatten and re-order the configuration list
                return LENIENT_UNSORTED.getConfigurationStream(namespace, configMap)
                        .sorted(new ConfigurationComparator());
            }
            return STRICT.getConfigurationStream(namespace, configMap);
        }
    },

    /**
     * Retrieves <b>unsorted</b> data from all {@code Configuration} objects regardless of
     * their namespaces <b>when no namespace specified</b> during data fetch.
     *
     * @since 0.2.0
     */
    LENIENT_UNSORTED
    {
        @Override
        Stream<Configuration<?>> getConfigurationStream(String namespace,
                Map<String, Set<Configuration<?>>> configMap)
        {
            if (StringUtils.isEmpty(namespace))
            {
                // Flatten and re-order the configuration list
                return configMap.values().stream()
                        .flatMap(Collection::stream);
            }
            return STRICT_UNSORTED.getConfigurationStream(namespace, configMap);
        }
    };


    /**
     * Retrieves a stream of sorted {@link Configuration} objects based on the specified
     * {@code namespace}.
     *
     * @param namespace the namespace to be searched
     * @param configMap the source map
     * @return a stream of {@link Configuration} objects according to the selected strategy,
     *         never {@code null}
     */
    abstract Stream<Configuration<?>> getConfigurationStream(String namespace,
            Map<String, Set<Configuration<?>>> configMap);

    /**
     * Returns either the passed namespace, or a default value, if the passed argument is
     * empty or null.
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
