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

package net.obvj.confectory.mapper;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.json.JSONObject;
import org.json.XML;
import org.w3c.dom.Document;

public class JsonOrgJSONToXMLMapper extends DocumentMapper implements Mapper<Document>
{
    private static final String DEFAULT_ROOT_ELEMENT_NAME = "root";

    private final String rootElementName;

    public JsonOrgJSONToXMLMapper()
    {
        this(DEFAULT_ROOT_ELEMENT_NAME);
    }

    public JsonOrgJSONToXMLMapper(String rootElementName)
    {
        this.rootElementName = rootElementName;
    }

    @Override
    public Document apply(InputStream inputStream) throws IOException
    {
        JSONObject json = new JsonOrgJSONObjectMapper().apply(inputStream);
        // A root element may be required
        String rootElement = json.length() == 1 ? null : rootElementName;
        String xml = XML.toString(json, rootElement);
        return super.apply(new ByteArrayInputStream(xml.getBytes()));
    }

}
