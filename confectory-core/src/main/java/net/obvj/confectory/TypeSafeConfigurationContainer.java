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

/**
 * An object that holds multiple {@code Configuration} objects of the same generic type
 * {@code <T>} and selects the highest-precedence available ones. This is particularly
 * useful for source prioritization or handling multiple versions of the same
 * configuration object.
 * <p>
 * To create a new empty container, use the default constructor
 * {@code new TypeSafeConfigurationContainer<T>()}. To create a new container with preset
 * {@code Configuration} objects, pass them as "var-args", provided that all objects have
 * the same mapped bean type {@code <T>}
 * <p>
 * Use the {@code add(Configuration<T>)} method at any time to register new objects inside
 * the container.
 * <p>
 * To retrieve the highest-precedence {@code Configuration} bean, use the
 * {@code getBean()} methods.
 *
 * @author oswaldo.bapvic.jr (Oswaldo Junior)
 * @since 1.0.0
 *
 * @see Configuration
 * @see ConfigurationContainer
 * @see DataFetchStrategy
 */
public final class TypeSafeConfigurationContainer<T>
{
    private ConfigurationContainer container;

    /**
     * Builds a new {@code TypeSageConfigurationContainer} with an arbitrary number of preset
     * {@code Configuration} objects to be registered.
     *
     * @param configs an arbitrary number of {@code Configuration} objects (zero or more) to
     *                be registered at constructor time
     */
    @SafeVarargs
    public TypeSafeConfigurationContainer(Configuration<T>... configs)
    {
        container = new ConfigurationContainer(DataFetchStrategy.STRICT, configs);
    }

    /**
     * Adds the specified {@code Configuration} to this container.
     *
     * @param configuration the {@link Configuration} to be added to the container
     */
    public void add(Configuration<T> configuration)
    {
        container.add(configuration);
    }

    /**
     * Removes all of the {@code Configuration} objects from this container.
     */
    public void clear()
    {
        container.clear();
    }

    /**
     * Returns the highest-precedence mapped bean inside this container.
     *
     * @return the highest-precedence mapped bean inside this container, or {@code null} if no
     *         bean available
     */
    public T getBean()
    {
        return getBean(ConfigurationContainer.DEFAULT_NAMESPACE);
    }

    /**
     * Returns the highest-precedence mapped bean associated with the specified namespace
     * inside this container.
     *
     * @param namespace the namespace to query
     * @return the highest-precedence mapped bean associated with the specified namespace, or
     *         {@code null} if no bean available in the namespace
     */
    @SuppressWarnings("unchecked")
    public T getBean(String namespace)
    {
        // Type safety is already guaranteed by the constructor
        return (T) container.getValue(namespace, Configuration::getBean);
    }

    /**
     * Returns the number of {@code Configuration} objects in this container.
     *
     * @return the number of {@code Configuration} objects in this container
     */
    public long size()
    {
        return container.size();
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
        return container.size(namespace);
    }

    /**
     * Returns {@code true} if this container contains no {@code Configuration} objects.
     *
     * @return {@code true} if this container contains no {@code Configuration} objects
     */
    public boolean isEmpty()
    {
        return container.isEmpty();
    }

    /**
     * @return the internal {@link ConfigurationContainer}, mainly for testing or
     *         troubleshooting purposes
     */
    protected ConfigurationContainer getInternal()
    {
        return container;
    }

}
