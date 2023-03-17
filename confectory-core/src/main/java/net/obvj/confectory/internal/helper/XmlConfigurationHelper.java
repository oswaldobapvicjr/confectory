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

import com.jayway.jsonpath.InvalidPathException;

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
public class XmlConfigurationHelper implements ConfigurationHelper<Document>
{
    protected final Document document;

    /**
     * Creates a new helper for the given XML {@link Document}.
     *
     * @param document the JSON document to set
     */
    protected XmlConfigurationHelper(Document document)
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
     * Returns the {@code Boolean} object associated with the specified {@code jsonPath} in
     * the {@code JsonNode} in context, provided that the expression returns a single element
     * that can be mapped to {@code boolean}.
     *
     * @param jsonPath the path to read
     * @return the {@code Boolean} object associated with the specified {@code jsonPath};
     *         {@code null} if the path is not found
     *
     * @throws IllegalArgumentException if {@code jsonPath} is null or empty
     * @throws InvalidPathException     if the {@code jsonPath} expression is not valid
     * @throws ConfigurationException   if the {@code jsonPath} expression returns more than a
     *                                  single element
     * @throws ClassCastException       if the {@code jsonPath} result cannot be assigned to
     *                                  {@code boolean}
     */
    @Override
    public Boolean getBoolean(String jsonPath)
    {
        return getValue(jsonPath, Boolean.class, false);
    }

    /**
     * Returns the {@code Boolean} object associated with the specified {@code jsonPath} in
     * the {@code JsonNode} in context, provided that the expression returns a single element
     * that can be mapped to {@code boolean}.
     *
     * @param jsonPath the path to read
     * @return the {@code Boolean} object associated with the specified {@code jsonPath};
     *         never {@code null}
     *
     * @throws IllegalArgumentException if {@code jsonPath} is null or empty
     * @throws InvalidPathException     if the {@code jsonPath} expression is not valid
     * @throws ConfigurationException   if not value found or the {@code jsonPath} expression
     *                                  returns more than a single element
     * @throws ClassCastException       if the {@code jsonPath} result cannot be assigned to
     *                                  {@code boolean}
     * @since 0.4.0
     */
    @Override
    public Boolean getMandatoryBoolean(String jsonPath)
    {
        return getValue(jsonPath, Boolean.class);
    }

    /**
     * Returns the {@code Integer} object associated with the specified {@code jsonPath} in
     * the {@code JsonNode} in context, provided that the expression returns a single element
     * that can be mapped to {@code int}.
     *
     * @param jsonPath the path to read
     * @return the {@code Integer} object associated with the specified {@code jsonPath};
     *         {@code null} if the path is not found
     *
     * @throws IllegalArgumentException if {@code jsonPath} is null or empty
     * @throws InvalidPathException     if the {@code jsonPath} expression is not valid
     * @throws ConfigurationException   if the {@code jsonPath} expression returns more than a
     *                                  single element
     * @throws ClassCastException       if the {@code jsonPath} result cannot be assigned to
     *                                  {@code int}
     */
    @Override
    public Integer getInteger(String jsonPath)
    {
        return getValue(jsonPath, Integer.class, false);
    }

    /**
     * Returns the {@code Integer} object associated with the specified {@code jsonPath} in
     * the {@code JsonNode} in context, provided that the expression returns a single element
     * that can be mapped to {@code int}.
     *
     * @param jsonPath the path to read
     * @return the {@code Integer} object associated with the specified {@code jsonPath};
     *         never {@code null}
     *
     * @throws IllegalArgumentException if {@code jsonPath} is null or empty
     * @throws InvalidPathException     if the {@code jsonPath} expression is not valid
     * @throws ConfigurationException   if not value found or the {@code jsonPath} expression
     *                                  returns more than a single element
     * @throws ClassCastException       if the {@code jsonPath} result cannot be assigned to
     *                                  {@code int}
     * @since 0.4.0
     */
    @Override
    public Integer getMandatoryInteger(String jsonPath)
    {
        return getValue(jsonPath, Integer.class);
    }

    /**
     * Returns the {@code Long} object associated with the specified {@code jsonPath} in the
     * {@code JsonNode} in context, provided that the expression returns a single element that
     * can be mapped to {@code long}.
     *
     * @param jsonPath the path to read
     * @return the {@code Long} object associated with the specified {@code jsonPath};
     *         {@code null} if the path is not found
     *
     * @throws IllegalArgumentException if {@code jsonPath} is null or empty
     * @throws InvalidPathException     if the {@code jsonPath} expression is not valid
     * @throws ConfigurationException   if the {@code jsonPath} expression returns more than a
     *                                  single element
     * @throws ClassCastException       if the {@code jsonPath} result cannot be assigned to
     *                                  {@code long}
     */
    @Override
    public Long getLong(String jsonPath)
    {
        return getValue(jsonPath, Long.class, false);
    }

    /**
     * Returns the {@code Long} object associated with the specified {@code jsonPath} in the
     * {@code JsonNode} in context, provided that the expression returns a single element that
     * can be mapped to {@code long}.
     *
     * @param jsonPath the path to read
     * @return the {@code Long} object associated with the specified {@code jsonPath}; never
     *         {@code null}
     *
     * @throws IllegalArgumentException if {@code jsonPath} is null or empty
     * @throws InvalidPathException     if the {@code jsonPath} expression is not valid
     * @throws ConfigurationException   if not value found or the {@code jsonPath} expression
     *                                  returns more than a single element
     * @throws ClassCastException       if the {@code jsonPath} result cannot be assigned to
     *                                  {@code long}
     * @since 0.4.0
     */
    @Override
    public Long getMandatoryLong(String jsonPath)
    {
        return getValue(jsonPath, Long.class);
    }

