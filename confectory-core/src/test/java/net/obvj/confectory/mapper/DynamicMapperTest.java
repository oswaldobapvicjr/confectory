/*
 * Copyright 2024 obvj.net
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

package net.obvj.confectory.mapper;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.junit.jupiter.api.Test;

import net.obvj.junit.utils.matchers.AdvancedMatchers;

/**
 * Unit tests for the {@link DynamicMapper}.
 *
 * @author oswaldo.bapvic.jr
 * @since 2.6.0
 */
class DynamicMapperTest
{
    static final String TEST_INI_CONTENT = "[web]\nhost=localhost\nport=1910";
    static final String TEST_PROPERTIES_CONTENT = "web.host=localhost\nweb.port=1910";
    static final String TEST_JSON_CONTENT = "{\"web\":{\"host\":\"localhost\",\"port\":1910}}";
    static final String TEST_TXT_CONTENT = "localhost:1910";
    static final String TEST_XML_CONTENT = "<web><host>localhost</host><port>1910</port></web>";

    private static ByteArrayInputStream asInputStream(String string)
    {
        return new ByteArrayInputStream(string.getBytes());
    }

    @Test
    void apply_ini_loadedSuccessfully() throws IOException
    {
        Mapper<Object> mapper = new DynamicMapper("INI");
        Object bean = mapper.apply(asInputStream(TEST_INI_CONTENT));
        assertThat(mapper.configurationHelper(bean).getString("web.host"), equalTo("localhost"));
    }

    @Test
    void apply_json_loadedSuccessfully() throws IOException
    {
        Mapper<Object> mapper = new DynamicMapper("JSON");
        Object bean = mapper.apply(asInputStream(TEST_JSON_CONTENT));
        assertThat(mapper.configurationHelper(bean).getString("$.web.host"), equalTo("localhost"));
    }

    @Test
    void apply_properties_loadedSuccessfully() throws IOException
    {
        Mapper<Object> mapper = new DynamicMapper("PROPERTIES");
        Object bean = mapper.apply(asInputStream(TEST_PROPERTIES_CONTENT));
        assertThat(mapper.configurationHelper(bean).getInteger("web.port"), equalTo(1910));
    }

    @Test
    void apply_txt_loadedSuccessfully() throws IOException
    {
        Mapper<Object> mapper = new DynamicMapper("TXT");
        Object bean = mapper.apply(asInputStream(TEST_TXT_CONTENT));
        assertThat(mapper.configurationHelper(bean).getAsString(), equalTo("localhost:1910"));
    }

    @Test
    void apply_xml_loadedSuccessfully() throws IOException
    {
        Mapper<Object> mapper = new DynamicMapper("XML");
        Object bean = mapper.apply(asInputStream(TEST_XML_CONTENT));
        assertThat(mapper.configurationHelper(bean).getInteger("/web/port"), equalTo(1910));
    }

    @Test
    void constructor_unknown_illegalArgument()
    {
        assertThat(() -> new DynamicMapper("unknown"),
                AdvancedMatchers.throwsException(IllegalArgumentException.class)
                        .withMessage("No default mapper available for the extension: \"unknown\""));
    }

}
