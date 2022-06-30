package net.obvj.confectory.util;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import net.minidev.json.JSONArray;

/**
 * Unit tests for the {@link JsonSmartJsonProvider} class.
 *
 * @author oswaldo.bapvic.jr
 * @since 2.2.0
 */
class JsonSmartJsonProviderTest
{
    private static final JSONArray ARRAY1 = new JSONArray();

    static
    {
        ARRAY1.add("element1");
        ARRAY1.add("element2");
    }

    private JsonProvider provider = new JsonSmartJsonProvider();

    @Test
    void newJsonArray_emptyJsonArray()
    {
        assertEquals(new JSONArray(), provider.newJsonArray());
    }

    @Test
    void newJsonArray_sourceJsonArray_copy()
    {
        Object result = provider.newJsonArray(ARRAY1);
        assertEquals(ARRAY1, result);
        assertNotSame(ARRAY1, result);
    }

}
