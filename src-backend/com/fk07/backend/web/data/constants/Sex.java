package com.fk07.backend.web.data.constants;

/**
 * @author Fabio
 *
 */
public enum Sex {
	/** Männlich */
	MALE("m"),
	/** Weiblich */
	FEMALE("w");

	private final String mKey;

	private Sex(final String key) {
		mKey = key;
	}

	/**
	 * @return the key.
	 */
	private String getKey() {
		return mKey;
	}

	/**
	 * @param key
	 * @return
	 */
	public static Sex of(final String key) {
		for (final Sex sex : values()) {
			if (sex.getKey().equalsIgnoreCase(key)) {
				return sex;
			}
		}
		throw new IllegalArgumentException("Unable to convert this '" + key
				+ "' to a sex");
	}
}
