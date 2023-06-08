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

import org.apache.commons.lang3.reflect.ConstructorUtils;

/**
 * A factory that encapsulates the logic to produce new objects for a given class.
 *
 * @author oswaldo.bapvic.jr
 * @since 2.5.0
 */
public enum ObjectFactory
{
    /**
     * Constructor-based object factory.
     * <p>
     * It's safer but requires on the existence of a public, default constructor in the
     * classes to allow the instantiation.
     */
    CLASSIC
    {
        @Override
        public <T> T newObject(Class<T> type) throws ReflectiveOperationException
        {
            return ConstructorUtils.invokeConstructor(type);
        }
    },

    /**
     * Enhanced object factory that can build objects without requiring a default constructor
     * declared in the class.
     * <p>
     * The default constructor, if present, is bypassed and an empty instance is created (all
     * fields assigned with
     * <a href="https://docs.oracle.com/javase/tutorial/java/nutsandbolts/datatypes.html">
     * default values</a>).
     */
    ENHANCED
    {
        @Override
        public <T> T newObject(Class<T> type) throws ReflectiveOperationException
        {
            return (T) UnsafeAccessor.UNSAFE.allocateInstance(type);
        }
    };

    /**
     * Creates a new instance of the specified class.
     *
     * @param <T>  the target type
     * @param type the target type
     * @return a new instance of the specified class
     *
     * @throws ReflectiveOperationException
     */
    public abstract <T> T newObject(Class<T> type) throws ReflectiveOperationException;
}
