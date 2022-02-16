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
 * valid INI {@code Source} (e.g.: file, URL), and delegates the final object type
 * population to a concrete implementation.
 *
 * @author oswaldo.bapvic.jr (Oswaldo Junior)
 * @since 1.3.0
 */
public abstract class AbstractINIMapper<T> implements Mapper<T>
{
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

    protected String currentLine;
    protected String currentSectionName;
    protected Object currentSection;
    protected String currentKey;
    protected int currentLineNumber = 0;

    /**
     * A template method that defines the skeleton of the INI source parsing operation and
     * delegates the final output mapping behavior to its concrete implementations.
     *
     * @param inputStream the {@code InputStream} to be read
     * @return the parsed object
     * @throws IOException if a low-level I/O problem (such and unexpected end-of-input, or
     *                     network error) occurs while reading from the {@code InputStream}
     */
    protected Object doApply(InputStream inputStream) throws IOException
    {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        currentLine = reader.readLine();
        currentLineNumber = 1;
        currentSectionName = null;

        Object out = newObject();
        currentSection = out; // Let the final out be the initial section
        boolean skipSection = false;

        while (currentLine != null)
        {
            currentLine = currentLine.trim();

            if (currentLine.isEmpty() || isCommentLine())
            {
                // Ignore empty and comment lines
            }
            else if (isSectionLine())
            {
                skipSection = false;
                currentSectionName = parseSectionName();
                currentSection = newObject(); // the current session, from now on
                if (currentSection == null)
                {
                    skipSection = true; // flag to skip next properties until the next section declaration
                }
                else
                {
                    put(out, currentSectionName, currentSection);
                }
            }
            else if (isPropertyLine())
            {
                if (!skipSection)
                {
                    currentKey = parseKey();
                    Object value = parseValue();
                    put(currentSection, currentKey, value);
                }
                // Do not process any property until the next section declaration
            }
            else
            {
                throw malformedIniException(ARG_PROPERTY);
            }

            currentLine = reader.readLine();
            currentLineNumber++;
        }
        return out;
    }

    /**
     * @return {@code true} if the current line starts with one of the comment tokens
     */
    private boolean isCommentLine()
    {
        char firstCharacter = currentLine.charAt(0);
        return firstCharacter == TOKEN_COMMENT_START || firstCharacter == TOKEN_COMMENT_START_ALT;
    }

    /**
     * @return {@code true} if the current line starts with {@link #TOKEN_SECTION_NAME_START}
     */
    private boolean isSectionLine()
    {
        return currentLine.charAt(0) == TOKEN_SECTION_NAME_START;
    }

    /**
     * Parses the section name.
     *
     * @param line   the line to be parsed (cannot be null)
     * @param currentLineNumber the line number for due exception reporting
     * @return the section name
     */
    private String parseSectionName()
    {
        int sectionNameDelimiterIndex = currentLine.indexOf(TOKEN_SECTION_NAME_END);
        if (sectionNameDelimiterIndex < 0)
        {
            throw malformedIniException(String.format(ARG_TOKEN, TOKEN_SECTION_NAME_END));
        }
        String name = currentLine.substring(1, sectionNameDelimiterIndex);
        if (name.isEmpty())
        {
            throw malformedIniException(ARG_SECTION_NAME);
        }
        return name;
    }

    /**
     * @return true if the current line contains the {@link #TOKEN_KEY_VALUE_DELIMITER}
     */
    private boolean isPropertyLine()
    {
        return currentLine.contains(TOKEN_KEY_VALUE_DELIMITER);
    }

    /**
     * @return the key part of the current line, provided that it's a property line
     */
    private String parseKey()
    {
        String key = currentLine.substring(0, currentLine.indexOf(TOKEN_KEY_VALUE_DELIMITER)).trim();
        if (key.isEmpty())
        {
            throw malformedIniException(ARG_PROPERTY_KEY);
        }
        return key;
    }

    /**
     * @return the value part of the current line, provided that it's a propertly line
     */
    private Object parseValue()
    {
        String value = currentLine.substring(currentLine.indexOf(TOKEN_KEY_VALUE_DELIMITER) + 1).trim();
        return doParseValue(value);
    }

    /**
     * Create a new {@link ConfigurationSourceException} with a formatted message, containing
     * also the current line text and number.
     *
     * @param expected a description of what was expected
     * @return a new {@link ConfigurationSourceException} with a formatted message
     */
    private ConfigurationSourceException malformedIniException(String expected)
    {
        return new ConfigurationSourceException(MSG_MALFORMED_INI, expected, currentLineNumber, currentLine);
    }

    /**
     * Creates a new container object that can accept names and values parsed from the source.
     *
     * @return an object that can represent either the final document or a specific section
     */
    abstract Object newObject();

    /**
     * Parse the specified text value into Java Object.
     *
     * @param value the value to be parsed
     * @return the parsed Java Object
     */
    abstract Object doParseValue(String value);

    /**
     * Associates the specified value with the specified name in the specified target object.
     *
     * @param target the object in which the specified name and value shall be set
     * @param name   the name to be set
     * @param value  the value to be set
     */
    abstract void put(Object target, String name, Object value);

}
