package net.obvj.confectory;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
        protected List<Configuration<?>> getConfigurationList(String namespace,
                Map<String, List<Configuration<?>>> configMap)
        {
            return configMap.getOrDefault(parseNamespace(namespace), Collections.emptyList());
        }
    },

    /**
     * Retrieves data from all {@code Configuration} objects regardless of their namespaces
     * <b>when no namespace specified</b> during data fetch.
     */
    LENIENT
    {
        @Override
        protected List<Configuration<?>> getConfigurationList(String namespace,
                Map<String, List<Configuration<?>>> configMap)
        {
            if (StringUtils.isEmpty(namespace))
            {
                // Flatten and re-order the configuration list
                return configMap.values().stream()
                        .flatMap(Collection::stream)
                        .sorted(new ConfigurationComparator())
                        .collect(Collectors.toList());
            }
            return STRICT.getConfigurationList(namespace, configMap);
        }
    };

    /**
     * Retrieves a list of {@link Configuration} objects based on the specified
     * {@code namespace}.
     *
     * @param namespace the namespace to be searched
     * @param configMap the source map
     * @return a list of {@link Configuration} objects according to the selected strategy,
     *         never {@code null}
     */
    protected abstract List<Configuration<?>> getConfigurationList(String namespace,
            Map<String, List<Configuration<?>>> configMap);

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
