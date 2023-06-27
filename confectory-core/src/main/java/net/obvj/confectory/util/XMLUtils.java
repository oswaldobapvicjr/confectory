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

import java.io.StringWriter;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import net.obvj.confectory.ConfigurationException;

/**
 * Common methods for working with XML.
 *
 * @author oswaldo.bapvic.jr (Oswaldo Junior)
 * @since 2.5.0
 */
public class XMLUtils
{
    private static final String YES = "yes";

    private XMLUtils()
    {
        throw new IllegalStateException("Instantiation not allowed");
    }

    /**
     * Obtain a new instance of a {@link DocumentBuilderFactory} which is considered to be
     * secure against XXE (XML External Entity) attacks.
     * <p>
     * For safety, DOCTYPE declarations are completely disabled.
     *
     * @return a new, secure {@link DocumentBuilderFactory}
     * @throws ParserConfigurationException if unable to disable DOCTYPE declarations in the
     *                                      new factory
     */
    public static DocumentBuilderFactory documentBuilderFactory()
            throws ParserConfigurationException
    {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        return factory;
    }

    /**
     * Obtain a new instance of a {@link TransformerFactory} which is considered to be secure
     * against XXE (XML External Entity) attacks.
     * <p>
     * For safety, the use of all protocols by external entities is disabled.
     *
     * @return a new, secure {@link TransformerFactory}
     */
    public static TransformerFactory transformerFactory()
    {
        TransformerFactory factory = TransformerFactory.newInstance();
        factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
        factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_STYLESHEET, "");
        return factory;
    }

    /**
     * Returns a string representation of the specified XML {@link NodeList}.
     *
     * @param nodeList the {@link NodeList} to be converted
     * @return the node list as string
     * @throws ConfigurationException if unable to convert a document node into string
     */
    public static String toString(NodeList nodeList)
    {
        return IntStream.range(0, nodeList.getLength())
                .mapToObj(nodeList::item)
                .map(XMLUtils::toString)
                .collect(Collectors.joining("\n"));
    }

    /**
     * Returns a string representation of the specified XML {@link Node}.
     * <p>
     * If the XML node is either an attribute or a text node, the text content will be
     * returned. For all other node types (e.g.: document, or element), the node will be
     * transformed/encoded as string.
     *
     * @param node the {@link Node} to be converted
     * @return the node value as string
     * @throws ConfigurationException if unable to convert the document node into string
     */
    public static String toString(Node node)
    {
        switch (node.getNodeType())
        {
        case Node.ATTRIBUTE_NODE:
        case Node.TEXT_NODE:
            return node.getTextContent();
        default:
            return transformToString(node);
        }
    }

    /**
     * Transform the XML node into a String.
     *
     * @param node the {@link Node} to be transformed
     * @return the node as an XML string
     */
    private static String transformToString(Node node)
    {
        DOMSource domSource = new DOMSource(node);
        StringWriter writer = new StringWriter();
        StreamResult result = new StreamResult(writer);
        try
        {
            Transformer transformer = XMLUtils.transformerFactory().newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, YES);
            transformer.setOutputProperty(OutputKeys.INDENT, YES);
            transformer.transform(domSource, result);
            return writer.toString().trim();
        }
        catch (TransformerException exception)
        {
            throw new ConfigurationException("Unable to convert document node into string",
                    exception);
        }
    }
}
