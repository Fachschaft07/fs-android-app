package com.fk07.backend.web.data.constants;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;

import com.google.common.base.Optional;

/**
 * @author Fabio
 *
 */
public class StudyGroup {
	private static final Pattern PATTERN = Pattern
			.compile("([A-z]{2})([1-7]{0,1})([A-z]{0,1})");
	private final Study mStudy;
	private final Semester mSemester;
	private final Letter mLetter;

	private StudyGroup(final Study study, final Semester semester,
			final Letter letter) {
		if (study == null) {
			throw new NullPointerException();
		}
		mStudy = study;
		mSemester = semester;
		mLetter = letter;
	}

	/**
	 * @return the study group.
	 */
	public Study getStudy() {
		return mStudy;
	}

	/**
	 * @return the semester.
	 */
	public Optional<Semester> getSemester() {
		return Optional.fromNullable(mSemester);
	}

	/**
	 * @return the letter.
	 */
	public Optional<Letter> getLetter() {
		return Optional.fromNullable(mLetter);
	}

	@Override
	public String toString() {
		final StringBuilder strBuilder = new StringBuilder();
		strBuilder.append(getStudy().toString());
		if (getSemester().isPresent()) {
			strBuilder.append(getSemester().get().getNumber());
		}
		if (getLetter().isPresent()) {
			strBuilder.append(getLetter().get().toString());
		}
		return strBuilder.toString();
	}

	/**
	 * @param name
	 *            of the study group (for example: "IB1A" or "IB" or "IC3").
	 * @return the study group.
	 */
	@SuppressLint("DefaultLocale")
	public static StudyGroup of(final String name) {
		final Matcher matcher = PATTERN.matcher(name);

		matcher.find(); // Muss immer vorhanden sein!
		final Study studyGroup = Study.of(matcher.group(1));

		final Semester semester;
		final String semesterMatch = matcher.group(2);
		if (semesterMatch.length() > 0) {
			semester = Semester.valueOf("_" + semesterMatch);
		} else {
			semester = null;
		}

		final Letter letter;
		final String letterMatch = matcher.group(3);
		if (letterMatch.length() > 0) {
			letter = Letter.valueOf(letterMatch.toUpperCase());
		} else {
			letter = null;
		}

		return new StudyGroup(studyGroup, semester, letter);
	}
}
