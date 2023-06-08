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
 * @author oswaldo.bapvic.jr
 * @since 2.5.0
 */
public enum ObjectFactory
{
    /**
     * Constructor-based object factory.
     * <p>
     * It's safer but requires the existence of a public, default constructor available in the
     * class to allow the instantiation.
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
     * Object factory that builds objects by allocating an instance directly on the heap,
     * without any constructor being called.
     * <p>
     * Final fields are assigned with
     * <a href="https://docs.oracle.com/javase/tutorial/java/nutsandbolts/datatypes.html">
     * default values</a>.
     * <p>
     * <b>Note:</b> This is the default strategy since 2.5.0.
     */
    UNSAFE
    {
        @Override
        @SuppressWarnings("restriction")
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
     * @throws ReflectiveOperationException
     */
    public abstract <T> T newObject(Class<T> type) throws ReflectiveOperationException;

}
