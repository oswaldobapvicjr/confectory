package net.obvj.confectory.mapper;

import java.util.Objects;
import java.util.Properties;

import org.apache.commons.collections4.keyvalue.MultiKey;
import org.apache.commons.collections4.map.MultiKeyMap;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.ConstructorUtils;
import org.w3c.dom.Document;

import net.minidev.json.JSONObject;


public class MapperFactory
{
    private static final MultiKeyMap<String, String> MAPPERS_BY_TARGET_TYPE_AND_INPUT_FORMAT = new MultiKeyMap<>();

    static
    {
        registerCoreMappers();
    }

    private static void registerCoreMappers()
    {
        registerMapper(Properties.class, "properties", PropertiesMapper.class);
        registerMapper(Document.class, "xml", DocumentMapper.class);
        registerMapper(JSONObject.class, "json", JSONObjectMapper.class);
        registerMapper(JSONObject.class, "ini", INIToJSONObjectMapper.class);
    }

    public static void registerMapper(Class<?> targetType, String inputFormat,
            Class<? extends Mapper<?>> mapperClass)
    {
        MultiKey<String> multiKey = new MultiKey<>(targetType.getCanonicalName(), StringUtils.lowerCase(inputFormat));
        MAPPERS_BY_TARGET_TYPE_AND_INPUT_FORMAT.put(multiKey, mapperClass.getCanonicalName());

    }

    public static <T> Mapper<T> of(Class<T> targetType, String inputFormat)
    {
        return of(targetType, inputFormat, null);
    }

    public static <T> Mapper<T> of(Class<T> targetType, String inputFormat, String provider)
    {
        Objects.requireNonNull(targetType, "The target type must not be null");
        String targetTypeClassName = targetType.getCanonicalName();
        String mapperClassName = MAPPERS_BY_TARGET_TYPE_AND_INPUT_FORMAT.get(targetTypeClassName, StringUtils.lowerCase(inputFormat));
        if (mapperClassName == null)
        {
            throw new IllegalArgumentException("No Mapper found from " + inputFormat + " to " + targetType);
        }
        return newInstanceOf(mapperClassName);
    }

    private static <T> Mapper<T> newInstanceOf(String mapperClassName)
    {
        try
        {
            Class mapperClass = ClassUtils.getClass(mapperClassName);
            return (Mapper<T>) ConstructorUtils.invokeConstructor(mapperClass);
        }
        catch (ReflectiveOperationException exception)
        {
            throw new IllegalStateException(exception);
        }
    }

    public static void main(String[] args)
    {
        Mapper<JSONObject> mapper = of(JSONObject.class, "ini");
        System.out.println(mapper);
    }
}
