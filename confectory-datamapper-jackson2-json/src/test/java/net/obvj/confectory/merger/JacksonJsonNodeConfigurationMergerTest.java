package net.obvj.confectory.merger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

import net.obvj.confectory.Configuration;
import net.obvj.confectory.mapper.JacksonJsonNodeMapper;
import net.obvj.confectory.source.StringSource;
import net.obvj.confectory.util.JacksonJsonNodeJsonProvider;

/**
 * Unit tests for the {@link JacksonJsonNodeConfigurationMerger}.
 *
 * @author oswaldo.bapvic.jr
 * @since 2.2.0
 */
class JacksonJsonNodeConfigurationMergerTest
{

    private static final String JSON_1
            = "{\n"
            + "  \"string\": \"value1\",\n"
            + "  \"array\": [1, 2, 3],\n"
            + "  \"object\": {\n"
            + "    \"a\": \"Json1ObjectA\",\n"
            + "    \"b\": \"Json1ObjectB\"\n"
            + "  }\n,"
            + "  \"alt\": \"alt1\""
            + "}";

    private static final String JSON_2
            = "{\n"
            + "  \"string\": \"value2\",\n"
            + "  \"array\": [3, 4, 5],\n"
            + "  \"object\": {\n"
            + "    \"a\": \"Json2ObjectA\",\n"
            + "    \"c\": \"Json2ObjectC\"\n"
            + "  }\n,"
            + "  \"number\": 9876"
            + "}";

    private static final List<Integer> EXPECTED_JSON_1_JSON_2_ARRAY = Arrays.asList(1, 2, 3, 4, 5);

    private static final String JSON_3
            = "{\n"
            + "  \"agents\": [\n"
            + "    {\n"
            + "      \"class\": \"Agent1\",\n"
            + "      \"description\": \"Json3Agent1\"\n"
            + "    },\n"
            + "    {\n"
            + "      \"class\": \"Agent2\",\n"
            + "      \"description\": \"Json3Agent2\"\n"
            + "    }\n"
            + "  ]\n"
            + "}";

    private static final String JSON_4
            = "{\n"
            + "  \"enabled\": true,\n"
            + "  \"agents\": [\n"
            + "    {\n"
            + "      \"class\": \"Agent1\",\n"
            + "      \"description\": \"Json4Agent1\"\n"
            + "    }\n"
            + "  ]\n"
            + "}";

    private static final List<String> EXPECTED_JSON_3_JSON_4_DESCRIPTIONS = Arrays.asList("Json3Agent1",
            "Json3Agent2", "Json4Agent1");

    private static final String JSON_5
            = "{\r\n"
            + "  \"name\": \"John\",\r\n"
            + "  \"address\": {\r\n"
            + "    \"postalCode\": \"630-0192\"\r\n"
            + "  },\r\n"
            + "  \"phoneNumbers\": [\r\n"
            + "    {\r\n"
            + "      \"type\": \"mobile\",\r\n"
            + "      \"number\": \"0123-4567-8888\"\r\n"
            + "    },\r\n"
            + "    {\r\n"
            + "      \"type\": \"home\",\r\n"
            + "      \"number\": \"0123-4567-8910\"\r\n"
            + "    }\r\n"
            + "  ]\r\n"
            + "}";

    private static final String JSON_6
            = "{\r\n"
            + "  \"name\": \"John\",\r\n"
            + "  \"address\": {},\r\n"
            + "  \"phoneNumbers\": [\r\n"
            + "    {\r\n"
            + "      \"type\": \"mobile\",\r\n"
            + "      \"number\": \"0123-4567-8888\"\r\n"
            + "    },\r\n"
            + "    {\r\n"
            + "      \"type\": \"work\",\r\n"
            + "      \"number\": \"0123-4567-9999\"\r\n"
            + "    }\r\n"
            + "  ]\r\n"
            + "}";

    private static final String JSON_7
            = "{\r\n"
            + "  \"address\": \"123 Street\"\r\n"
            + "}";

