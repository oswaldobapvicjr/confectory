package net.obvj.confectory.helper;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.MatcherAssert.assertThat;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Unit tests for the {@link JSONObjectHelper}.
 *
 * @author oswaldo.bapvic.jr (Oswaldo Junior)
 * @since 0.2.0
 */
@ExtendWith(MockitoExtension.class)
class JSONObjectHelperTest
{
    private static final JSONObject TEST_JSON_SAMPLE1 = new JSONObject("{\r\n"
            + "  \"intValue\": 9,\r\n"
            + "  \"longValue\": 9876543210,\r\n"
            + "  \"booleanValue\": true,\r\n"
            + "  \"store\": {\r\n"
            + "    \"books\": [ \r\n"
            + "      { \"category\": \"children\",\r\n"
            + "        \"author\": \"Julia Donaldson\",\r\n"
            + "        \"title\": \"The Gruffalo\",\r\n"
            + "        \"price\": 8.99\r\n"
            + "      },\r\n"
            + "      { \"category\": \"fiction\",\r\n"
            + "        \"author\": \"J. R. R. Tolkien\",\r\n"
            + "        \"title\": \"The Lord of the Rings\",\r\n"
            + "        \"price\": 22.99\r\n"
            + "      }\r\n"
            + "    ],\r\n"
            + "    \"attributes\": {\r\n"
            + "      \"color\": \"yellow\",\r\n"
            + "      \"shape\": \"square\"\r\n"
            + "    }\r\n"
            + "  }\r\n"
            + "}");

    private static final JSONObjectHelper HELPER = new JSONObjectHelper(TEST_JSON_SAMPLE1);


    @Test
    void getBean_empty()
    {
        assertThat(HELPER.getBean().get(), is(sameInstance(TEST_JSON_SAMPLE1)));
    }

    @Test
    void getBooleanProperty_existingKey_success()
    {
        assertThat(HELPER.getBoolean("$.booleanValue"), equalTo(true));
    }

    @Test
    void getBooleanProperty_unknownKey_false()
    {
        assertThat(HELPER.getBoolean("$.unknown"), equalTo(false));
    }

    @Test
    void getIntProperty_existingKey_success()
    {
        assertThat(HELPER.getInt("$.intValue"), equalTo(9));
    }

    @Test
    void getIntProperty_unknownKey_zero()
    {
        assertThat(HELPER.getInt("$.unknown"), equalTo(0));
    }

    @Test
    void getLongProperty_existingKey_success()
    {
        assertThat(HELPER.getLong("$.longValue"), equalTo(9876543210L));
    }

    @Test
    void getLongProperty_unknownKey_zero()
    {
        assertThat(HELPER.getLong("$.unknown"), equalTo(0L));
    }

    @Test
    void getDoubleProperty_existingKey_success()
    {
        assertThat(HELPER.getDouble("$.store.books[?(@.title=='The Gruffalo')].price"), equalTo(8.99));
    }

    @Test
    void getDoubleProperty_unknownKey_zero()
    {
        assertThat(HELPER.getDouble("$.unknown"), equalTo(0.0));
    }

    @Test
    void getSringProperty_existingKey_success()
    {
        assertThat(HELPER.getString("$.store.attributes.color"), equalTo("yellow"));
    }

    @Test
    void getSringProperty_unknownKey_empty()
    {
        assertThat(HELPER.getString("$.unknown"), equalTo(""));
    }

}
