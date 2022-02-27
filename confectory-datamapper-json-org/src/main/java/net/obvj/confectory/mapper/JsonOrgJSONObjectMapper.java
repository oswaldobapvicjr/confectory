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

import org.json.JSONObject;
import org.json.JSONTokener;

import net.obvj.confectory.helper.ConfigurationHelper;
import net.obvj.confectory.helper.JsonOrgJSONObjectHelper;

/**
 * A specialized {@code Mapper} that loads the contents of a valid JSON {@code Source}
 * (e.g.: file, URL) as a {@link JSONObject} ({@code json.org} reference implementation).
 *
 * @author oswaldo.bapvic.jr (Oswaldo Junior)
 * @since 0.2.0
 */
public class JsonOrgJSONObjectMapper implements Mapper<JSONObject>
{

    @Override
    public JSONObject apply(InputStream inputStream) throws IOException
    {
        JSONTokener tokener = new JSONTokener(inputStream);
        return new JSONObject(tokener);
    }

    @Override
    public ConfigurationHelper<JSONObject> configurationHelper(JSONObject jsonObject)
    {
        return new JsonOrgJSONObjectHelper(jsonObject);
    }

}
