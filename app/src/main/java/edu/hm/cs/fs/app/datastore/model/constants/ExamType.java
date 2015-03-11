package edu.hm.cs.fs.app.datastore.model.constants;

/**
 * @author Fabio
 *
 */
public enum ExamType {
	/** schriftliche Prüfung - 60 Minuten */
	WRITTEN_EXAMINATION_60("sp60"),
	/** schriftliche Prüfung - 90 Minuten */
	WRITTEN_EXAMINATION_90("sp90"),
	/** mündliche Prüfung */
	ORAL_EXAMINATION("mp"),
	/** praktische Prüfung */
	PRACTICAL_EXAMINATION("pl"),
	/** Referat */
	SEMINAR_PAPER("ref"),
	/** benotete Studienarbeit */
	GRADED_THESIS("bsta"),
	/** Seminararbeit */
	ESSAY("sema"),
	/** unbenotetes Kolloquium */
	UNGRADED_COLLOQUIUM("coll"),
	/** benotetes Kolloquium */
	GRADED_COLLOQUIUM("bcoll"),
	/** Bericht */
	REPORT("rep");

	private final String mKey;

	private ExamType(final String key) {
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
	public static ExamType of(final String key) {
		for (final ExamType type : values()) {
			if (type.toString().equalsIgnoreCase(key)) {
				return type;
			}
		}
		throw new IllegalArgumentException("The type is not valid: " + key);
	}
}
