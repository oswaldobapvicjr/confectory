package net.obvj.confectory.helper;

import static net.obvj.junit.utils.matchers.AdvancedMatchers.throwsException;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.MatcherAssert.assertThat;

import org.hamcrest.Matcher;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

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

    private static final GenericJsonConfigurationHelper<JSONObject> HELPER = new SmartJsonConfigurationHelper(
            TEST_JSON_SAMPLE1);

    private static final String PATH_UNKNOWN = "$.unknown";
    private static final Matcher<Runnable> EXCEPTION_NO_VALUE_FOUND_PATH_UNKNOWN = throwsException(
            ConfigurationException.class).withMessageContaining("No value found", PATH_UNKNOWN);

    @Test
    void getBean_notEmpty()
    {
        assertThat(HELPER.getBean(), is(sameInstance(TEST_JSON_SAMPLE1)));
    }

    @Test
    void getBoolean_existingKey_success()
    {
        assertThat(HELPER.getBoolean("$.booleanValue"), equalTo(true));
    }

    @Test
    void getBoolean_unknownKey_null()
    {
        assertThat(HELPER.getBoolean(PATH_UNKNOWN), equalTo(null));
    }

    @Test
    void getMandatoryBoolean_existingKey_success()
    {
        assertThat(HELPER.getMandatoryBoolean("$.booleanValue"), equalTo(true));
    }

    @Test
    void getMandatoryBoolean_unknownKey_exception()
    {
        assertThat(() -> HELPER.getMandatoryBoolean(PATH_UNKNOWN), EXCEPTION_NO_VALUE_FOUND_PATH_UNKNOWN);
    }

    @Test
    void getInteger_existingKey_success()
    {
        assertThat(HELPER.getInteger("$.intValue"), equalTo(9));
    }

    @Test
    void getInteger_unknownKey_null()
    {
        assertThat(HELPER.getInteger(PATH_UNKNOWN), equalTo(null));
    }

    @Test
    void getMandatoryInteger_existingKey_success()
    {
        assertThat(HELPER.getMandatoryInteger("$.intValue"), equalTo(9));
    }

    @Test
    void getMandatoryInteger_unknownKey_exception()
    {
        assertThat(() -> HELPER.getMandatoryInteger(PATH_UNKNOWN), EXCEPTION_NO_VALUE_FOUND_PATH_UNKNOWN);
    }

    @Test
    void getLong_existingKey_success()
    {
        assertThat(HELPER.getLong("$.longValue"), equalTo(9876543210L));
    }

    @Test
    void getLong_unknownKey_null()
    {
        assertThat(HELPER.getLong(PATH_UNKNOWN), equalTo(null));
    }

    @Test
    void getMandatoryLong_existingKey_success()
    {
        assertThat(HELPER.getMandatoryLong("$.longValue"), equalTo(9876543210L));
    }

    @Test
    void getMandatoryLong_unknownKey_exception()
    {
        assertThat(() -> HELPER.getMandatoryLong(PATH_UNKNOWN), EXCEPTION_NO_VALUE_FOUND_PATH_UNKNOWN);
    }

    @Test
    void getDouble_existingKey_success()
    {
        assertThat(HELPER.getDouble("$.doubleValue"), equalTo(7.89));
    }

    @Test
    void getDouble_unknownKey_null()
    {
        assertThat(HELPER.getDouble(PATH_UNKNOWN), equalTo(null));
    }

    @Test
    void getMandatoryDouble_existingKey_success()
    {
        assertThat(HELPER.getMandatoryDouble("$.doubleValue"), equalTo(7.89));
    }

    @Test
    void getMandatoryDouble_unknownKey_exception()
    {
        assertThat(() -> HELPER.getMandatoryDouble(PATH_UNKNOWN), EXCEPTION_NO_VALUE_FOUND_PATH_UNKNOWN);
    }

    @Test
    void getString_existingKey_success()
    {
        assertThat(HELPER.getString("$.stringValue"), equalTo("test"));
    }

    @Test
    void getSring_unknownKey_null()
    {
        assertThat(HELPER.getString(PATH_UNKNOWN), equalTo(null));
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
                throwsException(ConfigurationException.class).withMessageContaining("Multiple values found"));
    }

    @Test
    void getMandatoryString_existingKey_success()
    {
        assertThat(HELPER.getMandatoryString("$.stringValue"), equalTo("test"));
    }

    @Test
    void getMandatoryString_unknownKey_exception()
    {
        assertThat(() -> HELPER.getMandatoryString(PATH_UNKNOWN), EXCEPTION_NO_VALUE_FOUND_PATH_UNKNOWN);
    }

}
