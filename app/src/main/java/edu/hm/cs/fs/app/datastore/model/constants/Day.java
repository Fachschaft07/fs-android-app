package edu.hm.cs.fs.app.datastore.model.constants;

import java.util.Calendar;

/**
 * @author Fabio
 *
 */
public enum Day {
	/** Montag */
	MONDAY("mo", Calendar.MONDAY),
	/** Dienstag */
	TUESDAY("di", Calendar.TUESDAY),
	/** Mittwoch */
	WEDNESDAY("mi", Calendar.WEDNESDAY),
	/** Donnerstag */
	THURSDAY("do", Calendar.THURSDAY),
	/** Freitag */
	FRIDAY("fr", Calendar.FRIDAY),
	/** Samstag */
	SATURDAY("sa", Calendar.SATURDAY),
	/** Sonntag */
	SUNDAY("so", Calendar.SUNDAY);

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
			if (day.toString().equalsIgnoreCase(key)) {
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
