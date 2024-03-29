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

import net.obvj.confectory.internal.helper.ConfigurationHelper;
import net.obvj.confectory.mapper.Mapper;
import net.obvj.confectory.merger.ConfigurationMerger;
import net.obvj.confectory.source.Source;
import net.obvj.jsonmerge.JsonMergeOption;

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
 * {@code Configuration} object. A builder instance can be retrieved by calling the static
 * method {@link Configuration#builder()}. For example:
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
public final class Configuration<T>
        implements ConfigurationDataRetriever<T>, ConfigurationMetadataRetriever<T>
{
    private static final JsonMergeOption[] NO_MERGE_OPTION = new JsonMergeOption[0];

    private final String namespace;
    private final int precedence;
    private final Source<T> source;
    private final Mapper<T> mapper;
    private final boolean optional;
    private final boolean lazy;

    private ConfigurationService<T> service;

    /**
     * Builds a new {@code Configuration} from the specified {@link ConfigurationBuilder}.
     *
     * @param builder the {@link ConfigurationBuilder} to be built
     */
    protected Configuration(ConfigurationBuilder<T> builder)
    {
        namespace = builder.getNamespace();
        precedence = builder.getPrecedence();
        source = builder.getSource();
        mapper = builder.getMapper();
        optional = builder.isOptional();
        lazy = builder.isLazy();

        T bean = builder.getBean();
        if (bean != null)
        {
            service = new ConfigurationService<>(bean, mapper);
        }
        else if (!lazy)
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
    public Object get(String key)
    {
        return getService().get(key);
    }

    @Override
    public String getAsString()
    {
        return getService().getAsString();
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
     * Combines this {@code Configuration} with another one, producing a new
     * {@code Configuration}.
     * <p>
     * The resulting {@code Configuration} will receive all the elements from both objects. In
     * case of conflicting keys, the values at the highest-precedence {@code Configuration}
     * will be selected.
     * <p>
     * The metadata of the highest-precedence {@code Configuration} (namespace and precedence)
     * will be applied to the new {@code Configuration}.
     * <p>
     * <strong>Note: </strong> The other {@link Configuration} must be of the same type as the
     * current one.
     *
     * @param other the {@code Configuration} to be merged with this one; not null
     *
     * @return a new {@code Configuration} resulting from the combination of this object and
     *         the specified one
     * @throws NullPointerException if the other {@code Configuration} is {@code null}
     *
     * @since 2.2.0
     * @see ConfigurationMerger
     */
    public Configuration<T> merge(Configuration<T> other)
    {
        return merge(other, NO_MERGE_OPTION);
    }

    /**
     * Combines this {@code Configuration} with another one, producing a new
     * {@code Configuration}, using advanced options.
     * <p>
     * The resulting {@code Configuration} will receive all the elements from both objects. In
     * case of conflicting keys, the values at the highest-precedence {@code Configuration}
     * will be selected.
     * <p>
     * The metadata of the highest-precedence {@code Configuration} (namespace and precedence)
     * will be applied to the new {@code Configuration}.
     * <p>
     * <strong>Note: </strong> The other {@link Configuration} must be of the same type as the
     * current one.
     *
     * @param other        the {@code Configuration} to be merged with this one; not null
     * @param mergeOptions an array of options on how to merge the objects (optional)
     *
     * @return a new {@code Configuration} resulting from the combination of this object and
     *         the specified one
     * @throws NullPointerException if the other {@code Configuration} is {@code null}
     *
     * @since 2.2.0
     * @see ConfigurationMerger
     */
    public Configuration<T> merge(Configuration<T> other, JsonMergeOption... mergeOptions)
    {
        return getService().getHelper().configurationMerger().merge(this, other, mergeOptions);
    }

    /**
     * @return the actual configuration
     * @since 0.4.0
     */
    private ConfigurationService<T> getService()
    {
        if (service == null)
        {
            service = new ConfigurationService<>(source, mapper, optional);
        }
        return service;
    }

}

/**
 * Actual implementation (the Service part in the Proxy Design Pattern), which allows the
 * lazy loading of {@code Configuration} data.
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

    /**
     * Creates a {@code ConfigurationService} from a {@link Source} and {@link Mapper}.
     *
     * @param source   the {@link Source} to be loaded
     * @param mapper   the {@link Mapper} to be applied
     * @param optional a flag indicating whether or not an exception should be thrown in an
     *                 event of failure to load the configuration source
     */
    ConfigurationService(Source<T> source, Mapper<T> mapper, boolean optional)
    {
        this(source.load(mapper, optional), mapper);
    }

    /**
     * Creates a {@code ConfigurationService} with a preset bean.
     *
     * @param bean   the bean to set
     * @param mapper the {@link Mapper} which {@link ConfigurationHelper} is to be retrieved
     * @since 2.1.0
     */
    ConfigurationService(T bean, Mapper<T> mapper)
    {
        this.bean = bean;
        this.helper = ConfigurationHelper.newInstance(bean, mapper);
    }

    /**
     * @return the {@link ConfigurationHelper} assigned to this service
     * @since 2.2.0
     */
    ConfigurationHelper<T> getHelper()
    {
        return helper;
    }

    @Override
    public T getBean()
    {
        return bean;
    }

    @Override
    public String getAsString()
    {
        return helper.getAsString();
    }

    @Override
    public Object get(String key)
    {
        return helper.get(key);
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
