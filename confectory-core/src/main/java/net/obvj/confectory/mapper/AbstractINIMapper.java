/*
 * Copyright 2022 obvj.net
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

package net.obvj.confectory.mapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import net.obvj.confectory.ConfigurationSourceException;

/**
 * An abstract {@code Mapper} that defines a template of how to parse the contents of a
 * valid INI {@code Source} (e.g.: file, URL).
 * <p>
 * It delegates the final object type population to a concrete implementation.
 *
 * @author oswaldo.bapvic.jr (Oswaldo Junior)
 * @since 2.0.0
 */
public abstract class AbstractINIMapper<T> implements Mapper<T>
{
    /**
     * An object that holds context variables for each mapping operation, with the purpose to
     * secure thread-safety for this mapper.
     */
    static class Context
    {
        String currentLine;
        String currentSectionName;
        String currentKey;
        int currentLineNumber;
    }

    private static final char TOKEN_COMMENT_START = ';';
    private static final char TOKEN_COMMENT_START_ALT = '#';
    private static final char TOKEN_SECTION_NAME_START = '[';
    private static final char TOKEN_SECTION_NAME_END = ']';
    private static final String TOKEN_KEY_VALUE_DELIMITER = "=";
    private static final String MSG_MALFORMED_INI = "Malformed INI: expected %s at line %s: \"%s\"";
    private static final String ARG_SECTION_NAME = "section name";
    private static final String ARG_PROPERTY_KEY = "property key";
    private static final String ARG_PROPERTY = "property";
    private static final String ARG_TOKEN = "token '%s'";

    /**
     * A template method that defines the skeleton of the INI source parsing operation and
     * delegates the final output mapping behavior to its concrete implementations.
     *
     * @param inputStream the {@code InputStream} to be read
     * @return the parsed object
     * @throws IOException if a low-level I/O problem (such and unexpected end-of-input, or
     *                     network error) occurs while reading from the {@code InputStream}
     */
    final Object doApply(InputStream inputStream) throws IOException
    {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        Context context = new Context();
        context.currentLine = reader.readLine();
        context.currentLineNumber = 1;
        context.currentSectionName = null;

        Object out = newObject(context);
        Object currentSection = out; // Let the final out be the initial section
        boolean skipSection = false;

        while (context.currentLine != null)
        {
            context.currentLine = context.currentLine.trim();

            if (context.currentLine.isEmpty() || isCommentLine(context.currentLine))
            {
                // Ignore empty and comment lines
            }
            else if (isSectionLine(context.currentLine))
            {
                skipSection = false;
                context.currentSectionName = parseSectionName(context);
                currentSection = newObject(context); // the current session, from now on
                if (currentSection == null)
                {
                    skipSection = true; // flag to skip next properties until the next section declaration
                }
                else
                {
                    put(out, context.currentSectionName, currentSection);
                }
            }
            else if (isPropertyLine(context.currentLine))
            {
                if (!skipSection)
                {
                    context.currentKey = parseKey(context);
                    Object value = parseValue(context);
                    put(currentSection, context.currentKey, value);
                }
                // Do not process any property until the next section declaration
            }
            else
            {
                throw malformedIniException(context, ARG_PROPERTY);
            }

            context.currentLine = reader.readLine();
            context.currentLineNumber++;
        }
        return out;
    }

    /**
     * @return {@code true} if the current line starts with one of the comment tokens
     */
    private static final boolean isCommentLine(String line)
    {
        char firstCharacter = line.charAt(0);
        return firstCharacter == TOKEN_COMMENT_START || firstCharacter == TOKEN_COMMENT_START_ALT;
    }

    /**
     * @return {@code true} if the current line starts with {@link #TOKEN_SECTION_NAME_START}
     */
    private static final boolean isSectionLine(String line)
    {
        return line.charAt(0) == TOKEN_SECTION_NAME_START;
    }

    /**
     * Parses the section name.
     *
     * @param context           the {@link Context}
     * @param line              the line to be parsed (cannot be null)
     * @param currentLineNumber the line number for due exception reporting
     * @return the section name
     */
    private static final String parseSectionName(Context context)
    {
        int sectionNameDelimiterIndex = context.currentLine.indexOf(TOKEN_SECTION_NAME_END);
        if (sectionNameDelimiterIndex < 0)
        {
            throw malformedIniException(context, String.format(ARG_TOKEN, TOKEN_SECTION_NAME_END));
        }
        String name = context.currentLine.substring(1, sectionNameDelimiterIndex);
        if (name.isEmpty())
        {
            throw malformedIniException(context, ARG_SECTION_NAME);
        }
        return name;
    }

    /**
     * @return true if the current line contains the {@link #TOKEN_KEY_VALUE_DELIMITER}
     */
    private static final boolean isPropertyLine(String line)
    {
        return line.contains(TOKEN_KEY_VALUE_DELIMITER);
    }

    /**
     * @param context the {@link Context}
     * @return the key part of the current line, provided that it's a property line
     */
    private static final String parseKey(Context context)
    {
        int endIndex = context.currentLine.indexOf(TOKEN_KEY_VALUE_DELIMITER);
        String key = context.currentLine.substring(0, endIndex).trim();
        if (key.isEmpty())
        {
            throw malformedIniException(context, ARG_PROPERTY_KEY);
        }
        return key;
    }

    /**
     * @param context the {@link Context}
     * @return the value part of the current line, provided that it's a propertly line
     */
    private final Object parseValue(Context context)
    {
        int beginIndex = context.currentLine.indexOf(TOKEN_KEY_VALUE_DELIMITER) + 1;
        String value = context.currentLine.substring(beginIndex).trim();
        return parseValue(context, value);
    }

    /**
     * Create a new {@link ConfigurationSourceException} with a formatted message, containing
     * also the current line text and number.
     *
     * @param context  the {@link Context}
     * @param expected a description of what was expected
     * @return a new {@link ConfigurationSourceException} with a formatted message
     */
    private static final ConfigurationSourceException malformedIniException(Context context, String expected)
    {
        return new ConfigurationSourceException(MSG_MALFORMED_INI, expected, context.currentLineNumber,
                context.currentLine);
    }

    /**
     * Creates a new container object that can accept names and values parsed from the source.
     *
     * @param context the {@link Context}
     * @return an object that can represent either the final document or a specific section
     */
    abstract Object newObject(Context context);

    /**
     * Parse the specified text value into Java Object.
     *
     * @param context the {@link Context}
     * @param value   the value to be parsed
     * @return the parsed Java Object
     */
    abstract Object parseValue(Context context, String value);

    /**
     * Associates the specified value with the specified name in the specified target object.
     *
     * @param target the object in which the specified name and value shall be set
     * @param name   the name to be set
     * @param value  the value to be set
     */
    abstract void put(Object target, String name, Object value);

}
