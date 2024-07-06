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

package net.obvj.confectory.internal.helper;

import net.obvj.confectory.ConfigurationException;
import net.obvj.confectory.SaxonProvider;
import net.obvj.confectory.merger.ConfigurationMerger;
import net.obvj.confectory.util.ParseException;
import net.obvj.confectory.util.TypeFactory;
import net.sf.saxon.s9api.*;

/**
 * A specialized Configuration Helper that retrieves data from Saxon-HE's
 * {@link XdmNode}, with XPath capabilities.
 *
 * @author oswaldo.bapvic.jr (Oswaldo Junior)
 * @since 2.6.0
 */
public class SaxonXdmNodeConfigurationHelper extends AbstractConfigurationHelper<XdmNode> implements ConfigurationHelper<XdmNode>
{

    private final XdmNode document;
    private final XPathCompiler compiler = SaxonProvider.newXPathCompiler();

    /**
     * Creates a new helper for the given {@link XdmNode}.
     *
     * @param document the {@link XdmNode} to set
     */
    public SaxonXdmNodeConfigurationHelper(XdmNode document)
    {
        this.document = document;
    }

    @Override
    public XdmNode getBean()
    {
        return document;
    }

    @Override
    public String getAsString()
    {
        return document.toString();
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
        XdmValue result = get(xpath);
        switch (result.size())
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
                XdmItem item = result.itemAt(0);

                return TypeFactory.parse(targetType, item.getStringValue());
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

    @Override
    public ConfigurationMerger<XdmNode> configurationMerger()
    {
        throw new UnsupportedOperationException("Merge not supported for XML");
    }

    @Override
    public XdmValue get(String xpath)
    {
        try
        {
            XPathSelector selector = compiler.compile(xpath).load();
            selector.setContextItem(document);
            return selector.evaluate();
        }
        catch (SaxonApiException e)
        {
            throw new ConfigurationException(e);
        }
    }
}
