package net.obvj.confectory.helper;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.function.Supplier;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.jayway.jsonpath.spi.json.JsonSmartJsonProvider;
import com.jayway.jsonpath.spi.mapper.JsonSmartMappingProvider;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

/**
 * Unit tests for the {@link AbstractJsonConfigurationHelper}.
 *
 * @author oswaldo.bapvic.jr (Oswaldo Junior)
 * @since 0.3.0
 */
@ExtendWith(MockitoExtension.class)
class AbstractJsonConfigurationHelperTest
{
    private static final JSONObject TEST_JSON_SAMPLE1 = new JSONObject();

    static
    {
        TEST_JSON_SAMPLE1.put("intValue", 9);
        TEST_JSON_SAMPLE1.put("longValue", 9876543210L);
        TEST_JSON_SAMPLE1.put("booleanValue", true);
        TEST_JSON_SAMPLE1.put("stringValue", "test");
        TEST_JSON_SAMPLE1.put("doubleValue", 7.89);
    }

    /*
     * Simple concrete helper only for testing purposes.
     */
    private static final AbstractJsonConfigurationHelper<JSONObject> HELPER = new AbstractJsonConfigurationHelper<JSONObject>(
            TEST_JSON_SAMPLE1, new JsonSmartJsonProvider(), new JsonSmartMappingProvider())
    {

        @Override
        protected <T> T getValue(String jsonPath, Class<T> targetType, Supplier<T> defaultSupplier)
        {
            JSONArray result = super.documentContext.read(jsonPath, JSONArray.class);
            return super.mappingProvider.map(result.get(0), targetType, super.jsonPathConfiguration);
        }

    };

    @Test
    void getBean_notEmpty()
    {
        assertThat(HELPER.getBean().get(), is(sameInstance(TEST_JSON_SAMPLE1)));
    }

    @Test
    void getBooleanProperty_existingKey_success()
    {
        assertThat(HELPER.getBoolean("$.booleanValue"), equalTo(true));
    }

    @Test
    void getIntProperty_existingKey_success()
    {
        assertThat(HELPER.getInt("$.intValue"), equalTo(9));
    }

    @Test
    void getLongProperty_existingKey_success()
    {
        assertThat(HELPER.getLong("$.longValue"), equalTo(9876543210L));
    }

    @Test
    void getDoubleProperty_existingKey_success()
    {
        assertThat(HELPER.getDouble("$.doubleValue"), equalTo(7.89));
    }

    @Test
    void getSringProperty_existingKey_success()
    {
        assertThat(HELPER.getString("$.stringValue"), equalTo("test"));
    }

}
