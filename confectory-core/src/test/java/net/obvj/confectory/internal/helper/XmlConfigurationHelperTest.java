/*
 * Copyright 2023 obvj.net
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
import static org.hamcrest.MatcherAssert.assertThat;

import javax.xml.xpath.XPathExpressionException;

import org.hamcrest.Matcher;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;

import net.obvj.confectory.ConfigurationException;
import net.obvj.confectory.mapper.XmlMapper;
import net.obvj.confectory.source.StringSource;

/**
 * Unit tests for the {@link XmlConfigurationHelper}.
 *
 * @author oswaldo.bapvic.jr (Oswaldo Junior)
 * @since 2.4.0
 */
class XmlConfigurationHelperTest
{
    private static final Document BOOKS_XML = parseXml(
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
            + "<bookstore>\n"
            + "    <book category=\"cooking\">\n"
            + "        <title lang=\"en\">Everyday Italian</title>\n"
            + "        <author>Giada De Laurentiis</author>\n"
            + "        <year>2005</year>\n"
            + "        <price>30.00</price>\n"
            + "        <active>True</active>\n"
            + "        <isbn>9876543210999</isbn>\n"
            + "    </book>\n"
            + "    <book category=\"children\">\n"
            + "        <title lang=\"en\">Harry Potter</title>\n"
            + "        <author>J K. Rowling</author>\n"
            + "        <year>2005</year>\n"
            + "        <price>29.99</price>\n"
            + "    </book>\n"
            + "    <book category=\"web\">\n"
            + "        <title lang=\"en\">XQuery Kick Start</title>\n"
            + "        <author>James McGovern</author>\n"
            + "        <author>Per Bothner</author>\n"
            + "        <author>Kurt Cagle</author>\n"
            + "        <author>James Linn</author>\n"
            + "        <author>Vaidyanathan Nagarajan</author>\n"
            + "        <year>2003</year>\n"
            + "        <price>49.99</price>\n"
            + "    </book>\n"
            + "    <book category=\"web\">\n"
            + "        <title lang=\"en\">Learning XML</title>\n"
            + "        <author>Erik T. Ray</author>\n"
            + "        <year>2003</year>\n"
            + "        <price>39.95</price>\n"
            + "    </book>\n"
            + "</bookstore>");

    private static final ConfigurationHelper<Document> HELPER = new XmlConfigurationHelper(BOOKS_XML);

    private static final String PATH_UNKNOWN = "/unknown";
    private static final Matcher<Runnable> EXCEPTION_NO_VALUE_FOUND_PATH_UNKNOWN = throwsException(
            ConfigurationException.class).withMessageContaining("No value found", PATH_UNKNOWN);

    /**
     * Parse the specified string into an XML {@link Document}
     *
     * @param string the string to parse
     * @return a new {@link Document} from the specified string
     */
    private static Document parseXml(String string)
    {
        return new StringSource<Document>(string).load(new XmlMapper());
    }

    @Test
    void getBean_validDocument()
    {
        assertThat(HELPER.getBean(), equalTo(BOOKS_XML));
    }

    @Test
    void configurationMerger_unsupportedOperationException()
    {
        assertThat(() -> HELPER.configurationMerger(),
                throwsException(UnsupportedOperationException.class)
                        .withMessage("Merge not supported for XML"));
    }

    @Test
    void getString_invalidXPath_exception()
    {
        assertThat(() -> HELPER.getString("?"),
                throwsException(ConfigurationException.class)
                        .withCause(XPathExpressionException.class));
    }

    @Test
    void getString_singleElement_success()
    {
        assertThat(HELPER.getString("/bookstore/book[@category='cooking']/title/text()"),
                equalTo("Everyday Italian"));
    }

    @Test
    void getString_pathNotFound_null()
    {
        assertThat(HELPER.getString(PATH_UNKNOWN), equalTo(null));
    }

    @Test
    void getString_multipleElements_configurationException()
    {
        assertThat(() -> HELPER.getString("/bookstore/book[@category='web']/title/text()"),
                throwsException(ConfigurationException.class)
                        .withMessageContaining("Multiple values found"));
    }

    @Test
    void getMandatoryString_singleElement_success()
    {
        assertThat(HELPER.getMandatoryString("/bookstore/book[@category='cooking']/author/text()"),
                equalTo("Giada De Laurentiis"));
    }

