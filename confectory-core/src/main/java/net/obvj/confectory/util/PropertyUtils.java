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

import java.lang.reflect.Field;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.ConstructorUtils;

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
     * Returns either the field name, or the value specified in the {@code @}{@link Property}
     * annotation (if not null).
     *
     * @param property the {@link Property} annotation to be evaluated (null is allowed)
     * @param field    the {@link Field} to be evaluated (not null)
     *
     * @return the field name, or the key specified in the {@code @}{@link Property}
     *         annotation, if present in the field.
     *
     * @throws NullPointerException if the field is null
     */
    public static String getPropertyOrFieldName(Property property, Field field)
    {
        if (property != null)
        {
            String key = StringUtils.defaultIfEmpty(property.value(), property.key());
            if (StringUtils.isNotEmpty(key))
            {
                return key;
            }
        }
        return field.getName();
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
     * @throws ReflectiveOperationException  if an error occurs in the reflective operation
     * @throws UnsupportedOperationException if the specified type is not supported
     * @throws ParseException                if an error is encountered while parsing
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
