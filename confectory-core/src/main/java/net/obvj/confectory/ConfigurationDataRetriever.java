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
     * Returns the configuration bean used by this data retriever, typically for manual
     * handling and/or troubleshooting purposes.
     *
     * @return the configuration bean (<b>Note: </b> it can be {@code null} if the
     *         {@code Configuration} is marked as optional)
     */
    T getBean();

    /**
     * Returns the {@code Boolean} object associated with the specified {@code key}.
     *
     * @param key the object key (some implementations may also accept a path expression, e.g:
     *            {@code JSONPath})
     *
     * @return the {@code Boolean} object associated with the specified {@code key};
     *         {@code null} if not found
     */
    Boolean getBoolean(String key);

    /**
     * Returns the {@code Integer} object associated with the specified {@code key}.
     *
     * @param key the object key (some implementations may also accept a path expression, e.g:
     *            {@code JSONPath}
     *
     * @return the {@code Integer} object associated with the specified {@code key};
     *         {@code null} if not found
     *
     * @throws NumberFormatException if the value is not a parsable {@code int}.
     */
    Integer getInteger(String key);

    /**
     * Returns the {@code Long} object associated with the specified {@code key}.
     *
     * @param key the object key (some implementations may also accept a path expression, e.g:
     *            {@code JSONPath}
     *
     * @return the {@code Long} object associated with the specified {@code key}; {@code null}
     *         if not found
     *
     * @throws NumberFormatException if the value is not a parsable {@code long}.
     */
    Long getLong(String key);

    /**
     * Returns the {@code Double} object associated with the specified {@code key}.
     *
     * @param key the object key (some implementations may also accept a path expression, e.g:
     *            {@code JSONPath}
     *
     * @return the {@code Double} object associated with the specified {@code key};
     *         {@code null} if not found
     *
     * @throws NumberFormatException if the value is not a parsable {@code double}.
     */
    Double getDouble(String key);

    /**
     * Returns the {@code String} object associated with the specified {@code key}.
     *
     * @param key the object key (some implementations may also accept a path expression, e.g:
     *            {@code JSONPath})
     *
     * @return the {@code String} object associated with the specified {@code key};
     *         {@code null} if not found
     */
    String getString(String key);

    /**
     * Returns the {@code Boolean} object associated with the specified {@code key}, throwing
     * an exception if not found.
     *
     * @param key the object key (some implementations may also accept a path expression, e.g:
     *            {@code JSONPath})
     *
     * @return the {@code Boolean} value associated with the specified {@code key}; never
     *         {@code null}
     *
     * @throws ConfigurationException if the specified key (or path) is not found.
     *
     * @since 0.4.0
     */
    Boolean getMandatoryBoolean(String key);

    /**
     * Returns the {@code Integer} object associated with the specified {@code key}, throwing
     * an exception if not found.
     *
     * @param key the object key (some implementations may also accept a path expression, e.g:
     *            {@code JSONPath})
     *
     * @return the {@code Integer} object associated with the specified {@code key}; never
     *         {@code null}
     *
     * @throws ConfigurationException if the specified key (or path) is not found.
     * @throws NumberFormatException  if the value is not a parsable {@code int}.
     *
     * @since 0.4.0
     */
    Integer getMandatoryInteger(String key);

    /**
     * Returns the {@code Long} object associated with the specified {@code key}, throwing an
     * exception if not found.
     *
     * @param key the object key (some implementations may also accept a path expression, e.g:
     *            {@code JSONPath})
     *
     * @return the {@code Long} object associated with the specified {@code key}; never
     *         {@code null}
     *
     * @throws ConfigurationException if the specified key (or path) is not found.
     * @throws NumberFormatException  if the value is not a parsable {@code long}.
     *
     * @since 0.4.0
     */
    Long getMandatoryLong(String key);

    /**
     * Returns the {@code Double} object associated with the specified {@code key}, throwing
     * an exception if not found.
     *
     * @param key the object key (some implementations may also accept a path expression, e.g:
     *            {@code JSONPath})
     *
     * @return the {@code Double} object associated with the specified {@code key}; never
     *         {@code null}
     *
     * @throws ConfigurationException if the specified key (or path) is not found.
     * @throws NumberFormatException  if the value is not a parsable {@code double}.
     *
     * @since 0.4.0
     */
    Double getMandatoryDouble(String key);

    /**
     * Returns the {@code String} object associated with the specified {@code key}, throwing
     * an exception if not found.
     *
     * @param key the object key (some implementations may also accept a path expression, e.g:
     *            {@code JSONPath})
     *
     * @return the {@code String} object associated with the specified {@code key}; never
     *         {@code null}
     *
     * @throws ConfigurationException if the specified key (or path) is not found.
     *
     * @since 0.4.0
     */
    String getMandatoryString(String key);

}
