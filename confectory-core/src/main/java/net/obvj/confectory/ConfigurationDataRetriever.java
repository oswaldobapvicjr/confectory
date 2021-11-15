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

import java.util.Optional;

/**
 * A base interface for objects that retrieve configuration data.
 *
 * @param <T> the target type which configuration data is to be retrieved
 *
 * @author oswaldo.bapvic.jr (Oswaldo Junior)
 * @since 0.1.0
 */
public interface ConfigurationDataRetriever<T>
{

    /**
     * Returns the target configuration object used by this data retriever, typically for
     * manual handling and/or troubleshooting purposes.
     *
     * @return an {@link Optional}, possibly containing the target configuration object
     */
    Optional<T> getBean();

    /**
     * Returns the {@code boolean} value associated with the specified {@code key}.
     *
     * @param key the property key (some implementations may also accept a path expression,
     *            e.g: {@code JSONPath})
     *
     * @return the {@code boolean} value associated with the specified {@code key}
     */
    boolean getBoolean(String key);

    /**
     * Returns the {@code int} value associated with the specified {@code key}.
     *
     * @param key the property key (some implementations may also accept a path expression,
     *            e.g: {@code JSONPath}
     *
     * @return the {@code int} value associated with the specified {@code key}
     * @throws NumberFormatException if the value is not a parsable {@code int}.
     */
    int getInt(String key);

    /**
     * Returns the {@code long} value associated with the specified {@code key}.
     *
     * @param key the property key (some implementations may also accept a path expression,
     *            e.g: {@code JSONPath}
     *
     * @return the {@code long} value associated with the specified {@code key}
     * @throws NumberFormatException if the value is not a parsable {@code long}.
     */
    long getLong(String key);

    /**
     * Returns the {@code double} value associated with the specified {@code key}.
     *
     * @param key the property key (some implementations may also accept a path expression,
     *            e.g: {@code JSONPath}
     *
     * @return the {@code double} value associated with the specified {@code key}
     * @throws NumberFormatException if the value is not a parsable {@code double}.
     */
    double getDouble(String key);

    /**
     * Returns the {@code String} value associated with the specified {@code key}.
     *
     * @param key the property key (some implementations may also accept a path expression,
     *            e.g: {@code JSONPath})
     *
     * @return the {@code String} value associated with the specified {@code key}
     */
    String getString(String key);

    /**
     * Returns the {@code boolean} value associated with the specified {@code key}.
     *
     * @param key the property key (some implementations may also accept a path expression,
     *            e.g: {@code JSONPath})
     *
     * @return the {@code boolean} value associated with the specified {@code key}
     *
     * @throws ConfigurationException if the specified key (or path) is not found.
     *
     * @since 0.4.0
     */
    boolean getMandatoryBoolean(String key);

    /**
     * Returns the {@code int} value associated with the specified {@code key}.
     *
     * @param key the property key (some implementations may also accept a path expression,
     *            e.g: {@code JSONPath})
     *
     * @return the {@code int} value associated with the specified {@code key}
     *
     * @throws ConfigurationException if the specified key (or path) is not found.
     * @throws NumberFormatException  if the value is not a parsable {@code int}.
     *
     * @since 0.4.0
     */
    int getMandatoryInt(String key);

    /**
     * Returns the {@code long} value associated with the specified {@code key}.
     *
     * @param key the property key (some implementations may also accept a path expression,
     *            e.g: {@code JSONPath})
     *
     * @return the {@code long} value associated with the specified {@code key}
     *
     * @throws ConfigurationException if the specified key (or path) is not found.
     * @throws NumberFormatException  if the value is not a parsable {@code long}.
     *
     * @since 0.4.0
     */
    long getMandatoryLong(String key);

    /**
     * Returns the {@code double} value associated with the specified {@code key}.
     *
     * @param key the property key (some implementations may also accept a path expression,
     *            e.g: {@code JSONPath})
     *
     * @return the {@code double} value associated with the specified {@code key}
     *
     * @throws ConfigurationException if the specified key (or path) is not found.
     * @throws NumberFormatException  if the value is not a parsable {@code double}.
     *
     * @since 0.4.0
     */
    double getMandatoryDouble(String key);

    /**
     * Returns the {@code String} value associated with the specified {@code key}.
     *
     * @param key the property key (some implementations may also accept a path expression,
     *            e.g: {@code JSONPath})
     *
     * @return the {@code String} value associated with the specified {@code key}
     *
     * @throws ConfigurationException if the specified key (or path) is not found.
     *
     * @since 0.4.0
     */
    String getMandatoryString(String key);

}
