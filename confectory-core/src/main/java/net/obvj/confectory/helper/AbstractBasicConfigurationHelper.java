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

import java.util.Objects;

import net.obvj.confectory.Confectory;
import net.obvj.confectory.helper.nullvalue.NullValueProvider;

/**
 * An abstract Configuration Helper object providing common infrastructure for concrete
 * implementations.
 *
 * @param <T> the source type which configuration data is to be retrieved
 *
 * @author oswaldo.bapvic.jr (Oswaldo Junior)
 * @since 0.1.0
 */
public abstract class AbstractBasicConfigurationHelper<T> implements ConfigurationHelper<T>
{
    protected NullValueProvider nullValueProvider = Confectory.settings().getDefaultNullValueProvider();

    @Override
    public void setNullValueProvider(NullValueProvider provider)
    {
        this.nullValueProvider = Objects.requireNonNull(provider, "null is not allowed");
    }

    @Override
    public NullValueProvider getNullValueProvider()
    {
        return nullValueProvider;
    }

}
