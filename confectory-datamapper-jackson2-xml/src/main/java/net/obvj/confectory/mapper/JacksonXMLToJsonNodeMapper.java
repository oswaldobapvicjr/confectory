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

/**
 * A specialized {@code Mapper} that loads the contents of a valid XML {@code Source}
 * (e.g.: file, URL, string) and converts it into a {@link JsonNode}, using Jackson's
 * {@link XmlMapper}.
 * <p>
 * Because of differences between XML and JSON formats, the document structure may suffer
 * modifications in this transformation. XML uses elements, attributes, and content text,
 * while JSON uses unordered collections of name/value pairs and arrays of values. JSON
 * does not distinguish between elements and attributes.
 * <p>
 * Additional details may be found at Jackson's official documentation.
 * <p>
 * <strong>Note:</strong> Conversion from XML to JSON may vary depending on the
 * {@link Mapper} implementation.
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
        super(JsonNode.class);
    }

}
