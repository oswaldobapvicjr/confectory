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
import java.lang.reflect.Field;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.ConstructorUtils;
import org.apache.commons.lang3.reflect.FieldUtils;

import net.obvj.confectory.ConfigurationException;
import net.obvj.confectory.helper.BeanConfigurationHelper;
import net.obvj.confectory.helper.ConfigurationHelper;
import net.obvj.confectory.util.ParseFactory;
import net.obvj.confectory.util.Property;

/**
 * A specialized {@code Mapper} that loads the contents of a {@code Source} (e.g.: file,
 * URL) in the {@code Properties} format (a sequence of key-value pairs) and converts it
 * into a POJO (Plain-Old Java Object).
 *
 * @param <T> the target type to be produced by this {@code Mapper}
 *
 * @author oswaldo.bapvic.jr (Oswaldo Junior)
 * @since 0.1.0
 */
public class PropertiesToObjectMapper<T> implements Mapper<T>
{
    private final Class<T> targetType;

    /**
     * Builds a new Properties Mapper with the specified target type.
     *
     * @param targetType the target type to be produced by this {@code Mapper}
     */
    public PropertiesToObjectMapper(Class<T> targetType)
    {
        this.targetType = targetType;
    }

    @Override
    public T apply(InputStream inputStream) throws IOException
    {
        Properties properties = new PropertiesMapper().apply(inputStream);
        return asObject(properties);

    }

    private T asObject(Properties properties)
    {
        Field[] fields = FieldUtils.getAllFields(targetType);
        try
        {
            T targetObject = ConstructorUtils.invokeConstructor(targetType);
            for (Field field : fields)
            {
                writeField(targetObject, field, properties);
            }
            return targetObject;
        }
        catch (ReflectiveOperationException exception)
        {
            throw new ConfigurationException(exception, "Unable to build object of type: %s", targetType);
        }
    }

    private void writeField(T targetObject, Field field, Properties properties) throws IllegalAccessException
    {
        String propertyKey = getAnnotationPropertyKeyOrFieldName(field);
        String propertyValue = properties.getProperty(propertyKey);
        if (propertyValue != null)
        {
            Class<?> fieldType = field.getType();
            Object parsedValue = ParseFactory.parse(fieldType, propertyValue);
            FieldUtils.writeDeclaredField(targetObject, field.getName(), parsedValue, true);
        }
        // Do nothing if the property is not found
    }

    private String getAnnotationPropertyKeyOrFieldName(Field field)
    {
        Property property = field.getDeclaredAnnotation(Property.class);
        if (property != null && StringUtils.isNotEmpty(property.value()))
        {
            return property.value();
        }
        return field.getName();
    }

    @Override
    public ConfigurationHelper<T> configurationHelper(T type)
    {
        return new BeanConfigurationHelper<>(type);
    }

}
