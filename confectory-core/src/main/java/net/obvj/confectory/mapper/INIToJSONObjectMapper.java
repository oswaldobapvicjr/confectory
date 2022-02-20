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

import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;
import net.obvj.confectory.helper.ConfigurationHelper;
import net.obvj.confectory.helper.JsonSmartConfigurationHelper;

/**
 * A specialized {@code Mapper} that loads the contents of a valid INI {@code Source}
 * (e.g.: file, URL) as a {@link JSONObject}.
 *
 * @author oswaldo.bapvic.jr (Oswaldo Junior)
 * @since 1.3.0
 */
public class INIToJSONObjectMapper extends AbstractINIMapper<JSONObject> implements Mapper<JSONObject>
{
    @Override
    public JSONObject apply(InputStream inputStream) throws IOException
    {
        return (JSONObject) doApply(inputStream);
    }

    @Override
    Object newObject(Context context)
    {
        return new JSONObject();
    }

    @Override
    Object parseValue(Context context, String value)
    {
        return JSONValue.parse(value); // return either null, number, boolean, or string
    }

    @Override
    void put(Object target, String name, Object value)
    {
        ((JSONObject) target).put(name, value);
    }

    @Override
    public ConfigurationHelper<JSONObject> configurationHelper(JSONObject object)
    {
        return new JsonSmartConfigurationHelper(object);
    }

}
