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

import static net.obvj.junit.utils.matchers.AdvancedMatchers.instantiationNotAllowed;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the {@link DateUtils}.
 *
 * @author oswaldo.bapvic.jr
 * @since 2.5.0
 */
class DateUtilsTest
{
    private static final String STR_UTC = "UTC";

    @BeforeAll
    public static void setup()
    {
        Locale.setDefault(Locale.UK);
        TimeZone.setDefault(TimeZone.getTimeZone(STR_UTC));
    }

    private static Date toDateUtc(int year, int month, int day, int hour, int minute, int second, int millisecond)
    {
        return toCalendarUtc(year, month, day, hour, minute, second, millisecond).getTime();
    }

    private static Calendar toCalendarUtc(int year, int month, int day, int hour, int minute, int second, int millisecond)
    {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone(STR_UTC));
        calendar.set(year, month - 1, day, hour, minute, second);
        calendar.set(Calendar.MILLISECOND, millisecond);
        return calendar;
    }

    @Test
    void constructor_instantiationNotAllowed()
    {
        assertThat(DateUtils.class,
                instantiationNotAllowed().throwing(UnsupportedOperationException.class));
    }

    @Test
    void parseDateRfc3339_validRepresentations_success()
    {
        Date expectedDateMillis = toDateUtc(2019, 9, 7, 13, 21, 59, 987);
        Date expectedDateSeconds = toDateUtc(2019, 9, 7, 13, 21, 59, 0);

        assertThat(DateUtils.parseDateRfc3339("2019-09-07T13:21:59.987Z"), equalTo(expectedDateMillis));
        assertThat(DateUtils.parseDateRfc3339("2019-09-07 13:21:59.987Z"), equalTo(expectedDateMillis));
        assertThat(DateUtils.parseDateRfc3339("2019-09-07 13:21:59Z"),     equalTo(expectedDateSeconds));

        assertThat(DateUtils.parseDateRfc3339("2019-09-07 10:21:59-03:00"), equalTo(expectedDateSeconds));
        assertThat(DateUtils.parseDateRfc3339("2019-09-07T14:21:59+01:00"), equalTo(expectedDateSeconds));

        assertThat(DateUtils.parseDateRfc3339("2019-09-07T13:21:59.987+00:00"),  equalTo(expectedDateMillis));
        assertThat(DateUtils.parseDateRfc3339("2019-09-07T13:21:59.987654321Z"), equalTo(expectedDateMillis));
    }

}
