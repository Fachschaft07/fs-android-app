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

	public String getKey() {
		return mKey;
	}
	
	public int getId() {
		return id;
	}

	/**
	 * @param key
	 * @return
	 */
	public static Day of(final String key) {
		for (final Day day : values()) {
			if (day.getKey().equalsIgnoreCase(key)) {
				return day;
			}
		}
		throw new IllegalArgumentException(
				"Argument can not be converted into a day: " + key);
	}
}
