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

import javax.xml.xpath.XPathEvaluationResult;
import javax.xml.xpath.XPathEvaluationResult.XPathResultType;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import javax.xml.xpath.XPathNodes;

import org.w3c.dom.Document;

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
     * @param document the XML document to set
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
     * <p>
     * <strong>Note:</strong> An expression that does not return a node set, such as
     * {@code number(...)}, {@code count(...)}, {@code string(...)} or {@code boolean(...)},
     * always produces a single value, so the {@code mandatory} flag has no effect on it. For
     * example, {@code count(/unknown)} evaluates to zero and {@code boolean(/unknown)}
     * evaluates to {@code false}.
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
        XPathEvaluationResult<?> result = evaluate(xpath, XPathEvaluationResult.class);
        if (result.type() != XPathResultType.NODESET)
        {
            // Expressions such as number(...) or count(...) produce a single, atomic value:
            // let the engine apply the XPath string conversion rules on it
            return parse(xpath, targetType, evaluate(xpath, String.class));
        }
        XPathNodes nodes = (XPathNodes) result.value();
        switch (nodes.size())
        {
        case 0:
            if (mandatory)
            {
                throw new ConfigurationException("No value found for path: %s", xpath);
            }
            return null;
        case 1:
            return parse(xpath, targetType, nodes.iterator().next().getTextContent());
        default:
            throw new ConfigurationException("Multiple values found for path: %s", xpath);
        }
    }

    /**
     * Returns the object associated with the specified {@code XPath} expression in the XML
     * document in context.
     * <p>
     * <b>Note:</b> The actual return type may vary depending on the expression: one that
     * selects nodes produces an object holding those nodes, whereas the expressions
     * {@code number(...)}, {@code string(...)} and {@code boolean(...)} produce a
     * {@link Double}, a {@link String}, and a {@link Boolean}, respectively.
     *
     * @param xpath the {@code XPath} expression to read
     *
     * @return the object associated with the specified {@code XPath}
     *
     * @throws NullPointerException   if the {@code XPath} expression is null
     * @throws ConfigurationException if the {@code XPath} expression is not valid
     */
    @Override
    public Object get(String xpath)
    {
        XPathEvaluationResult<?> result = evaluate(xpath, XPathEvaluationResult.class);
        return result.type() == XPathResultType.NODESET
                ? new NodeListHolder((XPathNodes) result.value())
                : result.value();
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
     * Evaluates the specified {@code XPath} expression on the XML document in context.
     *
     * @param xpath      the {@code XPath} expression to evaluate
     * @param resultType one of the types accepted by
     *                   {@link XPathExpression#evaluateExpression(Object, Class)}; in
     *                   particular, {@code XPathEvaluationResult.class} preserves the result
     *                   type defined by the expression itself
     *
     * @return the evaluation result, converted to the specified {@code resultType}
     * @throws ConfigurationException if the {@code XPath} expression is not valid
     */
    private <T> T evaluate(String xpath, Class<T> resultType)
    {
        try
        {
            return compileXPath(xpath).evaluateExpression(document, resultType);
        }
        catch (XPathExpressionException exception)
        {
            throw new ConfigurationException(exception);
        }
    }

    /**
     * Converts the specified value into the specified type.
     *
     * @param xpath      the {@code XPath} expression that produced the value, for reporting
     *                   purposes
     * @param targetType the type the value should be converted to
     * @param value      the value to be converted
     *
     * @return the value converted into the specified {@code targetType}
     * @throws ConfigurationException if the value can not be converted
     */
    private <T> T parse(String xpath, Class<T> targetType, String value)
    {
        try
        {
            return TypeFactory.parse(targetType, value);
        }
        catch (ParseException parseException)
        {
            throw new ConfigurationException(parseException,
                    "The path %s was found but the object can not be converted into %s",
                    xpath, targetType);
        }
    }

    /**
     * This holds the nodes selected by an {@code XPath} expression and provides a better way
     * to display them as string.
     *
     * @since 2.5.0
     */
    static class NodeListHolder
    {
        private final XPathNodes nodes;

        NodeListHolder(final XPathNodes nodes)
        {
            this.nodes = Objects.requireNonNull(nodes, "The node list is null");
        }

        @Override
        public String toString()
        {
            return XMLUtils.toString(nodes);
        }

    }

}
