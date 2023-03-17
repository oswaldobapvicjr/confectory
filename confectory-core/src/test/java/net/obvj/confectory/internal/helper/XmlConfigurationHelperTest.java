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

import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.hamcrest.Matcher;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import net.obvj.confectory.ConfigurationException;

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
     * @return a new DOM Document from the specified string
     */
    private static Document parseXml(String string)
    {
        try
        {
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = builderFactory.newDocumentBuilder();
            return builder.parse(new InputSource(new StringReader(string)));
        }
        catch (ParserConfigurationException | SAXException | IOException e)
        {
            throw new AssertionError("Unable to parse test XML", e);
        }
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

}
