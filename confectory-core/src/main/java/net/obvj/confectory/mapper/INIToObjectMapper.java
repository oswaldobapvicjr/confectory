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
import net.obvj.confectory.internal.helper.BeanConfigurationHelper;
import net.obvj.confectory.internal.helper.ConfigurationHelper;
import net.obvj.confectory.util.ParseException;
import net.obvj.confectory.util.ParseFactory;
import net.obvj.confectory.util.Property;
import net.obvj.confectory.util.ReflectionUtils;

/**
 * A specialized {@code Mapper} that loads the contents of a valid INI {@code Source}
 * (e.g.: file, URL) into a POJO (Plain-Old Java Object).
 * <p>
 * Every property and section will be assigned to a field in the target object in either
 * of the following cases:
 * <ul>
 * <li>the field is marked with the {@code @}{@link Property} annotation, defining a
 * custom key to be mapped; or</li>
 * <li>the field name is equal to the property key (with no need to use an annotation in
 * these cases)</li>
 * </ul>
 * <p>
 * <strong>Notes:</strong>
 * <ul>
 * <li>The fields in the target object can be {@code private} (recommended)</li>
 * <li>Fields marked {@code transient} are ignored</li>
 * <li>If the associated property is missing in the source, the corresponding field will
 * assume a default value, i.e.: {@code null} for object types, zero for numeric types,
 * and {@code false} for booleans.</li>
 * <li>If the a section is not mapped to dedicated object in the target class, the whole
 * section will be skipped</li>
 * </ul>
 *
 * @param <T> the target type to be produced by this {@code Mapper}
 *
 * @author oswaldo.bapvic.jr (Oswaldo Junior)
 * @since 2.0.0
 */
public class INIToObjectMapper<T> extends AbstractINIMapper<T> implements Mapper<T>
{
    private static final String MSG_UNABLE_TO_BUILD_OBJECT = "Unable to build object of type: %s";
    private static final String MSG_UNPARSABLE_PROPERTY_VALUE = "Unable to parse the value of the property %s into a field of type '%s'";

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
    Object newObject(Context context)
    {
        Class<?> type = getCurrentType(context);
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
    Object parseValue(Context context, String value)
    {
        Field field = findField(getCurrentType(context), context.currentKey);
        try
        {
            return field != null ? ParseFactory.parse(field.getType(), value) : null;
        }
        catch (ParseException exception)
        {
            throw new ConfigurationException(exception, MSG_UNPARSABLE_PROPERTY_VALUE,
                    currentFieldIdentifierToString(context), field.getType().getCanonicalName());
        }
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
     *         section does not have a corresponding field in the final {@code targetType}
     */
    private Class<?> getCurrentType(Context context)
    {
        if (context.currentSectionName == null)
        {
            return targetType;
        }
        Field field = findField(targetType, context.currentSectionName);
        return field != null ? field.getType() : null;
    }

    /**
     * Find a field matching any of the following criteria in the specified type, or
     * {@code null} if no match found:
     * <ul>
     * <li>the field is marked with the {@code @}{@link Property} annotation, and the
     * annotation defines a custom name equal to the one specified in the parameter; or</li>
     * <li>the field name is equal to the specified {@code name} parameter</li>
     * </ul>
     *
     * @param type the class to reflect; not null
     * @param name the field name to obtain; not null
     * @return the {@link Field} object; can be null
     */
    private static Field findField(Class<?> type, String name)
    {
        return findFieldByAnnotation(type, name).orElseGet(() -> FieldUtils.getField(type, name, true));
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
     * @param field the field to be checked; not null
     * @param name  the name to be tested; not null
     * @return {@code true} if the field is annotated with a Property annotation which value
     *         matches the specified name
     */
    private static boolean isFieldAnnotated(Field field, String name)
    {
        Property property = field.getDeclaredAnnotation(Property.class);
        return property != null && name.equals(property.value());
    }

    /**
     * @return a string representing the current field for exception/troubleshooting purposes
     */
    private String currentFieldIdentifierToString(Context context)
    {
        StringBuilder builder = new StringBuilder();
        if (context.currentSectionName != null)
        {
            builder.append("['").append(context.currentSectionName).append("']");
        }
        builder.append("['").append(context.currentKey).append("']");
        return builder.toString();
    }

    @Override
    public ConfigurationHelper<T> configurationHelper(T object)
    {
        return new BeanConfigurationHelper<>(object);
    }

}
