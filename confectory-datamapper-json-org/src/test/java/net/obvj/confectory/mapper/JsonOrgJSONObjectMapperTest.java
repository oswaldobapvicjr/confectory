package net.obvj.confectory.mapper;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import net.obvj.confectory.helper.JsonOrgJSONObjectHelper;

/**
 * Unit tests for the {@link JsonOrgJSONObjectMapper} class.
 *
 * @author oswaldo.bapvic.jr
 * @since 0.2.0
 */
class JsonOrgJSONObjectMapperTest
{
    private static final String TEST_JSON_SAMPLE1 = "{"
                                                  + "  \"intValue\": 9,"
                                                  + "  \"booleanValue\": true,"
                                                  + "  \"array\": ["
                                                  + "    \"string1\","
                                                  + "    \"string2\""
                                                  + "  ]"
                                                  + "}";

    private Mapper<JSONObject> mapper = new JsonOrgJSONObjectMapper();

    private ByteArrayInputStream toInputStream(String content)
    {
        return new ByteArrayInputStream(content.getBytes());
    }

    @Test
    void apply_validInputStream_validJSONObject() throws IOException
    {
        JSONObject result = mapper.apply(toInputStream(TEST_JSON_SAMPLE1));
        assertThat(result.length(), equalTo(3));
        assertThat(result.get("intValue"), equalTo(9));
        assertThat(result.get("booleanValue"), equalTo(true));

        JSONArray array = result.getJSONArray("array");
        assertThat(array.length(), equalTo(2));
        assertThat(array.get(0), equalTo("string1"));
        assertThat(array.get(1), equalTo("string2"));
    }

    @Test
    void configurationHelper_jsonObjectConfigurationHelper()
    {
        assertThat(mapper.configurationHelper(new JSONObject()).getClass(), equalTo(JsonOrgJSONObjectHelper.class));
    }

}
