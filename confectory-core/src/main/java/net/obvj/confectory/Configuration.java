package net.obvj.confectory;

import java.util.Optional;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import net.obvj.confectory.helper.ConfigurationHelper;
import net.obvj.confectory.helper.NullConfigurationHelper;
import net.obvj.confectory.helper.provider.NullValueProvider;
import net.obvj.confectory.mapper.Mapper;
import net.obvj.confectory.source.Source;

/**
 * An immutable object that contains configuration data from a specific source, as well as
 * related metadata.
 * <p>
 * A {@code Configuration} may also be defined as a combination a configuration
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
public final class Configuration<T> implements ConfigurationDataRetriever<T>, ConfigurationMetadataRetriever<T>
{
    private final String namespace;
    private final int precedence;
    private final Source<T> source;
    private final Mapper<T> mapper;
    private final boolean optional;
    private final Optional<T> bean;
    private final ConfigurationHelper<T> helper;
    private final NullValueProvider nullValueProvider;

    /**
     * Builds a new {@code Configuration} from the specified {@link ConfigurationBuilder}.
     *
     * @param builder the {@link ConfigurationBuilder} to be built
     */
    protected Configuration(ConfigurationBuilder<T> builder)
    {
        this.namespace = builder.getNamespace();
        this.precedence = builder.getPrecedence();
        this.source = builder.getSource();
        this.mapper = builder.getMapper();
        this.optional = builder.isOptional();
        this.nullValueProvider = builder.getNullValueProvider();

        this.bean = source.load(mapper, optional);
        this.helper = prepareConfigurationHelper();
    }

    private ConfigurationHelper<T> prepareConfigurationHelper()
    {
        ConfigurationHelper<T> configurationHelper = bean.isPresent() ? mapper.configurationHelper(bean.get())
                : new NullConfigurationHelper<>();

        if (nullValueProvider != null)
        {
            configurationHelper.setNullValueProvider(nullValueProvider);
        }
        return configurationHelper;
    }

    /**
     * Creates a new configuration builder.
     *
     * @param <T> the target configuration type
     * @return a new {@link ConfigurationBuilder}
     */
    public static <T> ConfigurationBuilder<T> builder()
    {
        return new ConfigurationBuilder<>();
    }

    @Override
    public String getNamespace()
    {
        return namespace;
    }

    @Override
    public int getPrecedence()
    {
        return precedence;
    }

    @Override
    public Source<T> getSource()
    {
        return source;
    }

    @Override
    public Mapper<T> getMapper()
    {
        return mapper;
    }

    @Override
    public boolean isOptional()
    {
        return optional;
    }

    @Override
    public NullValueProvider getNullValueProvider()
    {
        return nullValueProvider;
    }

    public ConfigurationHelper<T> getHelper()
    {
        return helper;
    }

    @Override
    public Optional<T> getBean()
    {
        return bean;
    }

    @Override
    public boolean getBooleanProperty(String key)
    {
        return helper.getBooleanProperty(key);
    }

    @Override
    public int getIntProperty(String key)
    {
        return helper.getIntProperty(key);
    }

    @Override
    public long getLongProperty(String key)
    {
        return helper.getLongProperty(key);
    }

    @Override
    public double getDoubleProperty(String key)
    {
        return helper.getDoubleProperty(key);
    }

    @Override
    public String getStringProperty(String key)
    {
        return helper.getStringProperty(key);
    }

    @Override
    public String toString()
    {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE).append("namespace", namespace)
                .append("precedence", precedence).append("source", source).toString();
    }

}
