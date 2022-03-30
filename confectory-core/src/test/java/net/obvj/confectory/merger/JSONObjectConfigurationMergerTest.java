package net.obvj.confectory.merger;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.obvj.confectory.Configuration;
import net.obvj.confectory.mapper.JSONObjectMapper;
import net.obvj.confectory.source.StringSource;

/**
 * Unit tests for the {@link JSONObjectConfigurationMerger}.
 *
 * @author oswaldo.bapvic.jr
 * @since 2.1.0
 */
class JSONObjectConfigurationMergerTest
{

    private static final String JSON_1
            = "{\n"
            + "  \"string\": \"value1\",\n"
            + "  \"array\": [1, 2, 3],\n"
            + "  \"alt\": \"alt1\""
            + "}";

    private static final String JSON_2
            = "{\n"
            + "  \"string\": \"value2\",\n"
            + "  \"array\": [3, 4, 5],\n"
            + "  \"number\": \"9876\""
            + "}";

    private static final List<Integer> EXPECTED_JSON_1_WITH_JSON_2_ARRAY = Arrays.asList(1, 2, 3, 4, 5);

    private static final String JSON_WITH_DISTINCT_KEY_ARRAY_1
            = "{\n"
            + "  \"agents\": [\n"
            + "    {\n"
            + "      \"class\": \"Agent1\",\n"
            + "      \"interval\": \"*/2 * * * *\"\n"
            + "    },\n"
            + "    {\n"
            + "      \"class\": \"Agent2\",\n"
            + "      \"interval\": \"90s\"\n"
            + "    }\n"
            + "  ]\n"
            + "}";

    private static final String JSON_WITH_DISTINCT_KEY_ARRAY_2
            = "{\n"
            + "  \"enabled\": true,\n"
            + "  \"agents\": [\n"
            + "    {\n"
            + "      \"class\": \"Agent2\",\n"
            + "      \"interval\": \"10s\"\n"
            + "    }\n"
            + "  ]\n"
            + "}";

    private static Configuration<JSONObject> newConfiguration(String json, int precedence)
    {
        return Configuration.<JSONObject>builder().precedence(precedence).source(new StringSource<>(json))
                .mapper(new JSONObjectMapper()).build();
    }

    private static void assertArray(List<?> expected, Configuration<JSONObject> result, String key)
    {
        JSONArray array = (JSONArray) ((JSONArray) result.get(key)).get(0);
        assertEquals(expected.size(), array.size());
        assertTrue(array.containsAll(expected));
    }

    @Test
    void merge_json1HighWithJson2Low_sucess()
    {
        Configuration<JSONObject> result = new JSONObjectConfigurationMerger()
                .merge(newConfiguration(JSON_1, 9), newConfiguration(JSON_2, 1));

        assertEquals("value1", result.getString("string")); // from JSON_1
        assertEquals("alt1", result.getString("alt")); // from JSON_1
        assertEquals(9876, result.getInteger("number")); // from JSON_2
        assertArray(EXPECTED_JSON_1_WITH_JSON_2_ARRAY, result, "array");
    }

    @Test
    void merge_json1LowWithJson2High_sucess()
    {
        Configuration<JSONObject> result = new JSONObjectConfigurationMerger()
                .merge(newConfiguration(JSON_1, 1), newConfiguration(JSON_2, 9));

        assertEquals("value2", result.getString("string")); // from JSON_2
        assertEquals("alt1", result.getString("alt")); // from JSON_1
        assertEquals(9876, result.getInteger("number")); // from JSON_2
        assertArray(EXPECTED_JSON_1_WITH_JSON_2_ARRAY, result, "array");
    }

}
