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

package net.obvj.confectory.settings;

import java.util.Objects;

import net.obvj.confectory.DataFetchStrategy;
import net.obvj.confectory.helper.nullvalue.NullValueProvider;
import net.obvj.confectory.helper.nullvalue.StandardNullValueProvider;

/**
 * An object that defines the global settings for the {@code Confectory} project.
 *
 * @author oswaldo.bapvic.jr
 * @since 0.1.0
 */
public class ConfectorySettings
{
    /**
     * The initial {@link NullValueProvider} to be applied by default
     */
    protected static final NullValueProvider INITIAL_NULL_VALUE_PROVIDER = StandardNullValueProvider.instance();

    /**
     * The initial {@link DataFetchStrategy} to be applied by default
     */
    protected static final DataFetchStrategy INITIAL_DATA_FETCH_STRATEGY = DataFetchStrategy.STRICT;

    private static final ConfectorySettings INSTANCE = new ConfectorySettings();

    // Settings - start

    private NullValueProvider defaultNullValueProvider;
    private DataFetchStrategy defaultDataFetchStrategy;

    /*
     * Private constructor to hide the default, implicit one
     */
    private ConfectorySettings()
    {
        reset();
    }

    /**
     * Resets {@code Confectory} configuration.
     */
    public void reset()
    {
        defaultNullValueProvider = INITIAL_NULL_VALUE_PROVIDER;
        defaultDataFetchStrategy = INITIAL_DATA_FETCH_STRATEGY;
    }

    /**
     * @return a reference to the the current {@link ConfectorySettings} instance.
     */
    public static ConfectorySettings getInstance()
    {
        return INSTANCE;
    }

    /**
     * Returns the {@link NullValueProvider} to be applied by default when no such provider
     * specified at {@code Configuration} level.
     *
     * @return the default {@code NullValueProvider}
     */
    public NullValueProvider getDefaultNullValueProvider()
    {
        return defaultNullValueProvider;
    }

    /**
     * Defines the {@link NullValueProvider} to be applied by default when no such provider
     * specified at {@code Configuration} level.
     *
     * @param provider the default {@code NullValueProvider} to set; not null
     * @throws NullPointerException if the specified provider is null
     */
    public void setDefaultNullValueProvider(NullValueProvider provider)
    {
        defaultNullValueProvider = Objects.requireNonNull(provider, "null is not allowed");
    }

    /**
     * Returns the {@link DataFetchStrategy} to be applied by default when no specific
     * strategy is defined.
     *
     * @return the default {@link DataFetchStrategy} to be applied
     */
    public DataFetchStrategy getDefaultDataFetchStrategy()
    {
        return defaultDataFetchStrategy;
    }

    /**
     * Defines the {@link DataFetchStrategy} to be applied by default when no specific
     * strategy is defined.
     *
     * @param strategy the default {@link DataFetchStrategy} to set; not null
     * @throws NullPointerException if the specified strategy is null
     */
    public void setDefaultDataFetchStrategy(DataFetchStrategy strategy)
    {
        this.defaultDataFetchStrategy = Objects.requireNonNull(strategy,
                "the default DataFetchStrategy must not be null");
    }

}
