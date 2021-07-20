package net.obvj.confectory;

import java.util.Objects;

import org.apache.commons.lang3.StringUtils;

import net.obvj.confectory.mapper.Mapper;
import net.obvj.confectory.source.Source;

/**
 * A mutable object that supports the creation of immutable {@link Configuration} objects.
 * <p>
 * For example:
 *
 * <blockquote>
 *
 * <pre>
 * {@code Configuration<Properties> config = new ConfigurationBuilder<Properties>()}
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
 * @author oswaldo.bapvic.jr (Oswaldo Junior)
 * @since 0.1.0
 *
 * @see Configuration
 */
public class ConfigurationBuilder<T> implements ConfigurationMetadataRetriever<T>
{
    private String namespace;
    private int precedence;
    private Source<T> source;
    private Mapper<T> mapper;
    private boolean optional;

    /**
     * Creates a new, empty {@code ConfigurationBuilder}.
     */
    public ConfigurationBuilder()
    {
        this(null);
    }

    /**
     * Creates a new {@code ConfigurationBuilder} filled with the attributes of an existing
     * base {@code Configuration}.
     *
     * @param source the {@code Configuration} whose attributes are to be copied; {@code null}
     *               is allowed
     */
    public ConfigurationBuilder(Configuration<T> sourceConfiguration)
    {
        if (sourceConfiguration != null)
        {
            namespace = sourceConfiguration.getNamespace();
            precedence = sourceConfiguration.getPrecedence();
            source = sourceConfiguration.getSource();
            mapper = sourceConfiguration.getMapper();
            optional = sourceConfiguration.isOptional();
        }
    }

    /**
     * Declares a namespace to be assigned to the new {@code Configuration} object.
     * <p>
     * <strong>Note:</strong> The namespace is optional, but usually recommended to organize
     * the scope of the target object and to prevent key collisions.
     *
     * @param namespace the namespace to set; {@code null} is allowed
     * @return a reference to this same {@code ConfigurationBuilder} for chained calls
     */
    public ConfigurationBuilder<T> namespace(String namespace)
    {
        this.namespace = namespace;
        return this;
    }

    /**
     * Defines the level of precedence of the new {@code Configuration} compared to similar
     * objects in the same namespace.
     * <p>
     * In a common configuration container, the object with the highest precedence level may
     * be selected first in the occurrence of a key collision in the same namespace.
     *
     * @param precedence an integer number representing the order of importance given to the
     *                   target configuration
     * @return a reference to this same {@code ConfigurationBuilder} for chained calls
     */
    public ConfigurationBuilder<T> precedence(int precedence)
    {
        this.precedence = precedence;
        return this;
    }

    /**
     * Defines the {@link Source} of the new {@code Configuration}.
     *
     * @param source the {@link Source} to be set; <strong>not</strong> null
     * @return a reference to this same {@code ConfigurationBuilder} for chained calls
     */
    public ConfigurationBuilder<T> source(Source<T> source)
    {
        this.source = source;
        return this;
    }

    /**
     * Defines the {@link Mapper} of the new {@code Configuration}.
     *
     * @param mapper the {@link Mapper} to be set; <strong>not</strong> null
     * @return a reference to this same {@code ConfigurationBuilder} for chained calls
     */
    public ConfigurationBuilder<T> mapper(Mapper<T> mapper)
    {
        this.mapper = mapper;
        return this;
    }

    /**
     * Marks the new {@code Configuration} as optional.
     * <p>
     * The configuration source will be loaded quietly, i.e., not throwing an exception if the
     * source is not found or not loaded successfully.
     *
     * @return a reference to this same {@code ConfigurationBuilder} for chained calls
     */
    public ConfigurationBuilder<T> optional()
    {
        this.optional = true;
        return this;
    }

    /**
     * Marks the new {@code Configuration} as required (default).
     *
     * @return a reference to this same {@code ConfigurationBuilder} for chained calls
     */
    public ConfigurationBuilder<T> required()
    {
        this.optional = false;
        return this;
    }

    /**
     * Builds the target {@code Configuration}.
     *
     * @returns a new {@link Configuration} object
     * @throws NullPointerException         if either the {@code Source} or {@code Mapper}
     *                                      configuration parameters are missing
     * @throws ConfigurationSourceException in the event of a failure loading the
     *                                      configuration source
     */
    public Configuration<T> build()
    {
        Objects.requireNonNull(source, "The configuration source must not be null");
        Objects.requireNonNull(mapper, "The configuration mapper must not be null");
        namespace = StringUtils.defaultString(namespace);
        return new Configuration<>(this);
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

}
