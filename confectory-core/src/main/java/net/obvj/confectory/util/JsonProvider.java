package net.obvj.confectory.util;

import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Stream;

public interface JsonProvider
{

    boolean isJsonObject(Object object);

    boolean isJsonArray(Object object);

    boolean isEmpty(Object jsonObject);

    Object newJsonObject();

    Object newJsonObject(Object sourceJsonObject);

    Object newJsonArray();

    Object newJsonArray(Object sourceJsonArray);

    Set<Map.Entry<String, Object>> entrySet(Object jsonObject);

    Object get(Object jsonObject, String key);

    Object put(Object jsonObject, String key, Object value);

    boolean add(Object jsonArray, Object element);

    void forEachEntryInJsonObject(Object jsonObject, BiConsumer<? super String, ? super Object> action);

    void forEachElementInArray(Object jsonArray, Consumer<? super Object> action);

    Object putIfAbsent(Object jsonObject, String key, Object value);

    boolean arrayContains(Object jsonArray, Object element);

    Stream<Object> stream(Object jsonArray);
}
