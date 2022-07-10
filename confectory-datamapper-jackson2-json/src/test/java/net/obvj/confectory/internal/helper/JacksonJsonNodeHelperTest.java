package net.obvj.confectory.internal.helper;

import static net.obvj.junit.utils.matchers.AdvancedMatchers.throwsException;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fasterxml.jackson.databind.JsonNode;

import net.obvj.confectory.ConfigurationBuilder;
import net.obvj.confectory.ConfigurationException;
import net.obvj.confectory.mapper.JacksonJsonNodeMapper;
import net.obvj.confectory.merger.JacksonJsonNodeConfigurationMerger;
import net.obvj.confectory.source.StringSource;

/**
 * Unit tests for the {@link JacksonJsonNodeHelper}.
 *
 * @author oswaldo.bapvic.jr (Oswaldo Junior)
 * @since 0.3.0
 */
@ExtendWith(MockitoExtension.class)
class JacksonJsonNodeHelperTest
{
    private static final String STR_TEST_JSON_SAMPLE1 = "{\r\n"
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
            + "}";

    private static final JsonNode TEST_JSON_SAMPLE1 = new ConfigurationBuilder<JsonNode>()
            .source(new StringSource<JsonNode>(STR_TEST_JSON_SAMPLE1))
            .mapper(new JacksonJsonNodeMapper())
            .build().getBean();

    private static final JacksonJsonNodeHelper HELPER = new JacksonJsonNodeHelper(TEST_JSON_SAMPLE1);


    @Test
    void getBean_validInstance()
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
        assertThat(HELPER.getBoolean("$.unknown"), equalTo(null));
    }

    @Test
    void getInteger_existingKey_success()
    {
        assertThat(HELPER.getInteger("$.intValue"), equalTo(9));
    }

    @Test
    void getInteger_unknownKey_null()
    {
        assertThat(HELPER.getInteger("$.unknown"), equalTo(null));
    }

    @Test
    void getLong_existingKey_success()
    {
        assertThat(HELPER.getLong("$.longValue"), equalTo(9876543210L));
    }

    @Test
    void getLong_unknownKey_null()
    {
        assertThat(HELPER.getLong("$.unknown"), equalTo(null));
    }

    @Test
    void getDouble_existingKeyAndSingleton_success()
    {
        assertThat(HELPER.getDouble("$.store.books[?(@.title=='The Gruffalo')].price"), equalTo(8.99));
    }

    @Test
    void getDouble_existingKeyAndMultipleElements_configurationException()
    {
        assertThat(() -> HELPER.getDouble("$.store.books[?(@.price>5)].price"),
                throwsException(ConfigurationException.class).withMessageContaining("Multiple values found for path"));
    }

    @Test
    void getDouble_unknownKey_null()
    {
        assertThat(HELPER.getDouble("$.unknown"), equalTo(null));
    }

    @Test
    void getString_existingKey_success()
    {
        assertThat(HELPER.getString("$.store.attributes.color"), equalTo("yellow"));
    }

    @Test
    void getString_unknownKey_null()
    {
        assertThat(HELPER.getString("$.unknown"), equalTo(null));
    }

    @Test
    void configurationMerger_jacksonConfigurationMerger()
    {
        assertThat(HELPER.configurationMerger().getClass(),
                equalTo(JacksonJsonNodeConfigurationMerger.class));
    }

}
