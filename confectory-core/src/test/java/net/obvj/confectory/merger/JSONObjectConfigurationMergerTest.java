/*
 * Copyright 2022 obvj.net
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

package net.obvj.confectory.merger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static net.obvj.jsonmerge.JsonMergeOption.*;
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


    private final ConfigurationMerger<JSONObject> merger = new JSONObjectConfigurationMerger();

    private static Configuration<JSONObject> newConfiguration(String json, int precedence)
    {
        return Configuration.<JSONObject>builder().precedence(precedence).source(new StringSource<>(json))
                .mapper(new JSONObjectMapper()).build();
    }

    private static void assertArray(List<?> expected, Configuration<JSONObject> result, String jsonPath)
    {
        assertArray(expected, result, jsonPath, true);
    }

    private static void assertArray(List<?> expected, Configuration<JSONObject> result, String jsonPath, boolean exactSize)
    {
        JSONArray array = (JSONArray) result.get(jsonPath);
        if (exactSize)
        {
            assertEquals(expected.size(), array.size());
        }
        assertTrue(array.containsAll(expected));
    }

    @Test
    void merge_json1HighWithJson2Low_success()
    {
        Configuration<JSONObject> result = merger
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
        Configuration<JSONObject> result = merger
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
        Configuration<JSONObject> result = merger
                .merge(newConfiguration(JSON_3, 9), newConfiguration(JSON_4, 1));

        assertTrue(result.getBoolean("enabled")); // from JSON_4
        assertArray(EXPECTED_JSON_3_JSON_4_DESCRIPTIONS, result, "$.agents[*].description");
    }

    @Test
    void merge_json3LowWithJson4High_success()
    {
        Configuration<JSONObject> result = merger
                .merge(newConfiguration(JSON_3, 5), newConfiguration(JSON_4, 6));

        assertTrue(result.getBoolean("enabled")); // from JSON_4
        assertArray(EXPECTED_JSON_3_JSON_4_DESCRIPTIONS, result, "$.agents[*].description");
    }

    @Test
    void merge_json3HighWithJson4LowAndDistinctKey_success()
    {
        Configuration<JSONObject> result = merger
                .merge(newConfiguration(JSON_3, 9),
                       newConfiguration(JSON_4, 1),
                        onPath("$.agents").findObjectsIdentifiedBy("class")
                                .thenPickTheHighestPrecedenceOne());

        assertTrue(result.getBoolean("enabled")); // from JSON_4
        assertArray(Arrays.asList("Json3Agent1", "Json3Agent2"), result, "$.agents[*].description");
    }

    @Test
    void merge_json3LowWithJson4HighAndDistinctKey_success()
    {
        Configuration<JSONObject> result = merger
                .merge(newConfiguration(JSON_3, 2),
                       newConfiguration(JSON_4, 3),
                        onPath("$.agents").findObjectsIdentifiedBy("class")
                                .thenPickTheHighestPrecedenceOne());

        assertTrue(result.getBoolean("enabled")); // from JSON_4
        assertArray(Arrays.asList("Json4Agent1", "Json3Agent2"), result, "$.agents[*].description");
    }

    @Test
    void merge_json5HighWithJson6Low_success()
    {
        Configuration<JSONObject> result = merger
                .merge(newConfiguration(JSON_5, 9), newConfiguration(JSON_6, 8));

        assertEquals("0123-4567-8888", result.getString("$.phoneNumbers[?(@.type=='mobile')].number"));
        assertEquals("0123-4567-8910", result.getString("$.phoneNumbers[?(@.type=='home')].number"));
        assertEquals("0123-4567-9999", result.getString("$.phoneNumbers[?(@.type=='work')].number"));
        assertEquals("630-0192", result.getString("$.address.postalCode"));
    }

    @Test
    void merge_json5LowWithJson6High_success()
    {
        Configuration<JSONObject> result = merger.merge(newConfiguration(JSON_5, 9),
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
        Configuration<JSONObject> result = merger
                .merge(newConfiguration(JSON_8, 9),
                       newConfiguration(JSON_9, 1),
                        onPath("$.array").findObjectsIdentifiedBy("name")
                                .thenPickTheHighestPrecedenceOne());

        assertEquals("Json8Value1", result.getString("$.array[?(@.name=='name1')].value"));
        assertArray(Arrays.asList("element1", "element2"), result, "$.array[*]", false);
    }

    @Test
    void merge_json8LowWithJson9HighAndDistinctKey_success()
    {
        Configuration<JSONObject> result = merger
                .merge(newConfiguration(JSON_8, 9),
                       newConfiguration(JSON_9, 10),
                        onPath("$.array").findObjectsIdentifiedBy("name")
                                .thenPickTheHighestPrecedenceOne());

        assertEquals("Json9Value1", result.getString("$.array[?(@.name=='name1')].value"));
        assertArray(Arrays.asList("element1", "element2"), result, "$.array[*]", false);
    }

    @Test
    void merge_json8LowWithJson9HighAndUnknownDistinctKey_success()
    {
        Configuration<JSONObject> result = merger
                .merge(newConfiguration(JSON_8, 9),
                       newConfiguration(JSON_9, 10),
                        onPath("$.array").findObjectsIdentifiedBy("unknown")
                                .thenPickTheHighestPrecedenceOne());

        // No exception expected, but the merge will consider no distinct key
        assertTrue(((JSONArray) result.get("$.array[?(@.name=='name1')].value"))
                .containsAll(Arrays.asList("Json9Value1", "Json8Value1")));
    }

    @Test
    void merge_jsonFilesWithTwoDistinctKeys_success()
    {
        Configuration<JSONObject> config = Configuration.<JSONObject>builder()
                .source("testfiles/drive1.json")
                .mapper(new JSONObjectMapper())
                .precedence(1).build()
                    .merge(Configuration.<JSONObject>builder()
                        .source("testfiles/drive2.json")
                        .mapper(new JSONObjectMapper())
                        .precedence(2).build(),
                        onPath("$.files").findObjectsIdentifiedBy("id", "version")
                                .thenPickTheHighestPrecedenceOne());

        assertEquals(Arrays.asList("1", "2", "3"),
                config.get("$.files[?(@.id=='d2b638be-40d2-4965-906e-291521f8a19d')].version"));

        assertEquals(Arrays.asList("1", "2"),
                config.get("$.files[?(@.id=='9570cc646-1586-11ed-861d-0242ac120002')].version"));

        // drive2.json
        assertEquals("2017-07-07T10:14:59", config.getString(
                "$.files[?(@.id=='9570cc646-1586-11ed-861d-0242ac120002' && @.version=='1')].date"));
    }

    @Test
    void merge_jsonFilesWithTwoDistinctKeysAlt_success()
    {
        Configuration<JSONObject> config = Configuration.<JSONObject>builder()
                .source("testfiles/drive1.json")
                .mapper(new JSONObjectMapper())
                .precedence(2).build()
                    .merge(Configuration.<JSONObject>builder()
                        .source("testfiles/drive2.json")
                        .mapper(new JSONObjectMapper())
                        .precedence(0).build(),
                        onPath("$.files").findObjectsIdentifiedBy("id", "version")
                                .thenPickTheHighestPrecedenceOne());

        assertEquals(Arrays.asList("1", "2", "3"),
                config.get("$.files[?(@.id=='d2b638be-40d2-4965-906e-291521f8a19d')].version"));

        assertEquals(Arrays.asList("1", "2"),
                config.get("$.files[?(@.id=='9570cc646-1586-11ed-861d-0242ac120002')].version"));

        // drive1.json
        assertEquals("2022-08-06T09:51:40", config.getString(
                "$.files[?(@.id=='9570cc646-1586-11ed-861d-0242ac120002' && @.version=='1')].date"));
    }
}
