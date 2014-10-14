package com.fk07.backend.web.data.constants;

/**
 * @author Fabio
 *
 */
public enum Day {
	/** Montag */
	MONDAY("mo", 2),
	/** Dienstag */
	TUESDAY("di", 3),
	/** Mittwoch */
	WEDNESDAY("mi", 4),
	/** Donnerstag */
	THURSDAY("do", 5),
	/** Freitag */
	FRIDAY("fr", 6),
	/** Samstag */
	SATURDAY("sa", 7),
	/** Sonntag */
	SUNDAY("so", 1);

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
