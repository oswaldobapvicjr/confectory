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

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerFactory;

/**
 * Common methods for working with XML.
 *
 * @author oswaldo.bapvic.jr (Oswaldo Junior)
 * @since 2.5.0
 */
public class XMLUtils
{

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
}
