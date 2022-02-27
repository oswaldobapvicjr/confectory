package net.obvj.confectory.mapper;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import net.obvj.confectory.internal.helper.GsonJsonObjectHelper;

/**
 * Unit tests for the {@link GsonJsonObjectMapper} class.
 *
 * @author oswaldo.bapvic.jr
 * @since 0.4.0
 */
@ExtendWith(MockitoExtension.class)
class GsonJsonObjectMapperTest
{
    private static final String TEST_JSON_SAMPLE1 = "{"
                                                  + "  \"intValue\": 9,"
                                                  + "  \"booleanValue\": true,"
                                                  + "  \"array\": ["
                                                  + "    \"string1\","
                                                  + "    \"string2\""
                                                  + "  ]"
                                                  + "}";
    @Mock
    private JsonObject jsonNode;

    private Mapper<JsonObject> mapper = new GsonJsonObjectMapper();

    private ByteArrayInputStream toInputStream(String content)
    {
        return new ByteArrayInputStream(content.getBytes());
    }

    @Test
    void apply_validInputStream_validJsonNode() throws IOException
    {
        JsonObject result = mapper.apply(toInputStream(TEST_JSON_SAMPLE1));
        assertThat(result.size(), equalTo(3));
        assertThat(result.get("intValue").getAsInt(), equalTo(9));
        assertThat(result.get("booleanValue").getAsBoolean(), equalTo(true));

        JsonArray array = result.get("array").getAsJsonArray();
        assertThat(array.size(), equalTo(2));
        assertThat(array.get(0).getAsString(), equalTo("string1"));
        assertThat(array.get(1).getAsString(), equalTo("string2"));
    }

    @Test
    void configurationHelper_jsonObjectConfigurationHelper()
    {
        assertThat(mapper.configurationHelper(jsonNode).getClass(), equalTo(GsonJsonObjectHelper.class));
    }

}