    private static final String JSON_8
            = "{\r\n"
            + "  \"array\": [\r\n"
            + "    {\r\n"
            + "      \"name\": \"name1\",\r\n"
            + "      \"value\": \"Json8Value1\"\r\n"
            + "    },\r\n"
            + "    \"element1\""
            + "  ]\r\n"
            + "}";

    private static final String JSON_9
            = "{\r\n"
            + "  \"array\": [\r\n"
            + "    {\r\n"
            + "      \"name\": \"name1\",\r\n"
            + "      \"value\": \"Json9Value1\"\r\n"
            + "    },\r\n"
            + "    \"element1\","
            + "    \"element2\""
            + "  ]\r\n"
            + "}";


    private static final JacksonJsonNodeJsonProvider PROVIDER = new JacksonJsonNodeJsonProvider();

    private final ConfigurationMerger<JsonNode> merger = new JacksonJsonNodeConfigurationMerger();

    private static Configuration<JsonNode> newConfiguration(String json, int precedence)
    {
        return Configuration.<JsonNode>builder().precedence(precedence).source(new StringSource<>(json))
                .mapper(new JacksonJsonNodeMapper()).build();
    }

    private static void assertArray(List<?> expected, Configuration<JsonNode> result, String jsonPath)
    {
        assertArray(expected, result, jsonPath, true);
    }

    private static void assertArray(List<?> expected, Configuration<JsonNode> result, String jsonPath,
            boolean exactSize)
    {
        ArrayNode array = (ArrayNode) result.get(jsonPath);
        if (exactSize)
        {
            assertEquals(expected.size(), array.size());
        }
        expected.forEach(expectedElement ->
        {
            assertTrue(PROVIDER.stream(array).anyMatch(PROVIDER.toJsonNode(expectedElement)::equals),
                    () -> String.format("Expected element %s not found", expectedElement));
        });
    }

    @Test
    void merge_json1HighWithJson2Low_success()
    {
        Configuration<JsonNode> result = merger
                .merge(newConfiguration(JSON_1, 9), newConfiguration(JSON_2, 1));

        assertEquals("value1", result.getString("string")); // from JSON_1
        assertEquals("alt1", result.getString("alt")); // from JSON_1
        assertEquals(9876, result.getInteger("number")); // from JSON_2
        assertArray(EXPECTED_JSON_1_JSON_2_ARRAY, result, "$.array[*]");

        assertEquals("Json1ObjectA", result.getString("$.object.a")); // from JSON_1
        assertEquals("Json1ObjectB", result.getString("$.object.b")); // from JSON_1
        assertEquals("Json2ObjectC", result.getString("$.object.c")); // from JSON_2
    }

    @Test
    void merge_json1LowWithJson2High_success()
    {
        Configuration<JsonNode> result = merger
                .merge(newConfiguration(JSON_1, 1), newConfiguration(JSON_2, 9));

        assertEquals("value2", result.getString("string")); // from JSON_2
        assertEquals("alt1", result.getString("alt")); // from JSON_1
        assertEquals(9876, result.getInteger("number")); // from JSON_2
        assertArray(EXPECTED_JSON_1_JSON_2_ARRAY, result, "$.array[*]");

        assertEquals("Json2ObjectA", result.getString("$.object.a")); // from JSON_2
        assertEquals("Json1ObjectB", result.getString("$.object.b")); // from JSON_1
        assertEquals("Json2ObjectC", result.getString("$.object.c")); // from JSON_2
    }

    @Test
    void merge_json3HighWithJson4Low_success()
    {
        Configuration<JsonNode> result = merger
                .merge(newConfiguration(JSON_3, 9), newConfiguration(JSON_4, 1));

        assertTrue(result.getBoolean("enabled")); // from JSON_4
        assertArray(EXPECTED_JSON_3_JSON_4_DESCRIPTIONS, result, "$.agents[*].description");
    }

