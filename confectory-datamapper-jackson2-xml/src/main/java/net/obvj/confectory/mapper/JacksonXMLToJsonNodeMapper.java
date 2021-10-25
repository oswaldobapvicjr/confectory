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

import java.io.IOException;
import java.io.InputStream;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

/**
 * A specialized {@code Mapper} that deserializes a XML from an {@link InputStream} and
 * parses as a {@link JsonNode} using the Jackson provider.
 *
 * @author oswaldo.bapvic.jr (Oswaldo Junior)
 * @since 0.3.0
 */
public class JacksonXMLToJsonNodeMapper extends JacksonJsonNodeMapper implements Mapper<JsonNode>
{

    @Override
    public JsonNode apply(InputStream inputStream) throws IOException
    {
        ObjectMapper mapper = new XmlMapper();
        return mapper.readValue(inputStream, JsonNode.class);
    }

}
