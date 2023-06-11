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

import java.lang.reflect.Constructor;

import sun.misc.Unsafe;

/**
 * A class that allows to get access to {@code sun.misc.Unsafe}.
 *
 * @author oswaldo.bapvic.jr
 * @since 2.5.0
 */
@SuppressWarnings("restriction")
public final class UnsafeAccessor
{
    /**
     * A object with capabilities to performs low-level operations in the JVM.
     */
    public static final Unsafe UNSAFE = getUnsafe();

    private static Unsafe getUnsafe()
    {
        try
        {
            Constructor<Unsafe> constructor = Unsafe.class.getDeclaredConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance();
        }
        catch (ReflectiveOperationException exception)
        {
            throw new UnsupportedOperationException(
                    "Unable to get hold of an instance of sun.misc.Unsafe", exception);
        }
    }

    private UnsafeAccessor()
    {
        throw new UnsupportedOperationException("Instantiation not allowed");
    }

}
