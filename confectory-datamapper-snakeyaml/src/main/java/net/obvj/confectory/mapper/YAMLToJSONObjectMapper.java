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

import net.minidev.json.JSONObject;
import net.obvj.confectory.helper.ConfigurationHelper;
import net.obvj.confectory.helper.SmartJsonConfigurationHelper;

/**
 * A specialized {@code Mapper} that loads the contents of a valid YAML {@code Source}
 * (e.g.: file, URL, string) as a {@link JSONObject}, using SnakeYAML.
 *
 * @author oswaldo.bapvic.jr (Oswaldo Junior)
 * @since 1.1.0
 */
public class YAMLToJSONObjectMapper extends YAMLToObjectMapper<JSONObject> implements Mapper<JSONObject>
{

    /**
     * Builds a new YAML mapper
     */
    public YAMLToJSONObjectMapper()
    {
        super(JSONObject.class);
    }

    @Override
    public ConfigurationHelper<JSONObject> configurationHelper(JSONObject jsonObject)
    {
        return new SmartJsonConfigurationHelper(jsonObject);
    }

}
