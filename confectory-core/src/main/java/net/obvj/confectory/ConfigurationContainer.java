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

import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import net.obvj.confectory.settings.ConfectorySettings;

/**
 * An object that holds multiple {@code Configuration} objects and retrieves configuration
 * data seamlessly, by namespace and key.
 * <p>
 * The {@code Configuration} objects are sorted by precedence (from highest to lowest).
 * So, in case of key collision, the object with the highest precedence will be selected
 * first. The container may still select other lower-precedence {@code Configuration}
 * objects if a key is not found in the highest-precedence {@code Configuration}.
 * <p>
 * To create a new empty container, use the default constructor
 * {@code new ConfigurationContainer()}. To create a new container with preset
 * {@code Configuration} objects, pass them as "var-args".
 * <p>
 * Use the {@code add(Configuration)} method at any time to register new objects inside
 * the container.
 * <p>
 * To retrieve {@code Configuration} data, use any of the getter methods, specifying a
 * namespace and key.
 * <p>
 * Single-argument getter methods can be used to retrieve data from {@code Configuration}
 * objects that do not have a declared namespace. These methods may also retrieve data
 * from all {@code Configuration} objects if the selected data-fetch strategy is
 * {@code LENIENT}.
 * <p>
 * Each container may have a custom {@link DataFetchStrategy} which may be specified at
 * container construction time and modified using the setter method anytime. If not
 * specified, the container uses the default choice configured via
 * {@link ConfectorySettings}.
 * <p>
 * <strong>IMPORTANT:</strong> This class works only with map-based {@code Configuration}
 * objects (e.g.: {@code Properties}, {@code JSONObject}, {@code JsonNode}, etc.). In
 * other words, only "container" objects which values can be accessed using either a key
 * or path expression (e.g. {@code JSONPath}). POJO-based {@code Configuration} objects
 * are <strong>not</strong> supported inside a the {@link ConfigurationContainer}.
 *
 * @author oswaldo.bapvic.jr (Oswaldo Junior)
 * @since 0.1.0
 *
 * @see Configuration
 * @see DataFetchStrategy
 */
public class ConfigurationContainer
{
    protected static final String DEFAULT_NAMESPACE = "";

    private Map<String, Set<Configuration<?>>> configMap = new HashMap<>();
    private DataFetchStrategy dataFetchStrategy;

    /**
     * Builds a new {@code ConfigurationContainer} with an arbitrary number of preset
     * {@code Configuration} objects to be registered.
     *
     * @param configs an arbitrary number of {@code Configuration} objects (zero or more) to
     *                be registered at constructor time
     */
    public ConfigurationContainer(Configuration<?>... configs)
    {
        this(null, configs);
    }

    /**
     * Builds a new {@code ConfigurationContainer} with a custom {@link DataFetchStrategy} and
     * an arbitrary number of preset {@code Configuration} objects.
     *
     * @param dataFetchStrategy an optional {@link DataFetchStrategy} to be applied by this
     *                          container; {@code null} is allowed and indicates that the
     *                          default strategy defined in {@link Confectory#settings()} will
     *                          be applied
     * @param configs           an arbitrary number of {@code Configuration} objects (zero or
     *                          more) to be registered at constructor time
     * @see ConfectorySettings
     */
    public ConfigurationContainer(DataFetchStrategy dataFetchStrategy, Configuration<?>... configs)
    {
        ConfectorySettings settings = Confectory.settings();
        setDataFetchStrategy(ObjectUtils.defaultIfNull(dataFetchStrategy, settings.getDataFetchStrategy()));

        Arrays.stream(configs).forEach(this::add);
    }

    /**
     * Returns the {@code DataFetchStrategy} associated with this container.
     *
     * @return a {@link DataFetchStrategy}
     */
    public DataFetchStrategy getDataFetchStrategy()
    {
        return dataFetchStrategy;
    }

    /**
     * Defines a custom {@code DataFetchStrategy} for this container.
     *
     * @param strategy the {@link DataFetchStrategy} to set; not null
     * @throws NullPointerException if the specified {@code strategy} is null
     */
    public void setDataFetchStrategy(DataFetchStrategy strategy)
    {
        dataFetchStrategy = Objects.requireNonNull(strategy, "the DataFetchStrategy must not be null");
    }

