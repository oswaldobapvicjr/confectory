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

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import net.obvj.confectory.helper.ConfigurationHelper;
import net.obvj.confectory.helper.NullConfigurationHelper;
import net.obvj.confectory.mapper.Mapper;
import net.obvj.confectory.source.Source;

/**
 * An object that contains configuration data from a specific source, as well as related
 * metadata.
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
 * A {@code Configuration} object is <b>eager</b> by default, that is, the resource will
 * be loaded directly during {@code build()} time. Optionally, a {@code Configuration} may
 * be created with the <b>lazy</b> flag, indicating that the resource shall not be loaded
 * until really needed.
 * <p>
 * A {@code Configuration} may also be marked as <b>optional</b>, indicating that the
 * configuration shall be loaded quietly, that is, an "empty" {@code Configuration} object
 * will be instantiated even if the resource cannot be loaded (not default behavior).
 * <p>
 * <strong>IMPORTANT:</strong> Use a {@link ConfigurationBuilder} to create a
 * {@code Configuration} object. A builder instance can be retrieved by calling
 * {@link Configuration#builder()}. For example:
 *
 * <blockquote>
 *
 * <pre>
 * {@code Configuration<Properties> config = Configuration.<Properties>builder()}
 * {@code         .source(new ClasspathFileSource<>("my.properties"))}
 * {@code         .mapper(new PropertiesMapper())}
 * {@code         .namespace("default")}
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
    private final boolean lazy;

    private ConfigurationDataRetriever<T> service;

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
        this.lazy = builder.isLazy();

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
    public T getBean()
    {
        return getService().getBean();
    }

    @Override
    public Boolean getBoolean(String key)
    {
        return getService().getBoolean(key);
    }

    @Override
    public Integer getInteger(String key)
    {
        return getService().getInteger(key);
    }

    @Override
    public Long getLong(String key)
    {
        return getService().getLong(key);
    }

    @Override
    public Double getDouble(String key)
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
    public Boolean getMandatoryBoolean(String key)
    {
        return getService().getMandatoryBoolean(key);
    }

    @Override
    public Integer getMandatoryInteger(String key)
    {
        return getService().getMandatoryInteger(key);
    }

    @Override
    public Long getMandatoryLong(String key)
    {
        return getService().getMandatoryLong(key);
    }

    @Override
    public Double getMandatoryDouble(String key)
    {
        return getService().getMandatoryDouble(key);
    }

    @Override
    public String getMandatoryString(String key)
    {
        return getService().getMandatoryString(key);
    }

    /**
     * @return the actual implementation (the Service part in the Proxy Design Pattern)
     * @since 0.4.0
     */
    private ConfigurationDataRetriever<T> getService()
    {
        if (service == null)
        {
            service = new ConfigurationService<>(source, mapper, optional);
        }
        return service;
    }

}

/**
 * Actual implementation (the Service part in the Proxy Design Pattern).
 *
 * @param <T> the target configuration type
 *
 * @author oswaldo.bapvic.jr (Oswaldo Junior)
 * @since 0.4.0
 */
final class ConfigurationService<T> implements ConfigurationDataRetriever<T>
{
    private final T bean;
    private final ConfigurationHelper<T> helper;

    ConfigurationService(Source<T> source, Mapper<T> mapper, boolean optional)
    {
        this.bean = source.load(mapper, optional);
        this.helper = getConfigurationHelper(bean, mapper);
    }

    static <T> ConfigurationHelper<T> getConfigurationHelper(T bean, Mapper<T> mapper)
    {
        return bean != null ? mapper.configurationHelper(bean) : new NullConfigurationHelper<>();
    }

    @Override
    public T getBean()
    {
        return bean;
    }

    @Override
    public Boolean getBoolean(String key)
    {
        return helper.getBoolean(key);
    }

    @Override
    public Integer getInteger(String key)
    {
        return helper.getInteger(key);
    }

    @Override
    public Long getLong(String key)
    {
        return helper.getLong(key);
    }

    @Override
    public Double getDouble(String key)
    {
        return helper.getDouble(key);
    }

    @Override
    public String getString(String key)
    {
        return helper.getString(key);
    }

    @Override
    public Boolean getMandatoryBoolean(String key)
    {
        return helper.getMandatoryBoolean(key);
    }

    @Override
    public Integer getMandatoryInteger(String key)
    {
        return helper.getMandatoryInteger(key);
    }

    @Override
    public Long getMandatoryLong(String key)
    {
        return helper.getMandatoryLong(key);
    }

    @Override
    public Double getMandatoryDouble(String key)
    {
        return helper.getMandatoryDouble(key);
    }

    @Override
    public String getMandatoryString(String key)
    {
        return helper.getMandatoryString(key);
    }

}
