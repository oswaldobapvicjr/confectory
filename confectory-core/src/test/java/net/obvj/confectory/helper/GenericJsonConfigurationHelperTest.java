package net.obvj.confectory.helper;

import static net.obvj.junit.utils.matchers.AdvancedMatchers.throwsException;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.jayway.jsonpath.spi.json.JsonSmartJsonProvider;
import com.jayway.jsonpath.spi.mapper.JsonSmartMappingProvider;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.obvj.confectory.ConfigurationException;

/**
 * Unit tests for the {@link GenericJsonConfigurationHelper}.
 *
 * @author oswaldo.bapvic.jr (Oswaldo Junior)
 * @since 0.3.0
 */
@ExtendWith(MockitoExtension.class)
class GenericJsonConfigurationHelperTest
{
    private static final JSONObject TEST_JSON_SAMPLE1 = new JSONObject();

    static
    {
        TEST_JSON_SAMPLE1.put("intValue", 9);
        TEST_JSON_SAMPLE1.put("longValue", 9876543210L);
        TEST_JSON_SAMPLE1.put("booleanValue", true);
        TEST_JSON_SAMPLE1.put("stringValue", "test");
        TEST_JSON_SAMPLE1.put("doubleValue", 7.89);

        JSONArray array = new JSONArray();
        array.add("element1");
        array.add("element2");
        TEST_JSON_SAMPLE1.put("array", array);
    }

    // Simple concrete helper for testing purposes
    private static final GenericJsonConfigurationHelper<JSONObject> HELPER = new GenericJsonConfigurationHelper<JSONObject>(
            TEST_JSON_SAMPLE1, new JsonSmartJsonProvider(), new JsonSmartMappingProvider());

    @Test
    void getBean_notEmpty()
    {
        assertThat(HELPER.getBean().get(), is(sameInstance(TEST_JSON_SAMPLE1)));
    }

    @Test
    void getBoolean_existingKey_success()
    {
        assertThat(HELPER.getBoolean("$.booleanValue"), equalTo(true));
    }

    @Test
    void getBoolean_unknownKey_false()
    {
        assertThat(HELPER.getBoolean("$.unknown"), equalTo(false));
    }

    @Test
    void getInt_existingKey_success()
    {
        assertThat(HELPER.getInt("$.intValue"), equalTo(9));
    }

    @Test
    void getInt_unknownKey_zero()
    {
        assertThat(HELPER.getInt("$.unknown"), equalTo(0));
    }

    @Test
    void getLong_existingKey_success()
    {
        assertThat(HELPER.getLong("$.longValue"), equalTo(9876543210L));
    }

    @Test
    void getLong_unknownKey_zero()
    {
        assertThat(HELPER.getLong("$.unknown"), equalTo(0L));
    }

    @Test
    void getDouble_existingKey_success()
    {
        assertThat(HELPER.getDouble("$.doubleValue"), equalTo(7.89));
    }

    @Test
    void getDouble_unknownKey_zero()
    {
        assertThat(HELPER.getDouble("$.unknown"), equalTo(0.0));
    }

    @Test
    void getSring_existingKey_success()
    {
        assertThat(HELPER.getString("$.stringValue"), equalTo("test"));
    }

    @Test
    void getSring_unknownKey_empty()
    {
        assertThat(HELPER.getString("$.unknown"), equalTo(""));
    }

    @Test
    void getString_singleElement_success()
    {
        assertThat(HELPER.getString("$.array[0]"), equalTo("element1"));
    }

    @Test
    void getString_multipleElements_configurationException()
    {
        assertThat(() -> HELPER.getString("$.array[*]"),
                throwsException(ConfigurationException.class).withMessageContaining("more than one element"));
    }

}
