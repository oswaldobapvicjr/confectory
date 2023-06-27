/*
 * Copyright 2021 obvj.net
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

package net.obvj.confectory.internal.helper;

import static net.obvj.junit.utils.matchers.AdvancedMatchers.throwsException;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.google.gson.JsonObject;

import net.obvj.confectory.ConfigurationBuilder;
import net.obvj.confectory.ConfigurationException;
import net.obvj.confectory.mapper.GsonJsonObjectMapper;
import net.obvj.confectory.merger.GsonJsonObjectConfigurationMerger;
import net.obvj.confectory.source.StringSource;

/**
 * Unit tests for the {@link GsonJsonObjectHelper}.
 *
 * @author oswaldo.bapvic.jr (Oswaldo Junior)
 * @since 0.4.0
 */
@ExtendWith(MockitoExtension.class)
class GsonJsonObjectHelperTest
{
    private static final String STR_TEST_JSON_SAMPLE1 = "{\n"
            + "  \"intValue\": 9,\n"
            + "  \"longValue\": 9876543210,\n"
            + "  \"booleanValue\": true,\n"
            + "  \"store\": {\n"
            + "    \"books\": [ \n"
            + "      { \"category\": \"children\",\n"
            + "        \"author\": \"Julia Donaldson\",\n"
            + "        \"title\": \"The Gruffalo\",\n"
            + "        \"price\": 8.99\n"
            + "      },\n"
            + "      { \"category\": \"fiction\",\n"
            + "        \"author\": \"J. R. R. Tolkien\",\n"
            + "        \"title\": \"The Lord of the Rings\",\n"
            + "        \"price\": 22.99\n"
            + "      }\n"
            + "    ],\n"
            + "    \"attributes\": {\n"
            + "      \"color\": \"yellow\",\n"
            + "      \"shape\": \"square\"\n"
            + "    }\n"
            + "  }\n"
            + "}";

    private static final JsonObject TEST_JSON_SAMPLE1 = new ConfigurationBuilder<JsonObject>()
            .source(new StringSource<JsonObject>(STR_TEST_JSON_SAMPLE1))
            .mapper(new GsonJsonObjectMapper())
            .build().getBean();

    private static final GsonJsonObjectHelper HELPER = new GsonJsonObjectHelper(TEST_JSON_SAMPLE1);


    @Test
    void getBean_sameInstance()
    {
        assertThat(HELPER.getBean(), is(sameInstance(TEST_JSON_SAMPLE1)));
    }

    private static String trimmed(String string)
    {
        return string.replaceAll("\\n| ", "");
    }

    @Test
    void getAsString_success()
    {
        assertThat(trimmed(HELPER.getAsString()), is(equalTo(trimmed(STR_TEST_JSON_SAMPLE1))));
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
                throwsException(ConfigurationException.class).withMessageContaining("Multiple values found"));
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
    void configurationMerger_gsonConfigurationMerger()
    {
        assertThat(HELPER.configurationMerger().getClass(), equalTo(GsonJsonObjectConfigurationMerger.class));
    }
}
