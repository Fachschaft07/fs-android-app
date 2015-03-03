package edu.hm.cs.fs.app.datastore.model.constants;

/**
 * The semesters from 1 to 10.
 *
 * @author Fabio
 *
 */
public enum Semester {
	/** 1. Semester */
	_1(1),
	/** 2. Semester */
	_2(2),
	/** 3. Semester */
	_3(3),
	/** 4. Semester */
	_4(4),
	/** 5. Semester */
	_5(5),
	/** 6. Semester */
	_6(6),
	/** 7. Semester */
	_7(7),
	/** 8. Semester */
	_8(8),
	/** 9. Semester */
	_9(9),
	/** 10. Semester */
	_10(10);

	private final int number;

	private Semester(final int number) {
		this.number = number;
	}

	/**
	 * @return the number
	 */
	public int getNumber() {
		return number;
	}

	/**
	 * @param semesterNumber
	 * @return the semester.
	 */
	public static Semester of(final int semesterNumber) {
		for (final Semester semester : values()) {
			if (semester.getNumber() == semesterNumber) {
				return semester;
			}
		}
		throw new IllegalArgumentException("Unable to convert this '"
				+ semesterNumber + "' to a semester");
	}
}
