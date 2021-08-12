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

package net.obvj.confectory;

import net.obvj.confectory.settings.ConfectorySettings;

/**
 * A Facade for common operations in the {@code Confectory} project.
 *
 * @author FernandoNSC (Fernando Tiannamen)
 * @since 0.1.0
 */
public final class Confectory
{

    /**
     * The current {@link ConfigurationContainer} to be loaded as default
     */
    private static ConfigurationContainer globalConfigurationContainer = new ConfigurationContainer();

    private Confectory()
    {
        throw new IllegalStateException("Instantiation not allowed");
    }

    /**
     * Retrieves a {@link ConfigurationContainer} instance that can be accessed statically for
     * holding global configuration data.
     * <p>
     * It is possible to copy objects from an existing {@code ConfigurationContainer} into the
     * global instance by calling:
     *
     * <blockquote>
     *
     * <pre>
     * {@code Confectory.container().addAll(otherContainer);}
     * </pre>
     *
     * </blockquote>
     *
     * <p>
     * <strong>Note:</strong> {@code Configuration} data stored in the global container may be
     * shared by other applications loaded in the same classpath.
     *
     * @return the global {@link ConfigurationContainer} instance
     */
    public static ConfigurationContainer container()
    {
        return globalConfigurationContainer;
    }

    /**
     * Returns an object containing global settings for the {@code Confectory} project.
     *
     * @return a reference to the current {@link ConfectorySettings} instance
     */
    public static ConfectorySettings settings()
    {
        return ConfectorySettings.getInstance();
    }

}
