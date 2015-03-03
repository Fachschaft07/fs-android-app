package edu.hm.cs.fs.app.datastore.model.constants;

/**
 * @author Fabio
 *
 */
public enum Offer {
	/** Einzeln */
	SINGLE("einzeln"),
	/** Wechsel */
	SWITCHED("wechsel"),
	/** Sommersemester */
	SUMMER_SEMESTER("ss"),
	/** Wintersemester */
	WINTER_SEMESTER("ws"),
	/** Alle */
	ALL("alle");

	private final String mKey;

	private Offer(final String key) {
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
	public static Offer of(final String key) {
		for (final Offer offer : values()) {
			if (offer.getKey().equalsIgnoreCase(key)) {
				return offer;
			}
		}
		throw new IllegalArgumentException(
				"Can not be translated to an offer: " + key);
	}
}
