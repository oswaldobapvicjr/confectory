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

package net.obvj.confectory.mapper;

import static net.obvj.junit.utils.matchers.AdvancedMatchers.throwsException;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.w3c.dom.Document;
import org.xml.sax.SAXParseException;

import net.obvj.confectory.ConfigurationException;
import net.obvj.confectory.internal.helper.DocumentConfigurationHelper;

/**
 * Unit tests for the {@link DocumentMapper} class.
 *
 * @author oswaldo.bapvic.jr
 * @since 2.4.0
 */
@ExtendWith(MockitoExtension.class)
class DocumentMapperTest
{
    private static final String TEST_XML_SAMPLE
            = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
            + "<root>\n"
            + "    <element1>value1</element1>\n"
            + "    <element2 attrib=\"element2AttribValue\"/>\n"
            + "</root>\n";

    private static final String TEST_XML_INVALID = "<root></error>";

    @Mock
    private Document document;

    private Mapper<Document> mapper = new DocumentMapper();

    private ByteArrayInputStream toInputStream(String content)
    {
        return new ByteArrayInputStream(content.getBytes());
    }

    @Test
    void apply_validInputStream_validDocument() throws IOException
    {
        Document result = mapper.apply(toInputStream(TEST_XML_SAMPLE));
        assertThat(result.getDocumentElement().getElementsByTagName("element1").item(0)
                .getTextContent(), equalTo("value1"));
        assertThat(result.getDocumentElement().getElementsByTagName("element2").item(0)
                .getAttributes().getNamedItem("attrib").getNodeValue(),
                equalTo("element2AttribValue"));
    }

    @Test
    void apply_invalidJSON_configurationException() throws IOException
    {
        assertThat(() -> mapper.apply(toInputStream(TEST_XML_INVALID)),
                throwsException(ConfigurationException.class).withCause(SAXParseException.class));
    }

    @Test
    void configurationHelper_documentConfigurationHelper()
    {
        assertThat(mapper.configurationHelper(document).getClass(),
                equalTo(DocumentConfigurationHelper.class));
    }

}
