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
import org.objenesis.ObjenesisStd;

/**
 * A factory that encapsulates the logic to reflectively produce new objects depending on
 * the desired strategy.
 *
 * @author oswaldo.bapvic.jr (Oswaldo Junior)
 * @since 2.5.0
 */
public enum ObjectFactory
{
    /**
     * Constructor-based object factory.
     * <p>
     * It's safer but it requires the existence of a default (public and no-arguments)
     * constructor in the class to allow the instantiation.
     */
    CONSTRUCTOR_BASED
    {
        @Override
        public <T> T newObject(Class<T> type) throws ReflectiveOperationException
        {
            return ConstructorUtils.invokeConstructor(type);
        }
    },

    /**
     * A factory that builds objects by allocating an instance directly on the heap, without
     * any constructor being called.
     * <p>
     * Final fields are assigned with default values as specified by
     * <a href="https://docs.oracle.com/javase/tutorial/java/nutsandbolts/datatypes.html">
     * Java<sup>TM</sup> Tutorials &gt; Language Basics &gt; Variables &gt; Primitive Data
     * Types</a>, i.e., zero for numeric types, {@code false} for {@code boolean}, and
     * {@code null} for String (or any object type).
     * <p>
     * <b>Note:</b> This is the default strategy since 2.5.0.
     */
    FAST
    {
        @Override
        public <T> T newObject(Class<T> type) throws ReflectiveOperationException
        {
            return type.cast(UnsafeAccessor.UNSAFE.allocateInstance(type));
        }
    },

    /**
     * Alternative object factory that uses a variety of approaches to attempt to instantiate
     * the object, depending on the type of object, JVM version, JVM vendor and Security
     * Manager present.
     * <p>
     * <b>IMPORTANT:</b> This strategy requires the <b>optional dependency</b>
     * {@code org.objenesis:objenesis} in the class path.
     */
    OBJENESIS
    {
        @Override
        public <T> T newObject(Class<T> type)
        {
            return new ObjenesisStd().newInstance(type);
        }
    };

    /**
     * Creates a new instance of the specified class.
     *
     * @param <T>  the target type
     * @param type the target type
     * @return a new instance of the specified class
     *
     * @throws ReflectiveOperationException in case of failure during the instantiation
     */
    public abstract <T> T newObject(Class<T> type) throws ReflectiveOperationException;

}
