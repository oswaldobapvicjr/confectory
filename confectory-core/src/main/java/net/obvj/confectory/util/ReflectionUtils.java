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

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * Common methods for working with reflections.
 *
 * @author oswaldo.bapvic.jr (Oswaldo Junior)
 * @since 2.0.0
 */
public class ReflectionUtils
{

    private ReflectionUtils()
    {
        throw new IllegalStateException("Instantiation not allowed");
    }

    /**
     * Evaluates if the specified field is marked {@code transient}.
     *
     * @param field the {@link Field} to be evaluated
     * @return {@code true} if the field contains the {@code transient} modifier;
     *         {@code false}, otherwise
     * @throws NullPointerException if the field is null
     */
    public static boolean isTransient(final Field field)
    {
        return Modifier.isTransient(field.getModifiers());
    }
}
