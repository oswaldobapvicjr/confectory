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

package net.obvj.confectory.helper.nullvalue;

/**
 * An abstract {@link NullValueProvider} with common methods implemented.
 *
 * @author oswaldo.bapvic.jr (Oswaldo Junior)
 * @since 0.1.0
 */
public abstract class AbstractNullValueProvider implements NullValueProvider
{

    @Override
    public boolean isNull(boolean value)
    {
        return value == getBooleanValue();
    }

    @Override
    public boolean isNull(int value)
    {
        return value == getIntValue();
    }

    @Override
    public boolean isNull(long value)
    {
        return value == getLongValue();
    }

    @Override
    public boolean isNull(double value)
    {
        return value == getDoubleValue();
    }

    @Override
    public boolean isNull(String string)
    {
        return getStringValue().equals(string);
    }

}
