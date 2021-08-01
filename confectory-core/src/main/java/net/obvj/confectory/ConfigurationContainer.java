package net.obvj.confectory;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import net.obvj.confectory.helper.nullvalue.NullValueProvider;
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
 * Each container may have a custom {@link DataFetchStrategy} and
 * {@link NullValueProvider}. Both objects may be specified at container construction time
 * and modified using the setter methods at anytime. If not specified, the container uses
 * the default objects configured via {@link ConfectorySettings}.
 * <p>
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
    private NullValueProvider nullValueProvider;

    /**
     * Builds a new {@code ConfigurationContainer} with an arbitrary number of preset
     * {@code Configuration} objects to be registered.
     *
     * @param configs an arbitrary number of {@code Configuration} objects (zero or more) to
     *                be registered at constructor time
     */
    public ConfigurationContainer(Configuration<?>... configs)
    {
        this(null, null, configs);
    }

    /**
     * Builds a new {@code ConfigurationContainer} with a custom {@link DataFetchStrategy} and
     * an arbitrary number of preset {@code Configuration} objects.
     *
     * @param dataFetchStrategy an optional {@link DataFetchStrategy} to be applied by this
     *                          container; {@code null} is allowed and means the default
     *                          strategy will be applied
     * @param configs           an arbitrary number of {@code Configuration} objects (zero or
     *                          more) to be registered at constructor time
     */
    public ConfigurationContainer(DataFetchStrategy dataFetchStrategy, Configuration<?>... configs)
    {
        this(dataFetchStrategy, null, configs);
    }

    /**
     * Builds a new {@code ConfigurationContainer} with a custom {@link NullValueProvider} and
     * an arbitrary number of preset {@code Configuration} objects.
     *
     * @param nullValueProvider an optional {@link NullValueProvider} to be used when keys are
     *                          not found; {@code null} is allowed and means the default
     *                          provider will be applied
     * @param configs           an arbitrary number of {@code Configuration} objects (zero or
     *                          more) to be registered at constructor time
     */

    public ConfigurationContainer(NullValueProvider nullValueProvider, Configuration<?>... configs)
    {
        this(null, nullValueProvider, configs);
    }

    /**
     * Builds a new {@code ConfigurationContainer} with custom {@link DataFetchStrategy} and
     * {@link NullValueProvider} and an arbitrary number of preset {@code Configuration}
     * objects.
     *
     * @param dataFetchStrategy an optional {@link DataFetchStrategy} to be applied by this
     *                          container; {@code null} is allowed and means the default
     *                          strategy will be applied
     * @param nullValueProvider an optional {@link NullValueProvider} to be used when keys are
     *                          not found; {@code null} is allowed and means the default
     *                          provider will be applied
     * @param configs           an arbitrary number of {@code Configuration} objects (zero or
     *                          more) to be registered at constructor time
     */
    public ConfigurationContainer(DataFetchStrategy dataFetchStrategy, NullValueProvider nullValueProvider,
            Configuration<?>... configs)
    {
        ConfectorySettings settings = ConfectorySettings.getInstance();
        setDataFetchStrategy(ObjectUtils.defaultIfNull(dataFetchStrategy, settings.getDefaultDataFetchStrategy()));
        setNullValueProvider(ObjectUtils.defaultIfNull(nullValueProvider, settings.getDefaultNullValueProvider()));

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
     * Returns the {@code NullValueProvider} associated with this container.
     *
     * @return a {@link NullValueProvider} instance
     */
    public NullValueProvider getNullValueProvider()
    {
        return nullValueProvider;
    }

    /**
     * Defines a custom {@code NullValueProvider} for this container.
     *
     * @param provider the {@link NullValueProvider} to set; not null
     * @throws NullPointerException if the specified {@code provider} is null
     */
    public void setNullValueProvider(NullValueProvider provider)
    {
        nullValueProvider = Objects.requireNonNull(provider, "null is not allowed");
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
     * Returns the {@code boolean} value associated with the specified {@code key} in the
     * default namespace.
     *
     * @param key the property key
     * @return the {@code boolean} value associated with the specified {@code key}
     */
    public boolean getBooleanProperty(String key)
    {
        return getBooleanProperty(DEFAULT_NAMESPACE, key);
    }

    /**
     * Returns the {@code boolean} value associated with the specified {@code key} in the
     * specified {@code namespace}.
     *
     * @param namespace the namespace to be used
     * @param key       the property key
     * @return the {@code boolean} value associated with the specified {@code key}
     */
    public boolean getBooleanProperty(String namespace, String key)
    {
        return getProperty(namespace, config -> config.getBooleanProperty(key), NullValueProvider::getBooleanValue);
    }

    /**
     * Returns the {@code int} value associated with the specified {@code key} in the default
     * namespace.
     *
     * @param key the property key
     * @return the {@code int} value associated with the specified {@code key}
     */
    public int getIntProperty(String key)
    {
        return getIntProperty(DEFAULT_NAMESPACE, key);
    }

    /**
     * Returns the {@code int} value associated with the specified {@code key} in the
     * specified {@code namespace}.
     *
     * @param namespace the namespace to be used
     * @param key       the property key
     * @return the {@code int} value associated with the specified {@code key}
     */
    public int getIntProperty(String namespace, String key)
    {
        return getProperty(namespace, config -> config.getIntProperty(key), NullValueProvider::getIntValue);
    }

    /**
     * Returns the {@code long} value associated with the specified {@code key} in the default
     * namespace.
     *
     * @param key the property key
     * @return the {@code long} value associated with the specified {@code key}
     */
    public long getLongProperty(String key)
    {
        return getLongProperty(DEFAULT_NAMESPACE, key);
    }

    /**
     * Returns the {@code long} value associated with the specified {@code key} in the
     * specified {@code namespace}.
     *
     * @param namespace the namespace to be used
     * @param key       the property key
     * @return the {@code long} value associated with the specified {@code key}
     */
    public long getLongProperty(String namespace, String key)
    {
        return getProperty(namespace, config -> config.getLongProperty(key), NullValueProvider::getLongValue);
    }

    /**
     * Returns the {@code double} value associated with the specified {@code key} in the
     * default namespace.
     *
     * @param key the property key
     * @return the {@code double} value associated with the specified {@code key}
     */
    public double getDoubleProperty(String key)
    {
        return getDoubleProperty(DEFAULT_NAMESPACE, key);
    }

    /**
     * Returns the {@code double} value associated with the specified {@code key} in the
     * specified {@code namespace}.
     *
     * @param namespace the namespace to be used
     * @param key       the property key
     * @return the {@code double} value associated with the specified {@code key}
     */
    public double getDoubleProperty(String namespace, String key)
    {
        return getProperty(namespace, config -> config.getDoubleProperty(key), NullValueProvider::getDoubleValue);
    }

    /**
     * Returns the {@code String} value associated with the specified {@code key} in the
     * default namespace.
     *
     * @param key the property key
     * @return the {@code String} value associated with the specified {@code key}
     */
    public String getStringProperty(String key)
    {
        return getStringProperty(DEFAULT_NAMESPACE, key);
    }

    /**
     * Returns the {@code String} value associated with the specified {@code key} in the
     * specified {@code namespace}.
     *
     * @param namespace the namespace to be used
     * @param key       the property key
     * @return the {@code String} value associated with the specified {@code key}
     */
    public String getStringProperty(String namespace, String key)
    {
        return getProperty(namespace, config -> config.getStringProperty(key), NullValueProvider::getStringValue);
    }

    /**
     * Template method for retrieving properties from the {@code configMap}.
     *
     * @param <T>               the property return type
     * @param namespace         the namespace which property is to be fetched
     * @param mainFunction      the main data fetch function; applies a particular method to
     *                          the {@code Configuration} objects in process
     * @param nullValueSupplier a null-value supplying function
     *
     * @return the value of the property evaluated by the {@code mainFunction}, or the one
     *         specified by the {@code nullValueFunction} (on top of the instance-level
     *         {@code nullValueProvider})
     */
    private <T> T getProperty(String namespace, Function<Configuration<?>, T> mainFunction,
            Function<NullValueProvider, T> nullValueSupplier)
    {
        Iterator<Configuration<?>> iterator = getConfigurationStream(namespace).iterator();
        while (iterator.hasNext())
        {
            Configuration<?> config = iterator.next();
            T value = mainFunction.apply(config);

            // We use the provider defined at Configuration level to test the value
            T nullValue = nullValueSupplier.apply(config.getNullValueProvider());
            if (!nullValue.equals(value))
            {
                return value;
            }
        }
        // We use the provider defined at container level to return a default value
        return nullValueSupplier.apply(nullValueProvider);
    }

    /**
     * Retrieves a stream of {@link Configuration} objects on a given namespace.
     *
     * @param namespace the namespace to be searched
     * @return a stream of {@link Configuration} objects or an empty set, never {@code null}
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
     * Returns the number of {@code Configuration} objects associated with the specified
     * {@code namespace} in this container.
     *
     * @param namespace the namespace to be tested
     * @return the number of {@code Configuration} objects associated with the specified
     *         {@code namespace}
     */
    public long size(String namespace)
    {
        return getConfigurationStream(namespace).count();
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
