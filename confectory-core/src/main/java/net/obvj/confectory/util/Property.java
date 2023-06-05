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

package net.obvj.confectory.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import net.obvj.confectory.mapper.PropertiesToObjectMapper;

/**
 * Marker annotation that can be used to indicate that an object field shall be associated
 * with the <b>property key</b> defined by the annotation value.
 * <p>
 * Here is an example of how this annotation is meant to be used:
 * </p>
 * <blockquote>
 *
 * <pre>
 * public class MyClass {
 *     &#64;Property("host") String myHost;
 *     &#64;Property("port") int myPort;
 *   ...
 * }
 * </pre>
 *
 * </blockquote>
 * <p>
 * <strong>NOTE:</strong> The default value ({@code ""}) indicates that the field name
 * will be used as the property key.
 * </p>
 *
 * @author oswaldo.bapvic.jr
 * @since 1.2.0
 *
 * @see PropertiesToObjectMapper
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
public @interface Property
{

    /**
     * @return the property key from the source file/URL to be associated with this field
     */
    String value() default "";

    /**
     * Optionally specify a {@link TypeConverter} class to convert this property value into a
     * strongly typed object.
     * <p>
     * This is useful when a particular field should use a custom conversion that is different
     * from the normal conversion for the field's type (which is applied by the
     * {@link TypeFactory}).
     *
     * @return the type converter to convert String values into a strongly typed object for
     *         this property
     * @since 2.5.0
     */
    Class<? extends TypeConverter<?>>[] converter() default {};

}
