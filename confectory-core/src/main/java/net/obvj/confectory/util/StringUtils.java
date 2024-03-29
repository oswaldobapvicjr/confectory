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

package net.obvj.confectory.util;

import java.util.function.Supplier;

import org.apache.commons.text.StringSubstitutor;
import org.apache.commons.text.lookup.StringLookupFactory;

/**
 * Common methods for working with strings.
 *
 * @author oswaldo.bapvic.jr (Oswaldo Junior)
 * @since 0.1.0
 */
public class StringUtils
{
    private static final StringSubstitutor ENVIRONMENT_VARIABLE_SUBSTITUTOR = new StringSubstitutor(
            StringLookupFactory.INSTANCE.environmentVariableStringLookup());

    private StringUtils()
    {
        throw new IllegalStateException("Instantiation not allowed");
    }

    /**
     * Replaces all the occurrences of system environment variables placed between
     * <code>"${"</code> and <code>"}"</code> with their matching values from the given source
     * string.
     * <p>
     * For example:
     * <blockquote>
     *
     * <pre>
     * StringUtils.expandEnvironmentVariables("${TMPDIR}/file1") = "/tmp/file1"
     * </pre>
     *
     * </blockquote>
     *
     * @param string the string to be expanded; {@code null} returns {@code null}
     * @return the expanded string
     */
    public static String expandEnvironmentVariables(final String string)
    {
        return ENVIRONMENT_VARIABLE_SUBSTITUTOR.replace(string);
    }

    /**
     * Returns either the passed in string, or if the string is empty ({@code ""}) or
     * {@code null}, the value returned by the specified supplier.
     * <p>
     * For example:
     * <blockquote>
     *
     * <pre>
     * StringUtils.defaultIfEmpty(null,() -&gt; "default") = "default"
     * StringUtils.defaultIfEmpty("",  () -&gt; "default") = "default"
     * StringUtils.defaultIfEmpty("a", () -&gt; "default") = "a"
     * </pre>
     *
     * </blockquote>
     *
     * @param string          the string to check; may be {@code null}
     * @param defaultSupplier a string supplier function to be executed only if the original
     *                        string is empty or {@code null}
     *
     * @return the passed in string, or the value returned by the specified supplier
     * @throws NullPointerException if the passed in supplier is null
     * @since 2.5.0
     */
    public static String defaultIfEmpty(final String string, final Supplier<String> defaultSupplier)
    {
        return string == null || string.isEmpty() ? defaultSupplier.get() : string;
    }

}
