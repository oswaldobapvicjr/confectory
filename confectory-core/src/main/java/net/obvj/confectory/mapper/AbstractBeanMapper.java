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

package net.obvj.confectory.mapper;

import java.io.InputStream;

import net.obvj.confectory.helper.ConfigurationHelper;
import net.obvj.confectory.helper.BeanConfigurationHelper;

/**
 * An abstract {@code Mapper} for implementations intended to load the contents of an
 * {@link InputStream} into user-defined beans.
 *
 * @author oswaldo.bapvic.jr (Oswaldo Junior)
 * @since 0.1.0
 */
public abstract class AbstractBeanMapper<T> implements Mapper<T>
{

    /**
     * @return a {@link BeanConfigurationHelper}, since the configuration for this type of
     *         {@code Mapper} is intended to be retrieved by the user-defined bean.
     */
    @Override
    public ConfigurationHelper<T> configurationHelper(T bean)
    {
        return new BeanConfigurationHelper<>(bean);
    }

}
