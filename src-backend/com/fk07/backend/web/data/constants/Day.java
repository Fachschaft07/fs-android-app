package com.fk07.backend.web.data.constants;

/**
 * @author Fabio
 *
 */
public enum Day {
	/** Montag */
	MONDAY("mo"),
	/** Dienstag */
	TUESDAY("di"),
	/** Mittwoch */
	WEDNESDAY("mi"),
	/** Donnerstag */
	THURSDAY("do"),
	/** Freitag */
	FRIDAY("fr"),
	/** Samstag */
	SATURDAY("sa"),
	/** Sonntag */
	SUNDAY("so");

	private final String mKey;

	private Day(final String key) {
		mKey = key;
	}

	private String getKey() {
		return mKey;
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
