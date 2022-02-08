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

import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;
import net.obvj.confectory.ConfigurationSourceException;
import net.obvj.confectory.helper.ConfigurationHelper;
import net.obvj.confectory.helper.JsonSmartConfigurationHelper;

/**
 * A specialized {@code Mapper} that loads the contents of a valid INI {@code Source}
 * (e.g.: file, URL) as a {@link JSONObject}.
 *
 * @author oswaldo.bapvic.jr (Oswaldo Junior)
 * @since 1.3.0
 */
public class INIToJSONObjectMapper implements Mapper<JSONObject>
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

    @Override
    public JSONObject apply(InputStream inputStream) throws IOException
    {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String currentLine = reader.readLine();
        int number = 1;

        JSONObject json = new JSONObject();
        JSONObject section = json; // Let the final JSON be the initial section

        while (currentLine != null)
        {
            currentLine = currentLine.trim();

            if (currentLine.isEmpty() || isCommentLine(currentLine))
            {
                // Ignore empty and comment lines
            }
            else if (isSectionLine(currentLine))
            {
                String name = parseSectionName(currentLine, number);
                section = new JSONObject(); // the current session, from now on
                json.put(name, section);
            }
            else if (isPropertyLine(currentLine))
            {
                String key = parseKey(currentLine, number);
                Object value = parseValue(currentLine);
                section.put(key, value);
            }
            else
            {
                throw malformedIniException(ARG_PROPERTY, number, currentLine);
            }

            currentLine = reader.readLine();
            number++;
        }
        return json;
    }

    private static boolean isCommentLine(String line)
    {
        char firstCharacter = line.charAt(0);
        return firstCharacter == TOKEN_COMMENT_START || firstCharacter == TOKEN_COMMENT_START_ALT;
    }

    private static boolean isSectionLine(String line)
    {
        return line.charAt(0) == TOKEN_SECTION_NAME_START;
    }

    /**
     * Parses the section name.
     *
     * @param line   the line to be parsed (cannot be null)
     * @param number the line number for due exception reporting
     * @return the section name
     */
    private static String parseSectionName(String line, int number)
    {
        int sectionNameDelimiterIndex = line.indexOf(TOKEN_SECTION_NAME_END);
        if (sectionNameDelimiterIndex < 0)
        {
            throw malformedIniException(String.format(ARG_TOKEN, TOKEN_SECTION_NAME_END), number, line);
        }
        String name = line.substring(1, sectionNameDelimiterIndex);
        if (name.isEmpty())
        {
            throw malformedIniException(ARG_SECTION_NAME, number, line);
        }
        return name;
    }

    private static boolean isPropertyLine(String line)
    {
        return line.contains(TOKEN_KEY_VALUE_DELIMITER);
    }

    private static String parseKey(String line, int number)
    {
        String key = line.substring(0, line.indexOf(TOKEN_KEY_VALUE_DELIMITER)).trim();
        if (key.isEmpty())
        {
            throw malformedIniException(ARG_PROPERTY_KEY, number, line);
        }
        return key;
    }

    private static Object parseValue(String line)
    {
        String value = line.substring(line.indexOf(TOKEN_KEY_VALUE_DELIMITER) + 1).trim();
        return JSONValue.parse(value); // return either null, number, boolean, or string
    }

    private static ConfigurationSourceException malformedIniException(String expected, int lineNumber, String line)
    {
        return new ConfigurationSourceException(MSG_MALFORMED_INI, expected, lineNumber, line);
    }

    @Override
    public ConfigurationHelper<JSONObject> configurationHelper(JSONObject jsonObject)
    {
        return new JsonSmartConfigurationHelper(jsonObject);
    }

}
