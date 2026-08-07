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

package net.obvj.confectory.util;

import static net.obvj.junit.utils.matchers.AdvancedMatchers.instantiationNotAllowed;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import net.obvj.confectory.mapper.DocumentMapper;
import net.obvj.confectory.source.StringSource;

/**
 * Unit tests for the {@link XMLUtils}.
 *
 * @author oswaldo.bapvic.jr
 * @since 2.5.0
 */
class XMLUtilsTest
{
    private static final String BOOKS_XML_AS_STR = "<bookstore>"
            + "<book>Everyday Italian</book>"
            + "<book>Harry Potter</book>"
            + "</bookstore>";

    private static final String BOOKS_AS_STR = "<book>Everyday Italian</book>\n"
            + "<book>Harry Potter</book>";

    private static final Document BOOKS_XML = new StringSource<Document>(BOOKS_XML_AS_STR)
            .load(new DocumentMapper());

    private static final NodeList BOOKS = BOOKS_XML.getElementsByTagName("book");

    @Test
    void constructor_instantiationNotAllowed()
    {
        assertThat(XMLUtils.class,
                instantiationNotAllowed().throwing(IllegalStateException.class));
    }

    @Test
    void toString_nodeList_allNodesAsString()
    {
        assertThat(XMLUtils.toString(BOOKS), equalTo(BOOKS_AS_STR));
    }

    @Test
    void toString_iterableOfNodes_allNodesAsString()
    {
        assertThat(XMLUtils.toString(Arrays.asList(BOOKS.item(0), BOOKS.item(1))),
                equalTo(BOOKS_AS_STR));
    }

    @Test
    void toString_textNode_textContent()
    {
        assertThat(XMLUtils.toString(BOOKS.item(0).getFirstChild()),
                equalTo("Everyday Italian"));
    }
}
