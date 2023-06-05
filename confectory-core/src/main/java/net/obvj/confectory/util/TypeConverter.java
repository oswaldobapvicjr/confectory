/*
 * Copyright 2023 obvj.net
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

/**
 * This interface defines the contract for classes that know how to convert a String into
 * some domain object.
 *
 * @param <T> the type of the object that is the result of the conversion.
 *
 * @since 2.5.0
 * @author oswaldo.bapvic.jr
 */
public interface TypeConverter<T>
{
    /**
     * Converts the specified command line argument value to some domain object.
     *
     * @param value the command line argument String value
     * @return the resulting domain object
     *
     * @throws ParseException when type conversion fails, to have more control over the error
     *                        message that is logged
     */
    T convert(String value) throws ParseException;
}
