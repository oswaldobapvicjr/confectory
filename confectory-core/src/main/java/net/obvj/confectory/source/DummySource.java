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

package net.obvj.confectory.source;

import java.util.UUID;

import net.obvj.confectory.ConfigurationSourceException;
import net.obvj.confectory.mapper.Mapper;

/**
 * A dummy {@code Source} for internal purposes.
 *
 * @author oswaldo.bapvic.jr (Oswaldo Junior)
 * @since 2.1.0
 */
public class DummySource<T> extends AbstractSource<T> implements Source<T>
{
    /**
     * Builds a dummy source.
     */
    public DummySource()
    {
        super(UUID.randomUUID().toString());
    }

    @Override
    public T load(Mapper<T> mapper)
    {
        throw new ConfigurationSourceException("Dummy source");
    }

}
