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
import java.util.Objects;
import java.util.Properties;

import org.apache.commons.lang3.reflect.FieldUtils;

import net.obvj.confectory.ConfigurationException;
import net.obvj.confectory.internal.helper.BeanConfigurationHelper;
import net.obvj.confectory.internal.helper.ConfigurationHelper;
import net.obvj.confectory.settings.ConfectorySettings;
import net.obvj.confectory.util.*;

/**
 * A specialized {@code Mapper} that loads the contents of a {@code Source} (e.g.: file,
 * URL) in the {@code Properties} format (a sequence of key-value pairs) and converts it
 * into a POJO (Plain-Old Java Object).
 * <p>
 * Every property will be assigned to a field in the target object in either of the
 * following cases:
 * <ul>
 * <li>the field name is equal to the property key (with no need to use an annotation in
 * these cases); or</li>
 * <li>the field is marked with the {@code @}{@link Property} annotation, defining a
 * custom key to be mapped</li>
 * </ul>
 * <p>
 * <strong>Notes:</strong>
 * <ul>
 * <li>The fields in the target object can be {@code private} (recommended)</li>
 * <li>Fields marked {@code transient} are ignored</li>
 * <li>If the associated property is missing in the source, the corresponding field will
 * assume a default value, i.e.: {@code null} for object types, zero for numeric types,
 * and {@code false} for booleans.</li>
 * </ul>
 *
 * @param <T> the target type to be produced by this {@code Mapper}
 *
 * @author oswaldo.bapvic.jr (Oswaldo Junior)
 * @since 1.2.0
 *
 * @see Property
 */
public class PropertiesToObjectMapper<T> implements Mapper<T>
{
    private static final String MSG_UNABLE_TO_PARSE_PROPERTY = "Unable to parse the value of the property '%s' into a field of type '%s'";

    private final Class<T> targetType;
    private final ObjectFactory objectFactory;

    /**
     * Builds a new Properties Mapper with the specified target type.
     *
     * @param targetType the target type to be produced by this {@code Mapper}
     */
    public PropertiesToObjectMapper(Class<T> targetType)
    {
        this(targetType, ConfectorySettings.instance().getObjectFactory());
    }

    /**
     * Builds a new Properties Mapper with the specified target type and a custom object
     * factory.
     *
     * @param targetType    the target type to be produced by this {@code Mapper}
     * @param objectFactory the {@link ObjectFactory} to produce objects; not null
     * @since 2.5.0
     */
    public PropertiesToObjectMapper(Class<T> targetType, ObjectFactory objectFactory)
    {
        this.targetType = targetType;
        this.objectFactory = Objects.requireNonNull(objectFactory,
                "the ObjectFactory must not be null");
    }

    @Override
    public T apply(InputStream inputStream) throws IOException
    {
        Properties properties = new PropertiesMapper().apply(inputStream);
        return asObject(properties);

    }

    /**
     * Constructs the target object with the contents of the specified {@link Properties}.
     *
     * @param properties the {@link Properties} to be mapped to the target object
     * @return the target object, filled with properties mapped from the source
     */
    private T asObject(Properties properties)
    {
        Field[] fields = FieldUtils.getAllFields(targetType);
        try
        {
            T targetObject = objectFactory.newObject(targetType);
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

    /**
     * Evaluates (and possibly writes) a property value into the specified {@link Field}.
     *
     * @param targetObject an instantiated object to reflect; not null
     * @param field        the field form the target object to be possibly written; not null
     * @param properties   the properties which field is to be fetched
     * @throws ReflectiveOperationException if the field is not made accessible or the custom
     *                                      converter (if specified in the {@code @Property}
     *                                      annotation) could not be instantiated
     * @throws NullPointerException         if any of the parameters is null
     */
    private void writeField(T targetObject, Field field, Properties properties)
            throws ReflectiveOperationException
    {
        if (ReflectionUtils.isTransient(field))
        {
            return; // Ignore transient fields
        }
        Property annotation = getPropertyAnnotation(field);
        String propertyKey = PropertyUtils.getPropertyKeyOrFieldName(annotation, field);
        String propertyValue = properties.getProperty(propertyKey);
        if (propertyValue != null)
        {
            Class<?> fieldType = field.getType();
            try
            {
                Object parsedValue = PropertyUtils.parseValue(propertyValue, fieldType, annotation, objectFactory);
                FieldUtils.writeDeclaredField(targetObject, field.getName(), parsedValue, true);
            }
            catch (ParseException exception)
            {
                throw new ConfigurationException(exception, MSG_UNABLE_TO_PARSE_PROPERTY,
                        propertyKey, fieldType.getCanonicalName());
            }
        }
        // Do nothing if the property is not found
    }

    private static Property getPropertyAnnotation(Field field)
    {
        return field.getDeclaredAnnotation(Property.class);
    }

    @Override
    public ConfigurationHelper<T> configurationHelper(T type)
    {
        return new BeanConfigurationHelper<>(type);
    }

}
