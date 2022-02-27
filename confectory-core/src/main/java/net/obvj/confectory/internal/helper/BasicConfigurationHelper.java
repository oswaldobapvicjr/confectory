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

/**
 * A basic, abstract Configuration Helper object providing common infrastructure for
 * concrete implementations.
 *
 * @param <T> the source type which configuration data is to be retrieved
 *
 * @author oswaldo.bapvic.jr (Oswaldo Junior)
 * @since 0.1.0
 */
public abstract class BasicConfigurationHelper<T> implements ConfigurationHelper<T>
{
    protected final T bean;

    protected BasicConfigurationHelper(T bean)
    {
        this.bean = bean;
    }

    @Override
    public T getBean()
    {
        return bean;
    }

    @Override
    public Boolean getBoolean(String key)
    {
        String value = getString(key);
        return value == null ? null : Boolean.valueOf(value);
    }

    @Override
    public Boolean getMandatoryBoolean(String key)
    {
        String string = getMandatoryString(key);
        return Boolean.valueOf(string);
    }

    @Override
    public Integer getInteger(String key)
    {
        String value = getString(key);
        return value == null ? null : Integer.valueOf(value);
    }

    @Override
    public Integer getMandatoryInteger(String key)
    {
        String string = getMandatoryString(key);
        return Integer.valueOf(string);
    }

    @Override
    public Long getLong(String key)
    {
        String value = getString(key);
        return value == null ? null : Long.valueOf(value);
    }

    @Override
    public Long getMandatoryLong(String key)
    {
        String string = getMandatoryString(key);
        return Long.valueOf(string);
    }

    @Override
    public Double getDouble(String key)
    {
        String value = getString(key);
        return value == null ? null : Double.valueOf(value);
    }

    @Override
    public Double getMandatoryDouble(String key)
    {
        String string = getMandatoryString(key);
        return Double.valueOf(string);
    }

}
