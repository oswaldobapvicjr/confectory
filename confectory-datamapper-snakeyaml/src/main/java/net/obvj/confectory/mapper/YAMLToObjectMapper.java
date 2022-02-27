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

import org.yaml.snakeyaml.Yaml;

import net.obvj.confectory.internal.helper.BeanConfigurationHelper;
import net.obvj.confectory.internal.helper.ConfigurationHelper;

/**
 * A specialized {@code Mapper} that loads the contents of a valid YAML {@code Source}
 * (e.g.: file, URL, string) into POJO (Plain Old Java Object), using SnakeYAML.
 *
 * @param <T> the target type to be produced by this {@code Mapper}
 *
 * @author oswaldo.bapvic.jr (Oswaldo Junior)
 * @since 1.1.0
 */
public class YAMLToObjectMapper<T> implements Mapper<T>
{
    private final Class<T> targetType;

    /**
     * Builds a new YAML mapper with the specified target type.
     *
     * @param targetType the target type to be produced by this {@code Mapper}
     */
    public YAMLToObjectMapper(Class<T> targetType)
    {
        this.targetType = targetType;
    }

    @Override
    public T apply(InputStream input) throws IOException
    {
        Yaml yaml = new Yaml();
        return yaml.loadAs(input, targetType);
    }

    @Override
    public ConfigurationHelper<T> configurationHelper(T jsonObject)
    {
        return new BeanConfigurationHelper<>(jsonObject);
    }

}
