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

import java.util.Comparator;

import net.obvj.confectory.Configuration;

/**
 * A comparison function for {@code Configuration} objects.
 *
 * @author oswaldo.bapvic.jr (Oswaldo Junior)
 * @since 0.1.0
 */
public class ConfigurationComparator implements Comparator<Configuration<?>>
{

    /**
     * Compares two {@code Configuration} objects for precedence. Returns a negative integer,
     * zero, or a positive integer as the first {@code Configuration} has higher, equal, or
     * lower precedence than the second.
     *
     * @param first  the first {@code Configuration} to be compared
     * @param second the second {@code Configuration} to be compared
     * @return a negative integer if the first {@code Configuration} has <b>higher</b>
     *         precedence; zero if both objects have the same precedence; or a positive
     *         integer as the first object has <b>lower</b> precedence than, equal to, or
     *         greater than the second.
     */
    @Override
    public int compare(Configuration<?> first, Configuration<?> second)
    {
        return second.getPrecedence() - first.getPrecedence(); // reversed on purpose
    }

}
