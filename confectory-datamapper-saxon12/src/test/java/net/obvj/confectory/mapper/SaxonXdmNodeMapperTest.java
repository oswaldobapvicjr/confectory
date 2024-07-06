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

import static net.obvj.junit.utils.matchers.AdvancedMatchers.throwsException;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.xml.sax.SAXParseException;

import net.obvj.confectory.ConfigurationException;
import net.obvj.confectory.SaxonProvider;
import net.obvj.confectory.internal.helper.DocumentConfigurationHelper;
import net.obvj.confectory.internal.helper.SaxonXdmNodeConfigurationHelper;
import net.sf.saxon.s9api.SaxonApiException;
import net.sf.saxon.s9api.XdmNode;
import net.sf.saxon.tree.tiny.TinyDocumentImpl;

/**
 * Unit tests for the {@link SaxonXdmNodeMapper} class.
 *
 * @author oswaldo.bapvic.jr
 * @since 2.6.0
 */
@ExtendWith(MockitoExtension.class)
class SaxonXdmNodeMapperTest
{
    private static final String TEST_XML_SAMPLE
            = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
            + "<doc>\n"
            + "    <element attrib=\"element1AttribValue\">value1</element>\n"
            + "    <element attrib=\"element2AttribValue\">value2</element>\n"
            + "</doc>\n";

    private static final String TEST_XML_INVALID = "<root></error>";

    @Mock
    private XdmNode document;

    private Mapper<XdmNode> mapper = new SaxonXdmNodeMapper();

    private ByteArrayInputStream toInputStream(String content)
    {
        return new ByteArrayInputStream(content.getBytes());
    }

    @Test
    void apply_validInputStream_validDocument() throws IOException
    {
        XdmNode result = mapper.apply(toInputStream(TEST_XML_SAMPLE));
        assertThat(mapper.configurationHelper(result).getString("//element[@attrib='element2AttribValue']"), equalTo("value2"));

    }

    @Test
    void apply_invalidJSON_configurationException()
    {
        assertThat(() -> mapper.apply(toInputStream(TEST_XML_INVALID)),
                throwsException(ConfigurationException.class).withCause(SaxonApiException.class));
    }

    @Test
    void configurationHelper_documentConfigurationHelper()
    {
        assertThat(mapper.configurationHelper(document).getClass(),
                equalTo(SaxonXdmNodeConfigurationHelper.class));
    }

}
