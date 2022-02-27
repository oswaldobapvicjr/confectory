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

package net.obvj.confectory.internal.helper;

import net.obvj.confectory.ConfigurationException;

/**
 * A Configuration Helper implementation for user-defined beans.
 *
 * @param <T> the configuration bean type
 *
 * @author oswaldo.bapvic.jr (Oswaldo Junior)
 * @since 0.1.0
 */
public class BeanConfigurationHelper<T> extends BasicConfigurationHelper<T>
{
    /**
     * Builds a new Configuration Helper instance with a specific source.
     *
     * @param source the source bean, mainly for exception/reporting purposes
     */
    public BeanConfigurationHelper(T source)
    {
        super(source);
    }

    /**
     * @param key not used since this method implementation is a "no-op"
     * @throws ConfigurationException always, since the data for this type of helper should be
     *                                retrieved by the user-defined bean
     */
    @Override
    public Object get(String key)
    {
        throw newConfigurationException();
    }

    /**
     * @param key not used since this method implementation is a "no-op"
     * @throws ConfigurationException always, since the data for this type of helper should be
     *                                retrieved by the user-defined bean
     */
    @Override
    public String getString(String key)
    {
        throw newConfigurationException();
    }

    /**
     * @param key not used since this method implementation is a "no-op"
     * @throws ConfigurationException always, since the data for this type of helper should be
     *                                retrieved by the user-defined bean
     */
    @Override
    public String getMandatoryString(String key)
    {
        throw newConfigurationException();
    }

    private ConfigurationException newConfigurationException()
    {
        return new ConfigurationException("Operation not supported for bean of type '%s'",
                super.bean.getClass().getName());
    }

}
