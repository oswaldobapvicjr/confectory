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

import java.util.Objects;
import java.util.Optional;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import net.obvj.confectory.helper.nullvalue.NullValueProvider;
import net.obvj.confectory.mapper.Mapper;
import net.obvj.confectory.source.Source;

/**
 * An immutable object that contains configuration data from a specific source, as well as
 * related metadata.
 * <p>
 * A {@code Configuration} may also be defined as a combination of a {@link Source} and a
 * {@link Mapper}, producing either a properties list, a JSON object, or a user-defined
 * bean.
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
    private final ConfigurationBuilder<T> builder;
    private final String namespace;
    private final int precedence;
    private final Source<T> source;
    private final Mapper<T> mapper;
    private final boolean optional;
    private final boolean lazy;
    private final NullValueProvider nullValueProvider;

    private ConfigurationDataRetriever<T> service;

    /**
     * Builds a new {@code Configuration} from the specified {@link ConfigurationBuilder}.
     *
     * @param builder the {@link ConfigurationBuilder} to be built
     */
    protected Configuration(ConfigurationBuilder<T> builder)
    {
        this.builder = builder;

        this.namespace = builder.getNamespace();
        this.precedence = builder.getPrecedence();
        this.source = builder.getSource();
        this.mapper = builder.getMapper();
        this.optional = builder.isOptional();
        this.lazy = builder.isLazy();
        this.nullValueProvider = builder.getNullValueProvider();

        if (!lazy)
        {
            getService();
        }
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
    public boolean isLazy()
    {
        return lazy;
    }

    @Override
    public NullValueProvider getNullValueProvider()
    {
        return nullValueProvider;
    }

    @Override
    public Optional<T> getBean()
    {
        return getService().getBean();
    }

    @Override
    public boolean getBoolean(String key)
    {
        return getService().getBoolean(key);
    }

    @Override
    public int getInt(String key)
    {
        return getService().getInt(key);
    }

    @Override
    public long getLong(String key)
    {
        return getService().getLong(key);
    }

    @Override
    public double getDouble(String key)
    {
        return getService().getDouble(key);
    }

    @Override
    public String getString(String key)
    {
        return getService().getString(key);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(namespace, source);
    }

    /**
     * Indicates whether some other object is "equal to" this one.
     * <p>
     * Two {@code Configuration} objects can be considered equal if both share the same
     * {@code namespace} and {@code Source}.
     *
     * @param other the other object with which to compare
     *
     * @return {@code true} if this object is the same as the one specified in the argument;
     *         {@code false} otherwise.
     */
    @Override
    public boolean equals(Object other)
    {
        if (this == other)
        {
            return true;
        }
        if (other == null)
        {
            return false;
        }
        if (getClass() != other.getClass())
        {
            return false;
        }
        Configuration<?> otherConfiguration = (Configuration<?>) other;
        return Objects.equals(namespace, otherConfiguration.namespace)
                && Objects.equals(source, otherConfiguration.source);
    }

    @Override
    public String toString()
    {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE).append("namespace", namespace)
                .append("precedence", precedence).append("source", source).toString();
    }

    @Override
    public boolean getMandatoryBoolean(String key)
    {
        return getService().getMandatoryBoolean(key);
    }

    @Override
    public int getMandatoryInt(String key)
    {
        return getService().getMandatoryInt(key);
    }

    @Override
    public long getMandatoryLong(String key)
    {
        return getService().getMandatoryLong(key);
    }

    @Override
    public double getMandatoryDouble(String key)
    {
        return getService().getMandatoryDouble(key);
    }

    @Override
    public String getMandatoryString(String key)
    {
        return getService().getMandatoryString(key);
    }

    private ConfigurationDataRetriever<T> getService()
    {
        if (service == null)
        {
            service = new ConfigurationService<>(builder);
        }
        return service;
    }

}
