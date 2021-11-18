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

import java.util.Optional;

import net.obvj.confectory.helper.ConfigurationHelper;
import net.obvj.confectory.helper.NullConfigurationHelper;
import net.obvj.confectory.helper.nullvalue.NullValueProvider;
import net.obvj.confectory.mapper.Mapper;
import net.obvj.confectory.source.Source;

/**
 * @param <T> the target configuration type
 *
 * @see Source
 * @see Mapper
 * @see ConfigurationBuilder
 *
 * @author oswaldo.bapvic.jr (Oswaldo Junior)
 * @since 0.4.0
 */
public final class ConfigurationService<T> implements ConfigurationDataRetriever<T>
{
    private final Optional<T> bean;
    private final ConfigurationHelper<T> helper;

    /**
     * Builds a new {@code Configuration} from the specified {@link ConfigurationBuilder}.
     *
     * @param builder the {@link ConfigurationBuilder} to be built
     */
    protected ConfigurationService(ConfigurationBuilder<T> builder)
    {
        Source<T> source = builder.getSource();
        Mapper<T> mapper = builder.getMapper();
        boolean optional = builder.isOptional();
        NullValueProvider nullValueProvider = builder.getNullValueProvider();

        this.bean = source.load(mapper, optional);
        this.helper = prepareConfigurationHelper(mapper, nullValueProvider);
    }

    private ConfigurationHelper<T> prepareConfigurationHelper(Mapper<T> mapper, NullValueProvider nullValueProvider)
    {
        ConfigurationHelper<T> configurationHelper = bean.isPresent() ? mapper.configurationHelper(bean.get())
                : new NullConfigurationHelper<>();

        if (nullValueProvider != null)
        {
            configurationHelper.setNullValueProvider(nullValueProvider);
        }
        return configurationHelper;
    }

    @Override
    public Optional<T> getBean()
    {
        return bean;
    }

    @Override
    public boolean getBoolean(String key)
    {
        return helper.getBoolean(key);
    }

    @Override
    public int getInt(String key)
    {
        return helper.getInt(key);
    }

    @Override
    public long getLong(String key)
    {
        return helper.getLong(key);
    }

    @Override
    public double getDouble(String key)
    {
        return helper.getDouble(key);
    }

    @Override
    public String getString(String key)
    {
        return helper.getString(key);
    }

    @Override
    public boolean getMandatoryBoolean(String key)
    {
        return helper.getMandatoryBoolean(key);
    }

    @Override
    public int getMandatoryInt(String key)
    {
        return helper.getMandatoryInt(key);
    }

    @Override
    public long getMandatoryLong(String key)
    {
        return helper.getMandatoryLong(key);
    }

    @Override
    public double getMandatoryDouble(String key)
    {
        return helper.getMandatoryDouble(key);
    }

    @Override
    public String getMandatoryString(String key)
    {
        return helper.getMandatoryString(key);
    }

}
