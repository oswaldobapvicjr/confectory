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
import java.io.InputStreamReader;
import java.io.Reader;

import org.json.JSONObject;
import org.json.XML;

/**
 * A specialized {@code Mapper} that loads the contents of a valid XML {@code Source}
 * (e.g.: file, URL) and converts it into a {@link JSONObject} (using {@code json.org}
 * reference implementation).
 * <p>
 * Because of differences between XML and JSON formats, the document structure may suffer
 * modifications in this transformation:
 * <ul>
 * <li>XML uses elements, attributes, and content text, while JSON uses unordered
 * collections of name/value pairs and arrays of values.</li>
 * <li>JSON does not distinguish between elements and attributes.</li>
 * <li>Sequences of similar elements are represented as {@code JSONArray}.</li>
 * <li>Content text may be placed in a "content" member.</li>
 * <li>Comments, prologs, DTDs, are ignored.</li>
 * </ul>
 * <p>
 * <strong>Note:</strong> Conversion from XML to JSON may vary depending on the
 * {@link Mapper} implementation.
 *
 * @author oswaldo.bapvic.jr (Oswaldo Junior)
 * @since 2.0.0 (<b>note:</b> since 0.2.0 as {@code XMLTOJSONObjectMapper})
 */
public class JsonOrgXMLToJSONObjectMapper extends JsonOrgJSONObjectMapper implements Mapper<JSONObject>
{

    @Override
    public JSONObject apply(InputStream inputStream) throws IOException
    {
        Reader reader = new InputStreamReader(inputStream);
        return XML.toJSONObject(reader);
    }

}
