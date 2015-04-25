package edu.hm.cs.fs.app.datastore.model.constants;

import java.util.Calendar;

/**
 * @author Fabio
 */
public enum Day {
    /** Montag */
    MONDAY("montag", Calendar.MONDAY),
    /** Dienstag */
    TUESDAY("dienstag", Calendar.TUESDAY),
    /** Mittwoch */
    WEDNESDAY("mittwoch", Calendar.WEDNESDAY),
    /** Donnerstag */
    THURSDAY("donnerstag", Calendar.THURSDAY),
    /** Freitag */
    FRIDAY("freitag", Calendar.FRIDAY),
    /** Samstag */
    SATURDAY("samstag", Calendar.SATURDAY),
    /** Sonntag */
    SUNDAY("sonntag", Calendar.SUNDAY);

    private final String mKey;

    private final int id;

    private Day(final String key, int id) {
        mKey = key;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return mKey;
    }

    /**
     * @param key
     * @return
     */
    public static Day of(final String key) {
        for (final Day day : values()) {
            if (day.toString().equalsIgnoreCase(key) ||
                    day.toString().substring(0, 2).equalsIgnoreCase(key)) {
                return day;
            }
        }
        throw new IllegalArgumentException(
                "Argument can not be converted into a day: " + key);
    }

    public static Day byCalendar(final int dayOfWeek) {
        for (final Day day : values()) {
            if (day.getId() == dayOfWeek) {
                return day;
            }
        }
        throw new IllegalArgumentException(
                "Argument can not be converted into a day: " + dayOfWeek);
    }
}