    @Test
    void merge_json3LowWithJson4High_success()
    {
        Configuration<JsonNode> result = merger
                .merge(newConfiguration(JSON_3, 5), newConfiguration(JSON_4, 6));

        assertTrue(result.getBoolean("enabled")); // from JSON_4
        assertArray(EXPECTED_JSON_3_JSON_4_DESCRIPTIONS, result, "$.agents[*].description");
    }

    @Test
    void merge_json3HighWithJson4LowAndDistinctKey_success()
    {
        Configuration<JsonNode> result = merger
                .merge(newConfiguration(JSON_3, 9),
                       newConfiguration(JSON_4, 1),
                       JsonMergeOption.distinctKey("class", "$.agents"));

        assertTrue(result.getBoolean("enabled")); // from JSON_4
        assertArray(Arrays.asList("Json3Agent1", "Json3Agent2"), result, "$.agents[*].description");
    }

    @Test
    void merge_json3LowWithJson4HighAndDistinctKey_success()
    {
        Configuration<JsonNode> result = merger
                .merge(newConfiguration(JSON_3, 2),
                       newConfiguration(JSON_4, 3),
                       JsonMergeOption.distinctKey("class", "$.agents"));

        assertTrue(result.getBoolean("enabled")); // from JSON_4
        assertArray(Arrays.asList("Json4Agent1", "Json3Agent2"), result, "$.agents[*].description");
    }

    @Test
    void merge_json5HighWithJson6Low_success()
    {
        Configuration<JsonNode> result = merger
                .merge(newConfiguration(JSON_5, 9), newConfiguration(JSON_6, 8));

        assertEquals("0123-4567-8888", result.getString("$.phoneNumbers[?(@.type=='mobile')].number"));
        assertEquals("0123-4567-8910", result.getString("$.phoneNumbers[?(@.type=='home')].number"));
        assertEquals("0123-4567-9999", result.getString("$.phoneNumbers[?(@.type=='work')].number"));
        assertEquals("630-0192", result.getString("$.address.postalCode"));
    }

    @Test
    void merge_json5LowWithJson6High_success()
    {
        Configuration<JsonNode> result = merger.merge(newConfiguration(JSON_5, 9),
                newConfiguration(JSON_6, 10));

        assertEquals("0123-4567-8888", result.getString("$.phoneNumbers[?(@.type=='mobile')].number"));
        assertEquals("0123-4567-8910", result.getString("$.phoneNumbers[?(@.type=='home')].number"));
        assertEquals("0123-4567-9999", result.getString("$.phoneNumbers[?(@.type=='work')].number"));
        assertEquals("630-0192", result.getString("$.address.postalCode"));
    }

    @Test
    void merge_json5HighWithJson7Low_success()
    {
        assertEquals("630-0192", merger.merge(newConfiguration(JSON_5, 9), newConfiguration(JSON_7, 1))
                .getString("$.address.postalCode"));
    }

    @Test
    void merge_json5LowWithJson7High_success()
    {
        assertEquals("123 Street", merger.merge(newConfiguration(JSON_5, 9), newConfiguration(JSON_7, 10))
                .getString("$.address"));
    }

    @Test
    void merge_json8HighWithJson9LowAndDistinctKey_success()
    {
        Configuration<JsonNode> result = merger
                .merge(newConfiguration(JSON_8, 9),
                       newConfiguration(JSON_9, 1),
                       JsonMergeOption.distinctKey("name", "$.array"));

        assertEquals("Json8Value1", result.getString("$.array[?(@.name=='name1')].value"));
        assertArray(Arrays.asList("element1", "element2"), result, "$.array[*]", false);
    }

    @Test
    void merge_json8LowWithJson9HighAndDistinctKey_success()
    {
        Configuration<JsonNode> result = merger
                .merge(newConfiguration(JSON_8, 9),
                       newConfiguration(JSON_9, 10),
                       JsonMergeOption.distinctKey("name", "$.array"));

        assertEquals("Json9Value1", result.getString("$.array[?(@.name=='name1')].value"));
        assertArray(Arrays.asList("element1", "element2"), result, "$.array[*]", false);
    }
}