    @Test
    void getMandatoryString_pathNotFound_exception()
    {
        assertThat(() -> HELPER.getMandatoryString(PATH_UNKNOWN),
                EXCEPTION_NO_VALUE_FOUND_PATH_UNKNOWN);
    }

    @Test
    void getDouble_singleElement_success()
    {
        assertThat(HELPER.getDouble("/bookstore/book[title='Harry Potter']/price"),
                equalTo(29.99));
    }

    @Test
    void getDouble_pathNotFound_null()
    {
        assertThat(HELPER.getDouble(PATH_UNKNOWN), equalTo(null));
    }

    @Test
    void getDouble_unparsableDouble_exception()
    {
        assertThat(() -> HELPER.getDouble("/bookstore/book[1]/title"),
                throwsException(NumberFormatException.class)
                        .withMessageContaining("For input string"));
    }

    @Test
    void getMandatoryDouble_singleElement_success()
    {
        assertThat(HELPER.getMandatoryDouble("/bookstore/book[author='James Linn']/price"),
                equalTo(49.99));
    }

    @Test
    void getMandatoryDouble_pathNotFound_exception()
    {
        assertThat(() -> HELPER.getMandatoryDouble(PATH_UNKNOWN),
                EXCEPTION_NO_VALUE_FOUND_PATH_UNKNOWN);
    }

    @Test
    void getInteger_pathNotFound_null()
    {
        assertThat(HELPER.getInteger(PATH_UNKNOWN), equalTo(null));
    }

    @Test
    void getInteger_unparsableInteger_exception()
    {
        assertThat(() -> HELPER.getInteger("/bookstore/book[1]/price"),
                throwsException(NumberFormatException.class)
                        .withMessageContaining("For input string"));
    }

    @Test
    void getInteger_singleElement_success()
    {
        assertThat(HELPER.getInteger("/bookstore/book[author='James Linn']/year"),
                equalTo(2003));
    }

    @Test
    void getMandatoryInteger_singleElement_success()
    {
        assertThat(HELPER.getMandatoryInteger("/bookstore/book[author='James Linn']/year"),
                equalTo(2003));
    }

    @Test
    void getMandatoryInteger_pathNotFound_exception()
    {
        assertThat(() -> HELPER.getMandatoryInteger(PATH_UNKNOWN),
                EXCEPTION_NO_VALUE_FOUND_PATH_UNKNOWN);
    }

    @Test
    void getLong_pathNotFound_null()
    {
        assertThat(HELPER.getLong(PATH_UNKNOWN), equalTo(null));
    }

    @Test
    void getLong_unparsableLong_exception()
    {
        assertThat(() -> HELPER.getLong("/bookstore/book[2]/price"),
                throwsException(NumberFormatException.class)
                        .withMessageContaining("For input string"));
    }

    @Test
    void getLong_singleElement_success()
    {
        assertThat(HELPER.getLong("/bookstore/book[1]/isbn"),
                equalTo(9876543210999L));
    }

    @Test
    void getMandatoryLong_singleElement_success()
    {
        assertThat(HELPER.getMandatoryLong("/bookstore/book[1]/isbn"),
                equalTo(9876543210999L));
    }

    @Test
    void getMandatoryLong_pathNotFound_exception()
    {
        assertThat(() -> HELPER.getMandatoryLong(PATH_UNKNOWN),
                EXCEPTION_NO_VALUE_FOUND_PATH_UNKNOWN);
    }

    @Test
    void getBoolean_pathNotFound_null()
    {
        assertThat(HELPER.getBoolean(PATH_UNKNOWN), equalTo(null));
    }

    @Test
    void getBoolean_unparsableBoolean_false()
    {
        assertThat(HELPER.getBoolean("/bookstore/book[2]/price"), equalTo(false));
    }

    @Test
    void getBoolean_singleElement_success()
    {
        assertThat(HELPER.getBoolean("/bookstore/book[1]/active"), equalTo(true));
    }

    @Test
    void getMandatoryBoolean_singleElement_success()
    {
        assertThat(HELPER.getMandatoryBoolean("/bookstore/book[1]/active"), equalTo(true));
    }

    @Test
    void getMandatoryBoolean_pathNotFound_exception()
    {
        assertThat(() -> HELPER.getMandatoryBoolean(PATH_UNKNOWN),
                EXCEPTION_NO_VALUE_FOUND_PATH_UNKNOWN);
    }

}
