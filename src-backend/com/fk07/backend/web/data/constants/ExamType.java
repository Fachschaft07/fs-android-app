package com.fk07.backend.web.data.constants;

/**
 * @author Fabio
 *
 */
public enum ExamType {
	/** schriftliche Pr�fung - 60 Minuten */
	WRITTEN_EXAMINATION_60("sp60"),
	/** schriftliche Pr�fung - 90 Minuten */
	WRITTEN_EXAMINATION_90("sp90"),
	/** m�ndliche Pr�fung */
	ORAL_EXAMINATION("mp"),
	/** praktische Pr�fung */
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
	public static ExamType of(final String key) {
		for (final ExamType type : values()) {
			if (type.getKey().equalsIgnoreCase(key)) {
				return type;
			}
		}
		throw new IllegalArgumentException("The type is not valid: " + key);
	}
}