    /**
     * Adds the specified {@code Configuration} to this container.
     *
     * @param configuration the {@link Configuration} to be added to the container
     */
    public void add(Configuration<?> configuration)
    {
        String namespace = parseNamespace(configuration.getNamespace());
        Set<Configuration<?>> configSet = configMap.computeIfAbsent(namespace, k -> new HashSet<>());
        configSet.add(configuration);
    }

    /**
     * Copies all of the {@code Configuration} objects from another container to this
     * container.
     *
     * @param source the source container which {@code Configuration} objects are to be stored
     *               in this container; {@code null} is allowed
     */
    public void addAll(ConfigurationContainer source)
    {
        if (source != null)
        {
            source.configMap.values().stream().flatMap(Collection::stream).forEach(this::add);
        }
    }

    /**
     * Removes all of the {@code Configuration} objects from this container.
     */
    public void clear()
    {
        configMap.clear();
    }

    /**
     * Returns the {@code Boolean} object associated with the specified {@code key} in the
     * default namespace (or in all namespaces depending on the {@link DataFetchStrategy} in
     * scope).
     *
     * @param key the object key (or path)
     * @return the {@code Boolean} object associated with the specified {@code key};
     *         {@code null} if not found
     *
     * @see DataFetchStrategy
     */
    public Boolean getBoolean(String key)
    {
        return getBoolean(DEFAULT_NAMESPACE, key);
    }

    /**
     * Returns the {@code Boolean} object associated with the specified {@code key} in the
     * specified {@code namespace}.
     *
     * @param namespace the namespace to be used
     * @param key       the object key (or path)
     * @return the {@code Boolean} object associated with the specified {@code key};
     *         {@code null} if not found
     */
    public Boolean getBoolean(String namespace, String key)
    {
        return getValue(namespace, config -> config.getBoolean(key));
    }

    /**
     * Returns the {@code Integer} object associated with the specified {@code key} in the
     * default namespace (or in all namespaces depending on the {@link DataFetchStrategy} in
     * scope).
     *
     * @param key the object key (or path)
     * @return the {@code Integer} object associated with the specified {@code key};
     *         {@code null} if not found
     *
     * @see DataFetchStrategy
     */
    public Integer getInteger(String key)
    {
        return getInteger(DEFAULT_NAMESPACE, key);
    }

    /**
     * Returns the {@code Integer} object associated with the specified {@code key} in the
     * specified {@code namespace}.
     *
     * @param namespace the namespace to be used
     * @param key       the object key (or path)
     * @return the {@code Integer} object associated with the specified {@code key};
     *         {@code null} if not found
     */
    public Integer getInteger(String namespace, String key)
    {
        return getValue(namespace, config -> config.getInteger(key));
    }

    /**
     * Returns the {@code Long} object associated with the specified {@code key} in the
     * default namespace (or in all namespaces depending on the {@link DataFetchStrategy} in
     * scope).
     *
     * @param key the object key (or path)
     * @return the {@code Long} object associated with the specified {@code key}; {@code null}
     *         if not found
     *
     * @see DataFetchStrategy
     */
    public Long getLong(String key)
    {
        return getLong(DEFAULT_NAMESPACE, key);
    }

    /**
     * Returns the {@code Long} object associated with the specified {@code key} in the
     * specified {@code namespace}.
     *
     * @param namespace the namespace to be used
     * @param key       the object key (or path)
     * @return the {@code Long} object associated with the specified {@code key}; {@code null}
     *         if not found
     */
    public Long getLong(String namespace, String key)
    {
        return getValue(namespace, config -> config.getLong(key));
    }

    /**
     * Returns the {@code Double} object associated with the specified {@code key} in the
     * default namespace (or in all namespaces depending on the {@link DataFetchStrategy} in
     * scope).
     *
     * @param key the object key (or path)
     * @return the {@code Double} value associated with the specified {@code key};
     *         {@code null} if not found
     *
     * @see DataFetchStrategy
     */
    public Double getDouble(String key)
    {
        return getDouble(DEFAULT_NAMESPACE, key);
    }

