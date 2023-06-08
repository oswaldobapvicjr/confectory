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

import org.apache.commons.lang3.reflect.ConstructorUtils;
import org.apache.commons.lang3.reflect.FieldUtils;

import net.obvj.confectory.ConfigurationException;
import net.obvj.confectory.internal.helper.BeanConfigurationHelper;
import net.obvj.confectory.internal.helper.ConfigurationHelper;
import net.obvj.confectory.util.*;

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
    private static final String DELIMITER_LEFT = "['";
    private static final String DELIMITER_RIGHT = "']";

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
        Class<?> currentType = getCurrentType(context);
        Field field = PropertyUtils.findFieldByPropertyKeyOrName(currentType, context.currentKey);
        try
        {
            return field != null ? PropertyUtils.parseValue(value, field) : null;
        }
        catch (ParseException | ReflectiveOperationException exception)
        {
            throw new ConfigurationException(exception, MSG_UNPARSABLE_PROPERTY_VALUE,
                    currentFieldIdentifierToString(context), field.getType().getCanonicalName());
        }
    }

    @Override
    void put(Object target, String name, Object value)
    {
        Field field = PropertyUtils.findFieldByPropertyKeyOrName(target.getClass(), name);
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
        Field field = PropertyUtils.findFieldByPropertyKeyOrName(targetType, context.currentSectionName);
        return field != null ? field.getType() : null;
    }

    /**
     * @return a string representing the current field for exception/troubleshooting purposes
     */
    private String currentFieldIdentifierToString(Context context)
    {
        StringBuilder builder = new StringBuilder();
        if (context.currentSectionName != null)
        {
            builder.append(DELIMITER_LEFT).append(context.currentSectionName).append(DELIMITER_RIGHT);
        }
        builder.append(DELIMITER_LEFT).append(context.currentKey).append(DELIMITER_RIGHT);
        return builder.toString();
    }

    @Override
    public ConfigurationHelper<T> configurationHelper(T object)
    {
        return new BeanConfigurationHelper<>(object);
    }

}
