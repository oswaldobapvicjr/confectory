package net.obvj.confectory;

import java.util.*;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import net.obvj.confectory.config.ConfectoryConfiguration;
import net.obvj.confectory.helper.provider.NullValueProvider;
import net.obvj.confectory.util.ConfigurationComparator;

/**
 * An object that holds multiple {@code Configuration} objects and retrieves configuration
 * data seamlessly, by namespace and key.
 * <p>
 * The {@code Configuration} objects are sorted by precedence (from highest to lowest).
 * So, in case of key collision, the object with the highest precedence will be selected.
 * <p>
 * {@code Configuration} objects without a declared namespace can have their data queried
 * using the single-argument getter methods.
 *
 * @author oswaldo.bapvic.jr (Oswaldo Junior)
 * @since 0.1.0
 *
 * @see Configuration
 */
public class ConfigurationContainer
{
    private static final String DEFAULT_NAMESPACE = "";

    private Map<String, List<Configuration<?>>> configMap = new HashMap<>();
    private NullValueProvider nullValueProvider;

    /**
     * Builds a new {@code ConfigurationContainer} with an arbitrary number of preset
     * {@code Configuration} objects to be registered,
     *
     * @param configs an arbitrary number of {@code Configuration} objects (zero or more) to
     *                be registered at constructor time
     */
    public ConfigurationContainer(Configuration<?>... configs)
    {
        this(null, configs);
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
        this.nullValueProvider = ObjectUtils.defaultIfNull(nullValueProvider,
                ConfectoryConfiguration.getInstance().getDefaultNullValueProvider());

        Arrays.stream(configs).forEach(this::add);
    }

    public void add(Configuration<?> configuration)
    {
        String namespace = safeNamespace(configuration.getNamespace());
        List<Configuration<?>> configList = configMap.computeIfAbsent(namespace, k -> new ArrayList<>());
        configList.add(configuration);
        configList.sort(new ConfigurationComparator());
    }

    public void clear()
    {
        configMap.clear();
    }

    public boolean getBooleanProperty(String key)
    {
        return getBooleanProperty(DEFAULT_NAMESPACE, key);
    }

    public boolean getBooleanProperty(String namespace, String key)
    {
        // TODO Auto-generated method stub
        return false;
    }

    public int getIntProperty(String key)
    {
        return getIntProperty(DEFAULT_NAMESPACE, key);
    }

    public int getIntProperty(String namespace, String key)
    {
        // TODO Auto-generated method stub
        return 0;
    }

    public long getLongProperty(String key)
    {
        return getLongProperty(DEFAULT_NAMESPACE, key);
    }

    public long getLongProperty(String namespace, String key)
    {
        // TODO Auto-generated method stub
        return 0;
    }

    public double getDoubleProperty(String key)
    {
        return getDoubleProperty(DEFAULT_NAMESPACE, key);
    }

    public double getDoubleProperty(String namespace, String key)
    {
        // TODO Auto-generated method stub
        return 0;
    }

    public String getStringProperty(String key)
    {
        return getStringProperty(DEFAULT_NAMESPACE, key);
    }

    public String getStringProperty(String namespace, String key)
    {
        for (Configuration<?> config : getSafely(namespace))
        {
            String value = config.getStringProperty(key);
            if (!config.getNullValueProvider().isNull(value))
            {
                return value;
            }
        }
        return nullValueProvider.getStringValue();
    }

    /**
     * Retrieves a list of {@link Configuration} objects on a given namespace.
     *
     * @param namespace the namespace to be searched
     * @return a list of {@link Configuration} objects or an empty list, never {@code null}
     */
    private List<Configuration<?>> getSafely(String namespace)
    {
        return configMap.getOrDefault(safeNamespace(namespace), Collections.emptyList());
    }

    /**
     * Returns either the passed namespace, or the value defined for
     * {@code DEFAULT_NAMESPACE}, if the passed argument is empty or null
     *
     * @param namespace the namespace to the checked
     * @return the passed namespace, or the value of {@code DEFAULT_NAMESPACE}, never
     *         {@code null}
     */
    private String safeNamespace(String namespace)
    {
        return StringUtils.defaultString(namespace, DEFAULT_NAMESPACE);
    }

}
