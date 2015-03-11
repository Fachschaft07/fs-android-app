package edu.hm.cs.fs.app.datastore.model.constants;

/**
 * @author Fabio
 *
 */
public enum PersonStatus {
	/** Professor */
	PROFESSOR("prof"),
	/** Mitarbeiter */
	EMPLOYEE("staff"),
	/** Wissenschaftlicher Mitarbeiter */
	SCIENCE_EMPLOYEE("associate"),
	/** Lehrbeauftragte */
	LECTURER("lba"),
	/** Fellow */
	FELLOW("fellow"),
	/** Emeritus */
	EMERITUS("emeritus"),
	/** Gast */
	GUEST("guest");

	private final String mKey;

	private PersonStatus(final String key) {
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
	public static PersonStatus of(final String key) {
		for (final PersonStatus state : values()) {
			if (state.toString().equalsIgnoreCase(key)) {
				return state;
			}
		}
		throw new IllegalArgumentException("This is not a valid person state: "
				+ key);
	}
}
