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

import static org.apache.commons.lang3.StringUtils.*;

import net.obvj.confectory.mapper.DynamicMapper;
import net.obvj.confectory.mapper.Mapper;
import net.obvj.confectory.source.DynamicSource;
import net.obvj.confectory.source.Source;
import net.obvj.confectory.source.SourceFactory;

/**
 * A mutable object that supports the creation of immutable {@link Configuration} objects.
 * <p>
 * For example:
 *
 * <blockquote>
 * <b><i>Example 1: Combining a source and a mapper (type-safe):</i></b>
 * <pre>
 * {@code Configuration<Properties> config = new ConfigurationBuilder<Properties>()}
 * {@code         .source(new ClasspathFileSource<>("my.properties"))}
 * {@code         .mapper(new PropertiesMapper())}
 * {@code         .namespace("default")}
 * {@code         .precedence(10)}
 * {@code         .lazy()}
 * {@code         .build();}
 * </pre>
 *
 * </blockquote>
 * <blockquote>
 * <b><i>Example 2: Building a generic configuration</i></b>
 * (the actual mapper will be inferred based on the source file extension):
 * <pre>
 * {@code Configuration<?> config = Configuration.builder()}
 * {@code         .source("my.properties")}
 * {@code         .lazy()}
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
    private String path;
    private Source<T> source;
    private Mapper<T> mapper;
    private boolean optional;
    private boolean lazy;
    private T bean;

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
     * @param sourceConfiguration a preset {@code Configuration} object whose attributes are
     *                            to be copied; {@code null} is allowed
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
            lazy = sourceConfiguration.isLazy();
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
     * <p>
     * <strong>Note:</strong> This precedence value is optional and the default value is
     * {@code 0} (zero).
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
     * Defines a {@link DynamicSource} with the specified path for the new
     * {@code Configuration}.
     *
     * @param path the path to be set; <strong>not</strong> null
     * @return a reference to this same {@code ConfigurationBuilder} for chained calls
     */
    public ConfigurationBuilder<T> source(String path)
    {
        this.path = path;
        this.source = SourceFactory.dynamicSource(path);
        return this;
    }

    /**
     * Defines the {@link Mapper} of the new {@code Configuration}.
     * <p>
     * <b>Note:</b> Since 2.6.0, if not specified, a default mapper will be defined
     * dynamically, based on the source file extension, if present.
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
     * @see #required()
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
     * @see #optional()
     */
    public ConfigurationBuilder<T> required()
    {
        this.optional = false;
        return this;
    }

    /**
     * Marks the new {@code Configuration} as lazy.
     * <p>
     * The configuration source will not be loaded until needed.
     *
     * @return a reference to this same {@code ConfigurationBuilder} for chained calls
     * @see #eager()
     *
     * @since 0.4.0
     */
    public ConfigurationBuilder<T> lazy()
    {
        this.lazy = true;
        return this;
    }

    /**
     * Marks the new {@code Configuration} as eager.
     * <p>
     * The configuration source will loaded directly during {@code build()} time (default).
     *
     * @return a reference to this same {@code ConfigurationBuilder} for chained calls
     * @see #lazy()
     *
     * @since 0.4.0
     */
    public ConfigurationBuilder<T> eager()
    {
        this.lazy = false;
        return this;
    }

    /**
     * <strong>[Optional]</strong> Defines a preset bean for the new {@code Configuration}.
     *
     * @param bean the preset bean to be set
     * @return a reference to this same {@code ConfigurationBuilder} for chained calls
     * @since 2.1.0
     */
    public ConfigurationBuilder<T> bean(T bean)
    {
        this.bean = bean;
        return this;
    }

    /**
     * Builds the target {@code Configuration}.
     *
     * @return a new {@link Configuration} object
     * @throws IllegalStateException        if the {@code Source} parameter is missing; or the
     *                                      {@code Mapper} is missing; or the {@code Mapper}
     *                                      could not be inferred
     * @throws ConfigurationSourceException in the event of a failure loading the
     *                                      configuration source
     */
    public Configuration<T> build()
    {
        requireNonNullForBuild(source, "The configuration source must not be null");
        evaluateMapper();

        namespace = defaultString(namespace);

        return new Configuration<>(this);
    }

    /**
     * Secure the existence of a {@link Mapper}. Try to infer one if not specified.
     *
     * @throws IllegalStateException if the {@link Mapper} is null and could not be inferred
     * @since 2.6.0
     */
    private void evaluateMapper()
    {
        if (mapper == null)
        {
            // Try to infer based on the file extension if present
            String extension = substringAfterLast(path, ".");
            if (isEmpty(extension))
            {
                // If not able to infer by extension, let it be inferred during read time.
                mapper = (Mapper<T>) new DynamicMapper();
            }
            else
            {
                mapper = (Mapper<T>) new DynamicMapper(extension);
            }
        }
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

    public T getBean()
    {
        return bean;
    }

    private static <T> T requireNonNullForBuild(T object, String message)
    {
        if (object == null)
        {
            throw new IllegalStateException(message);
        }
        return object;
    }

}
