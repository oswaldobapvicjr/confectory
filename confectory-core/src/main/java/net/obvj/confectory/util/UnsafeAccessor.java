package net.obvj.confectory.util;

import java.lang.reflect.Constructor;

import sun.misc.Unsafe;

public class UnsafeAccessor
{
    public static final Unsafe UNSAFE = getUnsafe();

    private static Unsafe getUnsafe()
    {
        try
        {
            Constructor<Unsafe> constructor = Unsafe.class.getDeclaredConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance();
        }
        catch (ReflectiveOperationException e)
        {
            throw new UnsupportedOperationException(e);
        }
    }
}
