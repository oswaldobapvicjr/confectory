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
 * A "no-op" Configuration Helper object for situations where an optional
 * {@code Configuration} object is not available.
 *
 * @param <T> the source type which configuration data is to be retrieved
 *
 * @author oswaldo.bapvic.jr (Oswaldo Junior)
 * @since 0.1.0
 */
public class NullConfigurationHelper<T> extends AbstractBasicConfigurationHelper<T>
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
     * @return the smart-null value for {@code boolean}, always
     */
    @Override
    public boolean getBoolean(String key)
    {
        return nullValueProvider.getBooleanValue();
    }

    /**
     * @return the smart-null value for {@code int}, always
     */
    @Override
    public int getInt(String key)
    {
        return nullValueProvider.getIntValue();
    }

    /**
     * @return the smart-null value for {@code long}, always
     */
    @Override
    public long getLong(String key)
    {
        return nullValueProvider.getLongValue();
    }

    /**
     * @return the smart-null value for {@code double}, always
     */
    @Override
    public double getDouble(String key)
    {
        return nullValueProvider.getDoubleValue();
    }

    /**
     * @return the smart-null value for {@code String}, always
     */
    @Override
    public String getString(String key)
    {
        return nullValueProvider.getStringValue();
    }

}
