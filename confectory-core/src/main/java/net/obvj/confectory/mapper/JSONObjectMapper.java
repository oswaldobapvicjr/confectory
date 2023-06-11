/*
 * Copyright 2022 obvj.net
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
import java.io.UnsupportedEncodingException;

import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import net.obvj.confectory.ConfigurationException;
import net.obvj.confectory.internal.helper.ConfigurationHelper;
import net.obvj.confectory.internal.helper.JsonSmartConfigurationHelper;

/**
 * A specialized {@code Mapper} that loads the contents of a valid JSON {@code Source}
 * (e.g.: file, URL) as a {@link JSONObject} ({@code json-smart} JSON implementation).
 * <p>
 * This allows fetching the contents using <b>JSONPath</b> expressions.
 *
 * @author oswaldo.bapvic.jr (Oswaldo Junior)
 * @since 2.0.0
 */
public class JSONObjectMapper implements Mapper<JSONObject>
{

    @Override
    public JSONObject apply(InputStream inputStream) throws IOException
    {
        JSONParser parser = new JSONParser(JSONParser.DEFAULT_PERMISSIVE_MODE);
        try
        {
            return parser.parse(inputStream, JSONObject.class);
        }
        catch (UnsupportedEncodingException | ParseException exception)
        {
            throw new ConfigurationException(exception);
        }
    }

    @Override
    public ConfigurationHelper<JSONObject> configurationHelper(JSONObject jsonObject)
    {
        return new JsonSmartConfigurationHelper(jsonObject);
    }

}
