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
import net.obvj.confectory.util.ObjectFactory;

/**
 * An object that defines the global settings for the {@code Confectory} project.
 *
 * @author oswaldo.bapvic.jr
 * @since 0.1.0
 */
public class ConfectorySettings
{

    /**
     * The initial {@link DataFetchStrategy} applied by default
     */
    static final DataFetchStrategy INITIAL_DATA_FETCH_STRATEGY = DataFetchStrategy.LENIENT;

    /**
     * The initial {@link ObjectFactory} applied by default
     */
    static final ObjectFactory INITIAL_OBJECT_FACTORY = ObjectFactory.ENHANCED;

    private static final ConfectorySettings INSTANCE = new ConfectorySettings();

    // Settings - start

    private DataFetchStrategy defaultDataFetchStrategy;
    private ObjectFactory objectFactory;

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
        defaultDataFetchStrategy = INITIAL_DATA_FETCH_STRATEGY;
        objectFactory = INITIAL_OBJECT_FACTORY;
    }

    /**
     * @return a reference to the the current {@link ConfectorySettings} instance.
     */
    public static ConfectorySettings instance()
    {
        return INSTANCE;
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

    /**
     * @return the {@link ObjectFactory} to be produce new objects
     * @since 2.5.0
     */
    public ObjectFactory getObjectFactory()
    {
        return objectFactory;
    }

    /**
     * Defines the {@link ObjectFactory} to produce new objects.
     *
     * @param objectFactory the {@link ObjectFactory} to set; not null
     * @throws NullPointerException if the specified {@link ObjectFactory} is null
     * @since 2.5.0
     */
    public void setObjectFactory(ObjectFactory objectFactory)
    {
        this.objectFactory = Objects.requireNonNull(objectFactory,
                "the ObjectFactory must not be null");
    }

}
