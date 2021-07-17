package net.obvj.confectory;

import java.io.InputStream;
import java.util.Optional;

import net.obvj.confectory.helper.ConfigurationHelper;
import net.obvj.confectory.helper.NullConfigurationHelper;
import net.obvj.confectory.mapper.Mapper;
import net.obvj.confectory.source.Source;

/**
 * An object that contains configuration data from a specific source, as well as related
 * metadata.
 * <p>
 * A {@code Configuration} may also be defined as the union of a configuration
 * {@link Source} and a configuration {@link Mapper}, producing either a properties list,
 * a JSON object, or a user-defined bean.
 * <p>
 * Each {@code Configuration} may be assigned a namespace and precedence number, which
 * determines an order of importance. So, once stored in a common configuration container,
 * the object with the highest precedence value will be chosen first, taking precedence
 * over the other ones in the same namespace.
 * <p>
 * A {@code Configuration} may also be optional, which means that the configuration will
 * be loaded quietly.
 * <p>
 * <strong>Note:</strong> Use a {@link ConfigurationBuilder} to create a
 * {@code Configuration} object. A builder instance can be retrieved by calling
 * {@link Configuration#builder()}. For example:
 *
 * <blockquote>
 *
 * <pre>
 * {@code Configuration<Properties> config = Configuration.<Properties>builder()}
 * {@code         .source(new ClasspathFileSource<>("my.properties"))}
 * {@code         .mapper(new PropertiesMapper())}
 * {@code         .namespace("my-properties")}
 * {@code         .precedence(10)}
 * {@code         .build();}
 * </pre>
 *
 * </blockquote>
 *
 * @param <T> the target configuration type
 *
 * @see Source
 * @see Mapper
 * @see ConfigurationBuilder
 *
 * @author oswaldo.bapvic.jr (Oswaldo Junior)
 * @since 0.1.0
 */
public final class Configuration<T>
{
    private final String namespace;
    private final short precedence;
    private final Source<T> source;
    private final Mapper<InputStream, T> mapper;
    private final boolean optional;
    private final Optional<T> bean;
    private final ConfigurationHelper<T> helper;

    protected Configuration(ConfigurationBuilder<T> builder)
    {
        this.namespace = builder.namespace;
        this.precedence = builder.precedence;
        this.source = builder.source;
        this.mapper = builder.mapper;
        this.optional = builder.optional;
        this.bean = load(builder);
        this.helper = getConfigurationMapper();
    }

    private Optional<T> load(ConfigurationBuilder<T> builder)
    {
        if (optional)
        {
            return source.loadQuietly(mapper);
        }
        T value = source.load(mapper);
        return Optional.ofNullable(value);
    }

    private ConfigurationHelper<T> getConfigurationMapper()
    {
        if (optional && !bean.isPresent())
        {
            return new NullConfigurationHelper<>();
        }
        return mapper.configurationHelper(bean.get());
    }

    /**
     * Creates a configuration builder.
     *
     * @param <T> the target configuration type
     * @return a new {@link ConfigurationBuilder}
     */
    public static <T> ConfigurationBuilder<T> builder()
    {
        return new ConfigurationBuilder<>();
    }

    /**
     * @return the namespace
     */
    public String getNamespace()
    {
        return namespace;
    }

    /**
     * @return the precedence
     */
    public short getPrecedence()
    {
        return precedence;
    }

    /**
     * @return the source
     */
    public Source<T> getSource()
    {
        return source;
    }

    /**
     * @return the optional
     */
    public boolean isOptional()
    {
        return optional;
    }

    /**
     * @return the helper
     */
    public ConfigurationHelper<T> getHelper()
    {
        return helper;
    }

    /**
     * @return the value
     */
    public Optional<T> getBean()
    {
        return bean;
    }

    public boolean getBooleanProperty(String key)
    {
        return helper.getBooleanProperty(key);
    }

    public int getIntProperty(String key)
    {
        return helper.getIntProperty(key);
    }

    public long getLongProperty(String key)
    {
        return helper.getLongProperty(key);
    }

    public double getDoubleProperty(String key)
    {
        return helper.getDoubleProperty(key);
    }

    public String getStringProperty(String key)
    {
        return helper.getStringProperty(key);
    }

    public Optional<String> getOptionalStringProperty(String key)
    {
        return helper.getOptionalStringProperty(key);
    }

}
