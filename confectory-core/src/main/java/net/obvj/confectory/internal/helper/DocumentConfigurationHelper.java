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

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import net.obvj.confectory.ConfigurationException;
import net.obvj.confectory.merger.ConfigurationMerger;
import net.obvj.confectory.util.ParseFactory;

/**
 * A generic Configuration Helper that retrieves data from an XML {@link Document} using
 * XPath.
 *
 * @author oswaldo.bapvic.jr (Oswaldo Junior)
 * @since 2.4.0
 */
public class DocumentConfigurationHelper implements ConfigurationHelper<Document>
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
     * Returns the {@code Boolean} object associated with the specified {@code XPath}
     * expression in the XML document in context, provided that the expression returns a
     * single element that can be converted to {@code boolean}.
     * <p>
     * <strong>Note:</strong> If the evaluation result is a valid string it will parsed
     * according to {@link Boolean#parseBoolean(String)}, it will return {@code false} for any
     * string different than {@code "true"} (ignoring case).
     *
     * @param xpath the {@code XPath} expression to evaluate
     * @return the {@code Boolean} object from the evaluation result of the specified
     *         {@code XPath} expression; {@code null} if the expression is not found
     *
     * @throws NullPointerException   if the {@code XPath} expression is null
     * @throws ConfigurationException if the {@code XPath} expression is either invalid, or it
     *                                evaluates to more than a single element
     */
    @Override
    public Boolean getBoolean(String xpath)
    {
        return getValue(xpath, Boolean.class, false);
    }

    /**
     * Returns the {@code Boolean} object associated with the specified {@code XPath}
     * expression in the XML document in context, provided that the expression returns a
     * single element that can be converted to {@code boolean}.
     * <p>
     * If no value is found for the given expression, then an exception will be thrown.
     * <p>
     * <strong>Note:</strong> If the evaluation result is a valid string it will parsed
     * according to {@link Boolean#parseBoolean(String)}, it will return {@code false} for any
     * string different than {@code "true"} (ignoring case).
     *
     * @param xpath the {@code XPath} expression to evaluate
     * @return the {@code Boolean} object from the evaluation result of the the specified
     *         {@code XPath} expression; never {@code null}
     *
     * @throws NullPointerException   if the {@code XPath} expression is null
     * @throws ConfigurationException if the {@code XPath} expression is either invalid, not
     *                                found, or it evaluates to more than a single element
     */
    @Override
    public Boolean getMandatoryBoolean(String xpath)
    {
        return getValue(xpath, Boolean.class);
    }

    /**
     * Returns the {@code Integer} object associated with the specified {@code XPath}
     * expression in the XML document in context, provided that the expression returns a
     * single element that can be converted to {@code int}.
     *
     * @param xpath the {@code XPath} expression to evaluate
     * @return the {@code Integer} object from the evaluation result of the specified
     *         {@code XPath} expression; {@code null} if the expression is not found
     *
     * @throws NullPointerException   if the {@code XPath} expression is null
     * @throws ConfigurationException if the {@code XPath} expression is either invalid, or it
     *                                evaluates to more than a single element
     * @throws NumberFormatException  if the {@code XPath} result cannot be assigned to
     *                                {@code int}
     */
    @Override
    public Integer getInteger(String xpath)
    {
        return getValue(xpath, Integer.class, false);
    }

    /**
     * Returns the {@code Integer} object associated with the specified {@code XPath}
     * expression in the XML document in context, provided that the expression returns a
     * single element that can be converted to {@code int}.
     * <p>
     * If no value is found for the given expression, then an exception will be thrown.
     *
     * @param xpath the {@code XPath} expression to evaluate
     * @return the {@code Integer} object from the evaluation result of the the specified
     *         {@code XPath} expression; never {@code null}
     *
     * @throws NullPointerException   if the {@code XPath} expression is null
     * @throws ConfigurationException if the {@code XPath} expression is either invalid, not
     *                                found, or it evaluates to more than a single element
     * @throws NumberFormatException  if the {@code XPath} result cannot be assigned to
     *                                {@code int}
     */
    @Override
    public Integer getMandatoryInteger(String xpath)
    {
        return getValue(xpath, Integer.class);
    }

    /**
     * Returns the {@code Long} object associated with the specified {@code XPath} expression
     * in the XML document in context, provided that the expression returns a single element
     * that can be converted to {@code long}.
     *
     * @param xpath the {@code XPath} expression to evaluate
     * @return the {@code Long} object from the evaluation result of the specified
     *         {@code XPath} expression; {@code null} if the expression is not found
     *
     * @throws NullPointerException   if the {@code XPath} expression is null
     * @throws ConfigurationException if the {@code XPath} expression is either invalid, or it
     *                                evaluates to more than a single element
     * @throws NumberFormatException  if the {@code XPath} result cannot be assigned to
     *                                {@code long}
     */
    @Override
    public Long getLong(String xpath)
    {
        return getValue(xpath, Long.class, false);
    }

    /**
     * Returns the {@code Long} object associated with the specified {@code XPath} expression
     * in the XML document in context, provided that the expression returns a single element
     * that can be converted to {@code long}.
     * <p>
     * If no value is found for the given expression, then an exception will be thrown.
     *
     * @param xpath the {@code XPath} expression to evaluate
     * @return the {@code Long} object from the evaluation result of the the specified
     *         {@code XPath} expression; never {@code null}
     *
     * @throws NullPointerException   if the {@code XPath} expression is null
     * @throws ConfigurationException if the {@code XPath} expression is either invalid, not
     *                                found, or it evaluates to more than a single element
     * @throws NumberFormatException  if the {@code XPath} result cannot be assigned to
     *                                {@code long}
     */
    @Override
    public Long getMandatoryLong(String xpath)
    {
        return getValue(xpath, Long.class);
    }

    /**
     * Returns the {@code Double} object associated with the specified {@code XPath}
     * expression in the XML document in context, provided that the expression returns a
     * single element that can be converted to {@code double}.
     *
     * @param xpath the {@code XPath} expression to evaluate
     * @return the {@code Double} object from the evaluation result of the specified
     *         {@code XPath} expression; {@code null} if the expression is not found
     *
     * @throws NullPointerException   if the {@code XPath} expression is null
     * @throws ConfigurationException if the {@code XPath} expression is either invalid, or it
     *                                evaluates to more than a single element
     * @throws NumberFormatException  if the {@code XPath} result cannot be assigned to
     *                                {@code double}
     */
    @Override
    public Double getDouble(String xpath)
    {
        return getValue(xpath, Double.class, false);
    }

    /**
     * Returns the {@code Double} object associated with the specified {@code XPath}
     * expression in the XML document in context, provided that the expression returns a
     * single element that can be converted to {@code double}.
     * <p>
     * If no value is found for the given expression, then an exception will be thrown.
     *
     * @param xpath the {@code XPath} expression to evaluate
     * @return the {@code Double} object from the evaluation result of the the specified
     *         {@code XPath} expression; never {@code null}
     *
     * @throws NullPointerException   if the {@code XPath} expression is null
     * @throws ConfigurationException if the {@code XPath} expression is either invalid, not
     *                                found, or it evaluates to more than a single element
     * @throws NumberFormatException  if the {@code XPath} result cannot be assigned to
     *                                {@code double}
     */
    @Override
    public Double getMandatoryDouble(String xpath)
    {
        return getValue(xpath, Double.class);
    }

    /**
     * Returns the {@code String} object associated with the specified {@code XPath}
     * expression in the XML document in context, provided that the expression returns a
     * single element.
     *
     * @param xpath the {@code XPath} expression to evaluate
     * @return the {@code String} value associated with the specified {@code XPath}
     *         expression; {@code null} if the expression is not found
     *
     * @throws NullPointerException   if the {@code XPath} expression is null
     * @throws ConfigurationException if the {@code XPath} expression is either invalid, or it
     *                                evaluates to more than a single element
     */
    @Override
    public String getString(String xpath)
    {
        return getValue(xpath, String.class, false);
    }

    /**
     * Returns the {@code String} object associated with the specified {@code XPath}
     * expression in the XML document in context, provided that the expression returns a
     * single element.
     * <p>
     * If no value is found for the given expression, then an exception will be thrown.
     *
     * @param xpath the {@code XPath} expression to evaluate
     * @return the {@code String} value associated with the specified {@code XPath}
     *         expression; never {@code null}
     *
     * @throws NullPointerException   if the {@code XPath} expression is null
     * @throws ConfigurationException if the {@code XPath} expression is either invalid, not
     *                                found, or it evaluates to more than a single element
     */
    @Override
    public String getMandatoryString(String xpath)
    {
        return getValue(xpath, String.class);
    }

    /**
     * Returns the value associated with the specified {@code XPath} expression in the XML
     * document in context, provided that the expression returns a single element that can be
     * mapped to the specified class type.
     * <p>
     * If no value is found for the given expression, then an exception will be thrown.
     *
     * @param xpath      the {@code XPath} expression to evaluate
     * @param targetType the type the evaluation result should be converted to
     *
     * @return the object that is the result of evaluating the given {@code XPath} expression
     *         and converting the result to the specified {@code targetType}; never null
     *
     * @throws NullPointerException   if the {@code XPath} expression is null
     * @throws ConfigurationException if the {@code XPath} expression is either invalid, not
     *                                found, or it evaluates to more than a single element
     */
    protected <T> T getValue(String xpath, Class<T> targetType)
    {
        return getValue(xpath, targetType, true);
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
    protected <T> T getValue(String xpath, Class<T> targetType, boolean mandatory)
    {
        NodeList result = get(xpath);
        switch (result.getLength())
        {
        case 0:
            if (mandatory)
            {
                throw new ConfigurationException("No value found for path: %s", xpath);
            }
            return null;
        case 1:
            Node node = result.item(0);
            return ParseFactory.parse(targetType, node.getTextContent());
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
    public NodeList get(String xpath)
    {
        try
        {
            return (NodeList) compileXPath(xpath).evaluate(document, XPathConstants.NODESET);
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

}
