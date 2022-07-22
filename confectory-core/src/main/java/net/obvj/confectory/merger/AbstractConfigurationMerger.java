/*
 * Copyright 2022 obvj.net
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

package net.obvj.confectory.merger;

import java.util.function.Supplier;

import net.obvj.confectory.Configuration;
import net.obvj.confectory.mapper.DummyMapper;
import net.obvj.confectory.source.DummySource;

/**
 * An abstract {@link ConfigurationMerger} that contains common infrastructure logic for
 * the merging of two {@link Configuration} objects, delegating the actual combination to
 * a concrete implementation.
 *
 * @param <T> the source and target {@code Configuration} type
 *
 * @author oswaldo.bapvic.jr (Oswaldo Junior)
 * @since 2.1.0
 */
public abstract class AbstractConfigurationMerger<T> implements ConfigurationMerger<T>
{
    /**
     * @throws NullPointerException if a null {@code Configuration} is received
     */
    @Override
    public final Configuration<T> merge(Configuration<T> config1, Configuration<T> config2,
            MergeOption... mergeOptions)
    {
        checkParameters(config1, config2);
        T mergedObject = doMerge(config1, config2, mergeOptions);
        Configuration<T> higherPrecedenceConfig = getHighestPrecedenceConfig(config1, config2);
        return newConfiguration(mergedObject, higherPrecedenceConfig);
    }

    /**
     * Checks the parameters against nullity.
     *
     * @param config1 the first configuration to check
     * @param config2 the second configuration to check
     *
     * @throws NullPointerException if any configuration is null
     */
    private void checkParameters(Configuration<T> config1, Configuration<T> config2)
    {
        if (config1 == null || config2 == null)
        {
            throw new NullPointerException("The configuration to merge must not be null");
        }
    }

    /**
     * Combines two {@code Configuration} objects, with focus on the actual documents inside
     * each {@code Configuration}.
     *
     * @param config1      the first {@code Configuration}
     * @param config2      the second {@code Configuration}
     * @param mergeOptions an array of options on how to merge the objects (optional)
     * @return a new bean resulting from the combination of the actual beans inside
     *         {@code config1} and {@code config2}
     */
    abstract T doMerge(Configuration<T> config1, Configuration<T> config2,
            MergeOption... mergeOptions);

    /**
     * Returns the highest-precedence object from the input parameters.
     *
     * @param config1 the first {@code Configuration}
     * @param config2 the second {@code Configuration}
     * @return the object with the highest precedence
     */
    private Configuration<T> getHighestPrecedenceConfig(Configuration<T> config1, Configuration<T> config2)
    {
        return config1.getPrecedence() > config2.getPrecedence() ? config1 : config2;
    }

    /**
     * Creates a new {@link Configuration} with a specified bean and a given baseline
     * {@code Configuration}.
     *
     * @param bean     the bean to set
     * @param baseline the baseline {@link Configuration}
     * @return a new {@link Configuration}
     */
    private Configuration<T> newConfiguration(T bean, Configuration<T> baseline)
    {
        return Configuration.<T>builder()
                .namespace(baseline.getNamespace())
                .precedence(baseline.getPrecedence())
                .source(new DummySource<>())
                .mapper(new DummyMapper<>(baseline.getMapper()::configurationHelper))
                .bean(bean)
                .eager()
                .build();
    }

    /**
     * Returns the bean object associated with the specified {@link Configuration}, or a
     * default object specified by a supplying function if the bean is null.
     *
     * @param config          the {@link Configuration} which bean is to be retrieved
     * @param defaultSupplier a supplying function to be applied if the bean is null
     * @return the actual bean', or the object returned by the specified supplier; not null
     */
    T getBeanSafely(Configuration<T> config, Supplier<T> defaultSupplier)
    {
        T bean = config.getBean();
        return bean == null ? defaultSupplier.get() : bean;
    }

}
