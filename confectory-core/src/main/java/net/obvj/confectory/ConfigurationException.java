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
 * A specialized runtime exception for generic configuration handling.
 *
 * @author oswaldo.bapvic.jr (Oswaldo Junior)
 * @since 0.1.0
 */
public class ConfigurationException extends RuntimeException
{
    private static final long serialVersionUID = -4779778956015853543L;

    /**
     * Constructs a new exception with the specified detail message. A detail message is a
     * String that describes this particular exception.
     *
     * @param message the detailed message, which is saved for later retrieval by the
     *                {@link Throwable#getMessage()} method
     * @param args    arguments to the message format, as in
     *                {@link String#format(String, Object...)}
     */
    public ConfigurationException(String message, Object... args)
    {
        super(String.format(message, args));
    }

    /**
     * Constructs a new exception with the specified detail message and cause.
     *
     * @param cause   the cause, which is saved for later retrieval by the
     *                {@link Throwable#getCause()} method. (A {@code null} value is permitted,
     *                and indicates that the cause is nonexistent or unknown)
     * @param message the detail message, which is saved for later retrieval by the
     *                {@link Throwable#getMessage()} method
     * @param args    arguments to the message format, as in
     *                {@link String#format(String, Object...)}
     */
    public ConfigurationException(Throwable cause, String message, Object... args)
    {
        super(String.format(message, args), cause);
    }

    /**
     * Constructs a new exception with the specified cause. This constructor is useful for
     * exceptions that are wrappers for other throwables.
     *
     * @param cause the cause (which is saved for later retrieval by the
     *              {@link Throwable#getCause()} method). A {@code null} value is permitted,
     *              and indicates that the cause is nonexistent or unknown.
     */
    public ConfigurationException(Throwable cause)
    {
        super(cause);
    }
}
