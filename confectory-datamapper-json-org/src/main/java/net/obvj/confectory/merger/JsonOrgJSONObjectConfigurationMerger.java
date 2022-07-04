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

package net.obvj.confectory.merger;

import java.util.Collections;
import java.util.Map;

import org.json.JSONObject;

import com.jayway.jsonpath.InvalidPathException;

import net.obvj.confectory.Configuration;
import net.obvj.confectory.util.JsonOrgJsonProvider;
import net.obvj.confectory.util.JsonProvider;

/**
 * A specialized {@code ConfigurationMerger} that combines two {@link Configuration}
 * objects of type {@link JSONObject} ({@code json.org} implementation) into a single one.
 * <p>
 * For additional information, refer to the superclass
 * {@link GenericJSONConfigurationMerger}.
 *
 * @see GenericJSONConfigurationMerger
 * @author oswaldo.bapvic.jr (Oswaldo Junior)
 * @since 2.2.0
 */
public class JsonOrgJSONObjectConfigurationMerger extends GenericJSONConfigurationMerger<JSONObject>
{

    /**
     * Creates a new JSON Configuration Merger for {@link JSONObject} using the
     * {@code json.org} implementation.
     */
    public JsonOrgJSONObjectConfigurationMerger()
    {
        this(Collections.emptyMap());
    }

    /**
     * Creates a new JSON Configuration Merger for {@link JSONObject} using the
     * {@code json.org} implementation with a preset map of distinct keys.
     * <p>
     * For additional information, refer to
     * {@link GenericJSONConfigurationMerger#GenericJSONConfigurationMerger(JsonProvider, Map)}.
     *
     * @param distinctObjectKeysInsideArrays a map that associates JsonPath expressions and
     *                                       distinct keys during the merge of an array;
     *                                       {@code null} is allowed
     *
     * @throws IllegalArgumentException if the map contains a null or empty expression
     * @throws InvalidPathException     if the specified JsonPath expression is invalid
     */
    public JsonOrgJSONObjectConfigurationMerger(Map<String, String> distinctObjectKeysInsideArrays)
    {
        super(new JsonOrgJsonProvider(), distinctObjectKeysInsideArrays);
    }
}
