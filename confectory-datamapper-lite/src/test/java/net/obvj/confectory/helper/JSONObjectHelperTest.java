package net.obvj.confectory.helper;

import static net.obvj.junit.utils.matchers.AdvancedMatchers.throwsException;
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
    void getBooleanProperty_existingKey_properValue()
    {
        // TODO #13 result should be true
        assertThat(() -> HELPER.getBooleanProperty("$.booleanValue"), throwsException(UnsupportedOperationException.class));
    }

    @Test
    void getBooleanProperty_unknownKey_false()
    {
        // TODO #13 should be false
        assertThat(() -> HELPER.getBooleanProperty("$.unknown"), throwsException(UnsupportedOperationException.class));
    }

    @Test
    void getIntProperty_existingKey_zero()
    {
        // TODO #13 should be 9
        assertThat(() -> HELPER.getIntProperty("$.intValue"), throwsException(UnsupportedOperationException.class));
    }

    @Test
    void getIntProperty_unknownKey_zero()
    {
        // TODO #13 should be 0
        assertThat(() -> HELPER.getIntProperty("$.unknown"), throwsException(UnsupportedOperationException.class));
    }

    @Test
    void getLongProperty_existingKey_zero()
    {
        // TODO #13 should be 9876543210L
        assertThat(() -> HELPER.getLongProperty("$.longValue"), throwsException(UnsupportedOperationException.class));
    }

    @Test
    void getLongProperty_unknownKey_zero()
    {
        // TODO #13 should be 0L
        assertThat(() -> HELPER.getLongProperty("$.unknown"), throwsException(UnsupportedOperationException.class));
    }

    @Test
    void getDoubleProperty_existingKey_zero()
    {
        // TODO #13 should be 8.99
        assertThat(() -> HELPER.getDoubleProperty("$.store.books[?(@.title=='The Gruffalo')].price"),
                throwsException(UnsupportedOperationException.class));
    }

    @Test
    void getDoubleProperty_unknownKey_zero()
    {
        // TODO #13 should be 0.0
        assertThat(() -> HELPER.getDoubleProperty("$.unknown"), throwsException(UnsupportedOperationException.class));
    }

    @Test
    void getSringProperty_existingKey_empty()
    {
        // TODO #13 should be yellow
        assertThat(() -> HELPER.getStringProperty("$.store.attributes.collor"),
                throwsException(UnsupportedOperationException.class));
    }

    @Test
    void getSringProperty_unknownKey_empty()
    {
        // TODO #13 should be ""
        assertThat(() -> HELPER.getStringProperty("$.unknown"), throwsException(UnsupportedOperationException.class));
    }

}
