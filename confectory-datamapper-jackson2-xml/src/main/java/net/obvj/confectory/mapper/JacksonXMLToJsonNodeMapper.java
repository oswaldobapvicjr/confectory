/*
 * Copyright 2021 obvj.net
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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import net.obvj.confectory.internal.helper.ConfigurationHelper;
import net.obvj.confectory.internal.helper.JacksonJsonNodeHelper;

/**
 * A specialized {@code Mapper} that loads the contents of a valid XML {@code Source}
 * (e.g.: file, URL, string) and converts it into a {@link JsonNode}, using Jackson's
 * {@link XmlMapper}.
 * <p>
 * This allows fetching the contents using <b>JSONPath</b> expressions.
 * <p>
 * Because of differences between XML and JSON formats, there are certain limitations with
 * Jackson's XML tree traversal support:
 * <ul>
 * <li>Jackson cannot differentiate between an Object and an Array. Since XML lacks native
 * structures to distinguish an object from a list of objects, Jackson will simply collate
 * repeated elements into a single value.</li>
 * <li>Since Jackson maps each XML element to a JSON node, it doesn't support mixed
 * content (elements/attributes and text in same element).</li>
 * </ul>
 * <p>
 * Additional details can be found at
 * <a href="https://github.com/FasterXML/jackson-dataformat-xml#known-limitations">
 * Jackson's official documentation</a>.
 * <p>
 * <strong>Notes:</strong>
 * <ul>
 * <li>Conversion from XML to JSON may vary depending on the {@link Mapper}
 * implementation.</li>
 * <li>Support for Jackson modules lookup is disabled for this type of mapper.</li>
 * </ul>
 *
 * @author oswaldo.bapvic.jr (Oswaldo Junior)
 * @since 0.3.0
 */
public class JacksonXMLToJsonNodeMapper extends JacksonXMLToObjectMapper<JsonNode> implements Mapper<JsonNode>
{

    /**
     * Builds a new XML-to-JSON mapper.
     */
    public JacksonXMLToJsonNodeMapper()
    {
        super(JsonNode.class, true);
    }

    @Override
    public ConfigurationHelper<JsonNode> configurationHelper(JsonNode jsonNode)
    {
        return new JacksonJsonNodeHelper(jsonNode);
    }

}
