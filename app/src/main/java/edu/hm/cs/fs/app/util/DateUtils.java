package edu.hm.cs.fs.app.util;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by FHellman on 12.01.2016.
 */
public class DateUtils {
    public static boolean isToday(Date date) {
        Calendar today = Calendar.getInstance();

        Calendar calMeal = Calendar.getInstance();
        calMeal.setTime(date);

        return today.get(Calendar.YEAR) == calMeal.get(Calendar.YEAR) && today.get(Calendar.MONTH) == calMeal.get(Calendar.MONTH) && today.get(Calendar.DAY_OF_MONTH) == calMeal.get(Calendar.DAY_OF_MONTH);
    }
}
