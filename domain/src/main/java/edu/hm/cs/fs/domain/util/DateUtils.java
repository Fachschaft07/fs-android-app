package edu.hm.cs.fs.domain.util;

import java.util.Calendar;
import java.util.Date;

/**
 * Everything which is necessary for {@link Date}.
 */
public final class DateUtils {
    private DateUtils() {
    }

    /**
     * Check if only the date part of the date-time is today.
     *
     * @param date to check.
     * @return <code>true</code> if the date is today.
     */
    public static boolean isToday(Date date) {
        Calendar today = Calendar.getInstance();

        Calendar calMeal = Calendar.getInstance();
        calMeal.setTime(date);

        return today.get(Calendar.YEAR) == calMeal.get(Calendar.YEAR)
                && today.get(Calendar.MONTH) == calMeal.get(Calendar.MONTH)
                && today.get(Calendar.DAY_OF_MONTH) == calMeal.get(Calendar.DAY_OF_MONTH);
    }
}
