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

package net.obvj.confectory.helper;

import java.util.Optional;

import net.obvj.confectory.ConfigurationException;

/**
 * A "no-op" Configuration Helper object for situations where an optional
 * {@code Configuration} object is not available.
 *
 * @param <T> the source type which configuration data is to be retrieved
 *
 * @author oswaldo.bapvic.jr (Oswaldo Junior)
 * @since 0.1.0
 */
public class NullConfigurationHelper<T> implements ConfigurationHelper<T>
{

    /**
     * @return {@link Optional#empty()}, always
     */
    @Override
    public Optional<T> getBean()
    {
        return Optional.empty();
    }

    /**
     * @return {@code null}, always <b>(not to be interpreted as {@code false})</b>
     */
    @Override
    public Boolean getBoolean(String key)
    {
        return null;
    }

    /**
     * @return {@code null}, always
     */
    @Override
    public Integer getInteger(String key)
    {
        return null;
    }

    /**
     * @return {@code null}, always
     */
    @Override
    public Long getLong(String key)
    {
        return null;
    }

    /**
     * @return {@code null}, always
     */
    @Override
    public Double getDouble(String key)
    {
        return null;
    }

    /**
     * @return {@code null}, always
     */
    @Override
    public String getString(String key)
    {
        return null;
    }

    /**
     * @throws ConfigurationException always
     */
    @Override
    public Boolean getMandatoryBoolean(String key)
    {
        throw newConfigurationException();
    }

    /**
     * @throws ConfigurationException always
     */
    @Override
    public Integer getMandatoryInteger(String key)
    {
        throw newConfigurationException();
    }

    /**
     * @throws ConfigurationException always
     */
    @Override
    public Long getMandatoryLong(String key)
    {
        throw newConfigurationException();
    }

    /**
     * @throws ConfigurationException always
     */
    @Override
    public Double getMandatoryDouble(String key)
    {
        throw newConfigurationException();
    }

    /**
     * @throws ConfigurationException always
     */
    @Override
    public String getMandatoryString(String key)
    {
        throw newConfigurationException();
    }

    private ConfigurationException newConfigurationException()
    {
        return new ConfigurationException("Not found");
    }

}
