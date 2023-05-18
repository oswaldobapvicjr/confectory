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
