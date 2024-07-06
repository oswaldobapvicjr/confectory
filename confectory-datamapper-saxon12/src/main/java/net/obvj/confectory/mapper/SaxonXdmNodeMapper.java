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

import java.io.IOException;
import java.io.InputStream;

import javax.xml.transform.stream.StreamSource;

import net.obvj.confectory.ConfigurationException;
import net.obvj.confectory.SaxonProvider;
import net.obvj.confectory.internal.helper.ConfigurationHelper;
import net.obvj.confectory.internal.helper.SaxonXdmNodeConfigurationHelper;
import net.sf.saxon.s9api.DocumentBuilder;
import net.sf.saxon.s9api.SaxonApiException;
import net.sf.saxon.s9api.XdmNode;

/**
 * A specialized {@code Mapper} that loads the contents of a valid XML {@code Source}
 * (e.g.: file, URL) as a {@link XdmNode} ({@code Saxon-HE} 12 abstraction for a XML node).
 * <p>
 * This allows fetching the contents using <b>XPath</b> expressions.
 *
 * @author oswaldo.bapvic.jr (Oswaldo Junior)
 * @since 2.6.0
 */
public class SaxonXdmNodeMapper implements Mapper<XdmNode>
{

    @Override
    public XdmNode apply(InputStream inputStream) throws IOException
    {
        DocumentBuilder builder = SaxonProvider.getProcessor().newDocumentBuilder();
        try
        {
            return builder.build(new StreamSource(inputStream));
        }
        catch (SaxonApiException exception)
        {
            throw new ConfigurationException(exception);
        }
    }

    @Override
    public ConfigurationHelper<XdmNode> configurationHelper(XdmNode node)
    {
        return new SaxonXdmNodeConfigurationHelper(node);
    }

}
