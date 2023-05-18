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

package net.obvj.confectory;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Common methods for working with JUnit test cases.
 *
 * @author oswaldo.bapvic.jr
 * @since 2.5.0
 */
public class TestUtils
{
    public static Date toDateUtc(int year, int month, int day, int hour, int minute, int second,
            int millisecond)
    {
        return toCalendarUtc(year, month, day, hour, minute, second, millisecond).getTime();
    }

    public static Calendar toCalendarUtc(int year, int month, int day, int hour, int minute,
            int second, int millisecond)
    {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.set(year, month - 1, day, hour, minute, second);
        calendar.set(Calendar.MILLISECOND, millisecond);
        return calendar;
    }
}
