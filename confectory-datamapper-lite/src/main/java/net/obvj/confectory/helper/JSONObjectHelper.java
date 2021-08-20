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

package net.obvj.confectory.helper;

import java.util.Optional;

import org.json.JSONObject;

/**
 * A specialized Configuration Helper that retrieves data from a {@link JSONObject}.
 *
 * @author oswaldo.bapvic.jr (Oswaldo Junior)
 * @since 0.2.0
 */
public class JSONObjectHelper extends AbstractBasicConfigurationHelper<JSONObject>
{
    protected final JSONObject jsonObject;

    public JSONObjectHelper(JSONObject jsonObject)
    {
        this.jsonObject = jsonObject;
    }

    @Override
    public Optional<JSONObject> getBean()
    {
        return Optional.ofNullable(jsonObject);
    }

    @Override
    public boolean getBoolean(String key)
    {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int getInt(String key)
    {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public long getLong(String key)
    {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public double getDouble(String key)
    {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public String getString(String key)
    {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
