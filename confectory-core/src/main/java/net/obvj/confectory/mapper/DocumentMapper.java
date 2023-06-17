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

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import net.obvj.confectory.ConfigurationException;
import net.obvj.confectory.internal.helper.ConfigurationHelper;
import net.obvj.confectory.internal.helper.DocumentConfigurationHelper;
import net.obvj.confectory.util.XMLUtils;

/**
 * A specialized {@code Mapper} that loads the contents of a valid XML {@code Source}
 * (e.g.: file, URL) as a W3C {@link Document}.
 * <p>
 * This allows fetching the contents using <b>XPath</b> expressions.
 * <p>
 * <b>NOTE:</b> DOCTYPE declarations (either internal or external) are disabled for
 * security reasons.
 *
 * @author oswaldo.bapvic.jr (Oswaldo Junior)
 * @since 2.4.0
 */
public class DocumentMapper implements Mapper<Document>
{

    @Override
    public Document apply(InputStream inputStream) throws IOException
    {
        try
        {
            DocumentBuilderFactory factory = XMLUtils.documentBuilderFactory();
            DocumentBuilder builder = factory.newDocumentBuilder();
            return builder.parse(inputStream);
        }
        catch (ParserConfigurationException | SAXException exception)
        {
            throw new ConfigurationException(exception);
        }
    }

    @Override
    public ConfigurationHelper<Document> configurationHelper(Document document)
    {
        return new DocumentConfigurationHelper(document);
    }

}
