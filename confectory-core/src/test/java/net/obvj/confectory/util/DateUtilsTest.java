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

import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import net.obvj.confectory.TestUtils;

/**
 * Unit tests for the {@link DateUtils}.
 *
 * @author oswaldo.bapvic.jr
 * @since 2.5.0
 */
class DateUtilsTest
{
    @BeforeAll
    public static void setup()
    {
        Locale.setDefault(Locale.UK);
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
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
        Date expectedDateMillis = TestUtils.toDateUtc(2019, 9, 7, 13, 21, 59, 987);
        Date expectedDateSeconds = TestUtils.toDateUtc(2019, 9, 7, 13, 21, 59, 0);

        assertThat(DateUtils.parseDateRfc3339("2019-09-07T13:21:59.987Z"), equalTo(expectedDateMillis));
        assertThat(DateUtils.parseDateRfc3339("2019-09-07 13:21:59.987Z"), equalTo(expectedDateMillis));
        assertThat(DateUtils.parseDateRfc3339("2019-09-07 13:21:59Z"),     equalTo(expectedDateSeconds));

        assertThat(DateUtils.parseDateRfc3339("2019-09-07 10:21:59-03:00"), equalTo(expectedDateSeconds));
        assertThat(DateUtils.parseDateRfc3339("2019-09-07T14:21:59+01:00"), equalTo(expectedDateSeconds));

        assertThat(DateUtils.parseDateRfc3339("2019-09-07T13:21:59.987+00:00"),  equalTo(expectedDateMillis));
        assertThat(DateUtils.parseDateRfc3339("2019-09-07T13:21:59.987654321Z"), equalTo(expectedDateMillis));
    }

}