    /**
     * Returns the {@code Double} object associated with the specified {@code jsonPath} in the
     * {@code JsonNode} in context, provided that the expression returns a single element that
     * can be mapped to {@code double}.
     *
     * @param jsonPath the path to read
     * @return the {@code Double} object associated with the specified {@code jsonPath};
     *         {@code null} if the path is not found
     *
     * @throws IllegalArgumentException if {@code jsonPath} is null or empty
     * @throws InvalidPathException     if the {@code jsonPath} expression is not valid
     * @throws ConfigurationException   if the {@code jsonPath} expression returns more than a
     *                                  single element
     * @throws ClassCastException       if the {@code jsonPath} result cannot be assigned to
     *                                  {@code double}
     */
    @Override
    public Double getDouble(String jsonPath)
    {
        return getValue(jsonPath, Double.class, false);
    }

    /**
     * Returns the {@code Double} object associated with the specified {@code jsonPath} in the
     * {@code JsonNode} in context, provided that the expression returns a single element that
     * can be mapped to {@code double}.
     *
     * @param jsonPath the path to read
     * @return the {@code Double} object associated with the specified {@code jsonPath}; never
     *         {@code null}
     *
     * @throws IllegalArgumentException if {@code jsonPath} is null or empty
     * @throws InvalidPathException     if the {@code jsonPath} expression is not valid
     * @throws ConfigurationException   if not value found or the {@code jsonPath} expression
     *                                  returns more than a single element
     * @throws ClassCastException       if the {@code jsonPath} result cannot be assigned to
     *                                  {@code double}
     * @since 0.4.0
     */
    @Override
    public Double getMandatoryDouble(String jsonPath)
    {
        return getValue(jsonPath, Double.class);
    }

    /**
     * Returns the {@code String} object associated with the specified {@code jsonPath} in the
     * {@code JsonNode} in context, provided that the expression returns a single element.
     *
     * @param jsonPath the path to read
     * @return the {@code String} object associated with the specified {@code jsonPath};
     *         {@code null} if the path is not found
     *
     * @throws IllegalArgumentException if {@code jsonPath} is null or empty
     * @throws InvalidPathException     if the {@code jsonPath} expression is not valid
     * @throws ConfigurationException   if the {@code jsonPath} expression returns more than a
     *                                  single element
     */
    @Override
    public String getString(String jsonPath)
    {
        return getValue(jsonPath, String.class, false);
    }

    /**
     * Returns the {@code String} object associated with the specified {@code jsonPath} in the
     * {@code JsonNode} in context, provided that the expression returns a single element.
     *
     * @param jsonPath the path to read
     * @return the {@code String} value associated with the specified {@code jsonPath}; never
     *         {@code null}
     *
     * @throws IllegalArgumentException if {@code jsonPath} is null or empty
     * @throws InvalidPathException     if the {@code jsonPath} expression is not valid
     * @throws ConfigurationException   if not value found or the {@code jsonPath} expression
     *                                  returns more than a single element
     */
    @Override
    public String getMandatoryString(String jsonPath)
    {
        return getValue(jsonPath, String.class);
    }

    /**
     * Returns the value associated with the specified {@code XPath} expression in the XML
     * document in context, provided that the expression returns a single element that can be
     * mapped to the specified class type.
     *
     * @param xPath      the {@code XPath} expression to read
     * @param targetType the type the expression result should be mapped to
     * @param mandatory  a flag determining whether or not an exception should be thrown in
     *                   case the value is not found
     *
     * @return the value associated with the specified {@code XPath}; never null
     *
     * @throws IllegalArgumentException if the {@code XPath} expression is null or empty
     * @throws InvalidPathException     if the {@code XPath} expression is not valid
     * @throws ConfigurationException   if the {@code XPath} expression returns no value or
     *                                  more than a single element
     */
    protected <T> T getValue(String jsonPath, Class<T> targetType)
    {
        return getValue(jsonPath, targetType, true);
    }

    /**
     * Returns the value associated with the specified {@code XPath} expression in the XML
     * document in context, provided that the expression returns a single element that can be
     * mapped to the specified class type.
     *
     * @param xPath      the {@code XPath} expression to read
     * @param targetType the type the expression result should be mapped to
     * @param mandatory  a flag determining whether or not an exception should be thrown in
     *                   case the value is not found
     *
     * @return the value associated with the specified {@code XPath}
     *
     * @throws IllegalArgumentException if the {@code XPath} expression is null or empty
     * @throws InvalidPathException     if the {@code XPath} expression is not valid
     * @throws ConfigurationException   if the {@code XPath} expression returns no value (with
     *                                  the {@code mandatory} flag enabled) or more than a
     *                                  single element
     */
    protected <T> T getValue(String xPath, Class<T> targetType, boolean mandatory)
    {
        NodeList result = get(xPath);
        switch (result.getLength())
        {
        case 0:
            if (mandatory)
            {
                throw new ConfigurationException("No value found for path: %s", xPath);
            }
            return null;
        case 1:
            Node node = result.item(0);
            return ParseFactory.parse(targetType, node.getTextContent());
        default:
            throw new ConfigurationException("Multiple values found for path: %s", xPath);
        }
    }

    /**
     * Returns the {@link NodeList} object associated with the specified @{code XPath} in the
     * XML document in context.
     *
     * @param xPath the {@code XPath} expression to read
     *
     * @return the object associated with the specified {@code XPath}; or an empty array if
     *         the path is not found
     *
     * @throws NullPointerException   if the {@code XPath} expression is null
     * @throws ConfigurationException if the {@code XPath} expression is not valid
     */
    @Override
    public NodeList get(String xPath)
    {
        try
        {
            return (NodeList) compileXPath(xPath).evaluate(document, XPathConstants.NODESET);
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
