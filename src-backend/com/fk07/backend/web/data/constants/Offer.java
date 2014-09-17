package com.fk07.backend.web.data.constants;

/**
 * @author Fabio
 *
 */
public enum Offer {
	/** Einzeln */
	SINGLE("einzeln"),
	/** Wechsel */
	SWITCHED("wechsel");

	private final String mKey;

	private Offer(final String key) {
		this.mKey = key;
	}

	/**
	 * @return the key.
	 */
	private String getKey() {
		return mKey;
	}

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
