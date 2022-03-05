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

package net.obvj.confectory.mapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.function.Function;

import net.obvj.confectory.ConfigurationException;
import net.obvj.confectory.internal.helper.ConfigurationHelper;

/**
 * A dummy {@code Mapper} for use with a preset configuration bean and other internal
 * purposes.
 *
 * @param <T> the preset bean type
 *
 * @author oswaldo.bapvic.jr (Oswaldo Junior)
 * @since 2.1.0
 */
public class DummyMapper<T> implements Mapper<T>
{
    private final Function<T, ConfigurationHelper<T>> configurationHelperCreator;

    /**
     * Creates a dummy {@code Mapper} with a {@link ConfigurationHelper} factory function.
     *
     * @param configurationHelperCreator the {@link ConfigurationHelper} factory function to
     *                                   be applied by this {@code Mapper}; not null
     *
     * @throws NullPointerException if a null helper factory function is provided
     */
    public DummyMapper(Function<T, ConfigurationHelper<T>> configurationHelperCreator)
    {
        this.configurationHelperCreator = configurationHelperCreator;
    }

    @Override
    public T apply(InputStream input) throws IOException
    {
        throw new ConfigurationException("Dummy mapper");
    }

    @Override
    public ConfigurationHelper<T> configurationHelper(T bean)
    {
        return configurationHelperCreator.apply(bean);
    }

}
