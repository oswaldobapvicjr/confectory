package net.obvj.confectory.util;

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
     * <p>
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
    public static String expandEnvironmentVariables(String string)
    {
        return ENVIRONMENT_VARIABLE_SUBSTITUTOR.replace(string);
    }

}
