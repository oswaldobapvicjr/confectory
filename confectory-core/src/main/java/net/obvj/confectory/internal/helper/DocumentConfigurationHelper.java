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

import java.util.Objects;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import net.obvj.confectory.ConfigurationException;
import net.obvj.confectory.merger.ConfigurationMerger;
import net.obvj.confectory.util.ParseException;
import net.obvj.confectory.util.TypeFactory;
import net.obvj.confectory.util.XMLUtils;

/**
 * A generic Configuration Helper that retrieves data from an XML {@link Document} using
 * XPath.
 *
 * @author oswaldo.bapvic.jr (Oswaldo Junior)
 * @since 2.4.0
 */
public class DocumentConfigurationHelper extends AbstractConfigurationHelper<Document> implements ConfigurationHelper<Document>
{

    protected final Document document;

    /**
     * Creates a new helper for the given XML {@link Document}.
     *
     * @param document the JSON document to set
     */
    public DocumentConfigurationHelper(Document document)
    {
        this.document = document;
    }

    /**
     * @return the XML {@link Document} in context
     */
    @Override
    public Document getBean()
    {
        return document;
    }

    /**
     * @return the XML {@link Document} in context, transformed/encoded as string
     * @since 2.5.0
     */
    @Override
    public String getAsString()
    {
        return XMLUtils.toString(document);
    }

    /**
     * Returns the value associated with the specified {@code XPath} expression in the XML
     * document in context, provided that the expression returns a single element that can be
     * mapped to the specified class type.
     * <p>
     * If no value is found for the given expression and the {@code mandatory} flag is
     * {@code true}, an exception will be thrown; if the flag is not set, then the method
     * returns {@code null}.
     *
     * @param xpath      the {@code XPath} expression to evaluate
     * @param targetType the type the evaluation result should be converted to
     * @param mandatory  a flag determining whether or not an exception should be raised in
     *                   case the expression returns no data
     *
     * @return the object that is the result of evaluating the given {@code XPath} expression
     *         and converting the result to the specified {@code targetType}; it may be
     *         {@code null} if no value is found and the {@code mandatory} flag is set
     *
     * @throws NullPointerException   if the {@code XPath} expression is null
     * @throws ConfigurationException if the {@code XPath} expression is either invalid, not
     *                                found (with the {@code mandatory} flag set), or it
     *                                evaluates to more than a single element
     */
    @Override
    protected <T> T getValue(String xpath, Class<T> targetType, boolean mandatory)
    {
        NodeList result = get(xpath).getNodeList();
        switch (result.getLength())
        {
        case 0:
            if (mandatory)
            {
                throw new ConfigurationException("No value found for path: %s", xpath);
            }
            return null;
        case 1:
            try
            {
                Node node = result.item(0);
                return TypeFactory.parse(targetType, node.getTextContent());
            }
            catch (ParseException parseException)
            {
                throw new ConfigurationException(parseException,
                        "The path %s was found but the object can not be converted into %s",
                        xpath, targetType);
            }
        default:
            throw new ConfigurationException("Multiple values found for path: %s", xpath);
        }
    }

    /**
     * Returns the {@link NodeList} object associated with the specified @{code XPath} in the
     * XML document in context.
     *
     * @param xpath the {@code XPath} expression to read
     *
     * @return the {@link NodeList} object associated with the specified {@code XPath}
     *
     * @throws NullPointerException   if the {@code XPath} expression is null
     * @throws ConfigurationException if the {@code XPath} expression is not valid
     */
    @Override
    public NodeListHolder get(String xpath)
    {
        try
        {
            NodeList nodeList = (NodeList) compileXPath(xpath).evaluate(document, XPathConstants.NODESET);
            return new NodeListHolder(nodeList);
        }
        catch (XPathExpressionException exception)
        {
            throw new ConfigurationException(exception);
        }
    }

    /**
     * Compiles the given XPath expression.
     *
     * @param expression the XPath expression to be compiled
     * @return an {@code XPathExpression} object that can be used for further evaluation
     * @throws XPathExpressionException if the expression cannot be compiled
     */
    public static XPathExpression compileXPath(String expression) throws XPathExpressionException
    {
        return XPathFactory.newInstance().newXPath().compile(expression);
    }

    @Override
    public ConfigurationMerger<Document> configurationMerger()
    {
        throw new UnsupportedOperationException("Merge not supported for XML");
    }

    /**
     * This holds a {@link NodeList} and provides a better way to display it as string.
     *
     * @since 2.5.0
     */
    static class NodeListHolder
    {
        private final NodeList nodeList;

        NodeListHolder(final NodeList nodeList)
        {
            this.nodeList = Objects.requireNonNull(nodeList, "The node list is null");
        }

        /**
         * @return the XML node list
         */
        public NodeList getNodeList()
        {
            return nodeList;
        }

        @Override
        public String toString()
        {
            return XMLUtils.toString(nodeList);
        }

    }

}
