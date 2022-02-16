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
import java.util.Optional;

import org.apache.commons.lang3.reflect.ConstructorUtils;
import org.apache.commons.lang3.reflect.FieldUtils;

import net.obvj.confectory.ConfigurationException;
import net.obvj.confectory.helper.BeanConfigurationHelper;
import net.obvj.confectory.helper.ConfigurationHelper;
import net.obvj.confectory.util.ParseFactory;
import net.obvj.confectory.util.Property;
import net.obvj.confectory.util.ReflectionUtils;

/**
 * A specialized {@code Mapper} that loads the contents of a valid INI {@code Source}
 * (e.g.: file, URL) into a POJO (Plain-Old Java Object).
 * <p>
 * <strong>Note:</strong> This mapper is <b>NOT</b> thread-safe.
 *
 * @author oswaldo.bapvic.jr (Oswaldo Junior)
 * @since 1.3.0
 */
public class INIToObjectMapper<T> extends AbstractINIMapper<T> implements Mapper<T>
{
    private static final String MSG_UNABLE_TO_BUILD_OBJECT = "Unable to build object of type: %s";

    private final Class<T> targetType;

    /**
     * Builds a new {@code INIToObjectMapper} with the specified target type.
     *
     * @param targetType the target type to be produced by this {@code Mapper}
     */
    public INIToObjectMapper(Class<T> targetType)
    {
        this.targetType = targetType;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T apply(InputStream inputStream) throws IOException
    {
        return (T) doApply(inputStream);
    }

    @Override
    Object newObject()
    {
        Class<?> type = getCurrentType();
        try
        {
            return type != null ? ConstructorUtils.invokeConstructor(type) : null;
        }
        catch (ReflectiveOperationException exception)
        {
            throw new ConfigurationException(exception, MSG_UNABLE_TO_BUILD_OBJECT, type);
        }
    }

    @Override
    Object doParseValue(String value)
    {
        Field field = findField(getCurrentType(), currentKey);
        return field != null ? ParseFactory.parse(field.getType(), value) : null;
    }

    @Override
    void put(Object target, String name, Object value)
    {
        Field field = findField(target.getClass(), name);
        if (field != null && !ReflectionUtils.isTransient(field))
        {
            try
            {
                FieldUtils.writeDeclaredField(target, field.getName(), value, true);
            }
            catch (IllegalAccessException exception)
            {
                throw new ConfigurationException(exception, MSG_UNABLE_TO_BUILD_OBJECT, target);
            }
        }
    }

    /**
     * @return the current type being processed by the template, which can be either the final
     *         {@code targetType}, or the current section type; or {@code null} if the current
     *         section does not have a corresponing field in the final {@code targetType}
     */
    private Class<?> getCurrentType()
    {
        if (currentSectionName == null)
        {
            return targetType;
        }
        Field field = findField(targetType, currentSectionName);
        return field != null ? field.getType() : null;
    }

    /**
     * Find a field matching any of the following criteria in the specified type, or
     * {@code null} if no match found:
     * <ul>
     * <li>the field name is equal to the specified {@code name} parameter; or</li>
     * <li>the field is marked with the {@code @}{@link Property} annotation, and the
     * annotation defines a custom name equal to the one specified in the parameter</li>
     * </ul>
     *
     * @param type the class to reflect; not null
     * @param name the field name to obtain; not null
     * @return the {@link Field} object; can be null
     */
    private static Field findField(Class<?> type, String name)
    {
        return Optional.ofNullable(FieldUtils.getField(type, name, true))
                .orElseGet(() -> findFieldByAnnotation(type, name).orElse(null));
    }

    /**
     * Find a field matching the following criteria in the specified type, or {@code null} if
     * no match found:
     * <ul>
     * <li>the field is marked with the {@code @}{@link Property} annotation, and the
     * annotation defines a custom name equal to the one specified in the parameter</li>
     * </ul>
     *
     * @param type the class to reflect; not null
     * @param name the field name to obtain; not null
     * @return an Optional possibly containing a {@link Field} object; can be empty
     */
    private static Optional<Field> findFieldByAnnotation(Class<?> type, String name)
    {
        return FieldUtils.getFieldsListWithAnnotation(type, Property.class).stream()
                .filter(field -> isFieldAnnotated(field, name)).findAny();
    }

    /**
     * Returns {@code true} if the field is annotated with a Property annotation which value
     * matches the specified name.
     *
     * @param field the field to be checked
     * @param name  the name to be tested
     * @return {@code true} if the field is annotated with a Property annotation which value
     *         matches the specified name
     */
    private static boolean isFieldAnnotated(Field field, String name)
    {
        Property property = field.getDeclaredAnnotation(Property.class);
        return property != null && name.equals(property.value());
    }

    @Override
    public ConfigurationHelper<T> configurationHelper(T object)
    {
        return new BeanConfigurationHelper<>(object);
    }

}
