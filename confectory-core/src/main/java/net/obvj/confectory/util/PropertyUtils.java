/*
 * Copyright 2023 obvj.net
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

package net.obvj.confectory.util;

import static net.obvj.confectory.util.StringUtils.*;

import java.lang.reflect.Field;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.ConstructorUtils;
import org.apache.commons.lang3.reflect.FieldUtils;

/**
 * Common methods for working with the {@link Property} annotation.
 *
 * @author oswaldo.bapvic.jr (Oswaldo Junior)
 * @since 2.5.0
 */
public class PropertyUtils
{

    private PropertyUtils()
    {
        throw new UnsupportedOperationException("Instantiation not allowed");
    }

    /**
     * Returns either the property key specified in the {@code @}{@link Property} annotation
     * or, if not present, the field name.
     *
     * @param property the {@link Property} annotation to be evaluated (null is allowed)
     * @param field    the {@link Field} to be evaluated (not null)
     *
     * @return the field name, or the key specified in the {@code @}{@link Property}
     *         annotation, if present in the field.
     *
     * @throws NullPointerException if the field is null
     */
    public static String getPropertyKeyOrFieldName(Property property, Field field)
    {
        return defaultIfEmpty(getPropertyKey(property), field::getName);
    }

    /**
     * Safely returns the property key defined in the {@code @}{@link Property} annotation, or
     * an empty string if the annotation is null.
     *
     * @param property the {@link Property} annotation to be evaluated (null is allowed)
     * @return the property key defined in the annotation, or an empty string (not null)
     */
    public static String getPropertyKey(Property property)
    {
        return property != null
                ? StringUtils.defaultIfEmpty(property.value(), property.key())
                : StringUtils.EMPTY;
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
    public static Field findFieldByPropertyKeyOrName(Class<?> type, String name)
    {
        return findFieldByPropertyKey(type, name)
                .orElseGet(() -> FieldUtils.getField(type, name, true));
    }

    /**
     * Find a field matching the following criteria in the specified type, or {@code null} if
     * no match found:
     * <ul>
     * <li>the field is marked with the {@code @}{@link Property} annotation; and</li>
     * <li>the annotation defines a property key which is equal to the one specified in the
     * parameter</li>
     * </ul>
     *
     * @param type the class to check; not {@code null}
     * @param name the field name to obtain; {@code null}
     * @return an Optional possibly containing a {@link Field} object; can be empty
     */
    public static Optional<Field> findFieldByPropertyKey(Class<?> type, String name)
    {
        return FieldUtils.getFieldsListWithAnnotation(type, Property.class).stream()
                .filter(field -> isFieldAnnotated(field, name)).findAny();
    }

    /**
     * Returns {@code true} if the field is annotated with a Property annotation which key
     * matches the specified name.
     *
     * @param field the field to be checked; not null
     * @param name  the name to be tested
     * @return {@code true} if the field is annotated with a Property annotation which key
     *         matches the specified name
     */
    public static boolean isFieldAnnotated(Field field, String name)
    {
        Property property = field.getDeclaredAnnotation(Property.class);
        return PropertyUtils.getPropertyKey(property).equals(name);
    }

    /**
     * Parse the specified value into an object based on the field type.
     * <p>
     * This method may apply custom conversion logic by invoking the {@link TypeConverter}
     * class configured in the {@link Property} annotation (if not null), or standard
     * conversion by using the {@link TypeFactory} by default.
     *
     * @param string   the string to be parsed
     * @param field    the target field (not null)
     *
     * @return the object resulting from the parse operation
     *
     * @throws ReflectiveOperationException if an error occurs in the reflective operation
     * @throws ParseException               if an error is encountered while parsing
     */
    public static Object parseValue(String string, Field field)
            throws ReflectiveOperationException, ParseException
    {
        return parseValue(string, field.getType(), field.getAnnotation(Property.class));
    }

    /**
     * Parse the specified value into an object of the specified type.
     * <p>
     * This method may apply custom conversion logic by invoking the {@link TypeConverter}
     * class configured in the {@link Property} annotation (if not null), or standard
     * conversion by using the {@link TypeFactory} by default.
     *
     * @param string     the string to be parsed
     * @param targetType the target type (not null)
     * @param property   the {@link Property} annotation to be evaluated (null is allowed)
     *
     * @return the object resulting from the parse operation
     *
     * @throws ReflectiveOperationException if an error occurs in the reflective operation
     * @throws ParseException               if an error is encountered while parsing
     */
    public static Object parseValue(String string, Class<?> targetType, Property property)
            throws ReflectiveOperationException, ParseException
    {
        if (property != null && property.converter().length > 0)
        {
            // Apply custom converter specified in the annotation
            TypeConverter<?> converter = ConstructorUtils.invokeConstructor(property.converter()[0]);
            return converter.convert(string);
        }
        // Apply standard/default conversion
        return TypeFactory.parse(targetType, string);
    }
}
