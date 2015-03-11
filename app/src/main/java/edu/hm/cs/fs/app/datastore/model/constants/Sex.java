package edu.hm.cs.fs.app.datastore.model.constants;

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

    @Override
    public String toString() {
        return mKey;
    }

    /**
	 * @param key
	 * @return
	 */
	public static Sex of(final String key) {
        if("f".equalsIgnoreCase(key)) {
            return FEMALE;
        }
		for (final Sex sex : values()) {
			if (sex.toString().equalsIgnoreCase(key)) {
				return sex;
			}
		}
		throw new IllegalArgumentException("Unable to convert this '" + key
				+ "' to a sex");
	}
}
