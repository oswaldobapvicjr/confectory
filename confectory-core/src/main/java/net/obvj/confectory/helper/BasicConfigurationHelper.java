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

/**
 * A basic, abstract Configuration Helper object providing common infrastructure for
 * concrete implementations.
 *
 * @param <T> the source type which configuration data is to be retrieved
 *
 * @author oswaldo.bapvic.jr (Oswaldo Junior)
 * @since 0.1.0
 */
public abstract class BasicConfigurationHelper<T> extends AbstractBasicConfigurationHelper<T>
{
    protected final T bean;

    protected BasicConfigurationHelper(T bean)
    {
        this.bean = bean;
    }

    @Override
    public Optional<T> getBean()
    {
        return Optional.ofNullable(bean);
    }

    @Override
    public boolean getBoolean(String key)
    {
        String value = getString(key);
        return nullValueProvider.isNull(value) ? nullValueProvider.getBooleanValue() : Boolean.parseBoolean(value);
    }

    @Override
    public int getInt(String key)
    {
        String value = getString(key);
        return nullValueProvider.isNull(value) ? nullValueProvider.getIntValue() : Integer.parseInt(value);
    }

    @Override
    public long getLong(String key)
    {
        String value = getString(key);
        return nullValueProvider.isNull(value) ? nullValueProvider.getLongValue() : Long.parseLong(value);
    }

    @Override
    public double getDouble(String key)
    {
        String value = getString(key);
        return nullValueProvider.isNull(value) ? nullValueProvider.getDoubleValue() : Double.parseDouble(value);
    }

}
