package edu.hm.cs.fs.app.datastore.model.constants;

/**
 * @author Fabio
 *
 */
public enum TeachingForm {
	/** Seminaristischer Unterricht - Praktikum */
	LECTORS_PLACEMENT("su-praktikum"),
	/** Praktikum */
	PLACEMENT("praktikum"),
	/** Projekt */
	PROJECT("projekt"),
	/** Seminar */
	SEMINAR("seminar"),
	/** Seminaristischer Unterricht - Übungen */
	LECTORS_EXERCISES("su-uebungen"),
	/** Seminaristischer Unterricht */
	LECTORS("su"),
	/** Nachfach */
	NACHFACH("nachfach"),
	/** Selbst */
	SELF("selbst");

	private final String mKey;

	private TeachingForm(final String key) {
		mKey = key;
	}

    @Override
    public String toString() {
        return mKey;
    }

    /**
	 * @param teachingForm
	 * @return
	 */
	public static TeachingForm of(final String teachingForm) {
		for (final TeachingForm teachForm : values()) {
			if (teachForm.toString().equalsIgnoreCase(teachingForm)) {
				return teachForm;
			}
		}
		throw new IllegalArgumentException("Not a valid teaching form: "
				+ teachingForm);
	}
}