    /**
     * Returns the {@code Double} object associated with the specified {@code key} in the
     * specified {@code namespace}.
     *
     * @param namespace the namespace to be used
     * @param key       the object key (or path)
     * @return the {@code Double} object associated with the specified {@code key};
     *         {@code null} if not found
     */
    public Double getDouble(String namespace, String key)
    {
        return getValue(namespace, config -> config.getDouble(key));
    }

    /**
     * Returns the {@code String} object associated with the specified {@code key} in the
     * default namespace (or in all namespaces depending on the {@link DataFetchStrategy} in
     * scope).
     *
     * @param key the object key (or path)
     * @return the {@code String} object associated with the specified {@code key};
     *         {@code null} if not found
     *
     * @see DataFetchStrategy
     */
    public String getString(String key)
    {
        return getString(DEFAULT_NAMESPACE, key);
    }

    /**
     * Returns the {@code String} object associated with the specified {@code key} in the
     * specified {@code namespace}.
     *
     * @param namespace the namespace to be used
     * @param key       the object key (or path)
     * @return the {@code String} object associated with the specified {@code key};
     *         {@code null} if not found
     */
    public String getString(String namespace, String key)
    {
        return getValue(namespace, config -> config.getString(key));
    }

    /**
     * Template method for retrieving properties from the {@code configMap}.
     *
     * @param <T>          the value return type
     * @param namespace    the namespace which property is to be fetched
     * @param mainFunction the main data fetch function; applies a particular method to the
     *                     {@code Configuration} objects in process
     *
     * @return the value evaluated by the {@code mainFunction}, or {@code null} if not found
     */
    protected <T> T getValue(String namespace, Function<Configuration<?>, T> mainFunction)
    {
        Iterator<Configuration<?>> iterator = getConfigurationStream(namespace).iterator();
        while (iterator.hasNext())
        {
            Configuration<?> config = iterator.next();
            T value = mainFunction.apply(config);

            if (value != null)
            {
                return value;
            }
        }
        return null;
    }

    /**
     * Retrieves a stream of {@link Configuration} objects on a given namespace.
     *
     * @param namespace the namespace to be searched
     * @return a stream of {@link Configuration} objects or an empty stream, never
     *         {@code null}
     */
    private Stream<Configuration<?>> getConfigurationStream(String namespace)
    {
        return dataFetchStrategy.getConfigurationStream(namespace, configMap);
    }

    /**
     * Returns either the passed namespace, or the value defined for
     * {@code DEFAULT_NAMESPACE}, if the passed argument is empty or null
     *
     * @param namespace the namespace to the checked
     * @return the passed namespace, or the value of {@code DEFAULT_NAMESPACE}, never
     *         {@code null}
     */
    private String parseNamespace(String namespace)
    {
        return StringUtils.defaultString(namespace, DEFAULT_NAMESPACE);
    }

    /**
     * Returns the number of {@code Configuration} objects in this container.
     *
     * @return the number of {@code Configuration} objects in this container
     * @since 1.0.0
     */
    public long size()
    {
        return configMap.values().stream().mapToInt(Set::size).sum();
    }

    /**
     * Returns the number of {@code Configuration} objects associated with the specified
     * {@code namespace} in this container.
     *
     * @param namespace the namespace to be tested
     * @return the number of {@code Configuration} objects associated with the specified
     *         {@code namespace}
     */
    public long size(String namespace)
    {
        return configMap.getOrDefault(parseNamespace(namespace), Collections.emptySet()).size();
    }

    /**
     * Returns {@code true} if this container contains no {@code Configuration} objects.
     *
     * @return {@code true} if this container contains no {@code Configuration} objects
     * @since 1.0.0
     */
    public boolean isEmpty()
    {
        return configMap.isEmpty();
    }

    /**
     * Returns all of the namespaces defined inside this container.
     *
     * @return a set of namespaces
     */
    public Collection<String> getNamespaces()
    {
        return configMap.keySet();
    }

}
