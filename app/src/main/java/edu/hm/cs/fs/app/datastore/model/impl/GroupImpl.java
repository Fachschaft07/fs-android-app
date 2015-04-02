package edu.hm.cs.fs.app.datastore.model.impl;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.hm.cs.fs.app.datastore.model.Group;
import edu.hm.cs.fs.app.datastore.model.constants.Letter;
import edu.hm.cs.fs.app.datastore.model.constants.Semester;
import edu.hm.cs.fs.app.datastore.model.constants.Study;

/**
 * @author Fabio
 */
public class GroupImpl implements Group {
    private static final Pattern PATTERN = Pattern
            .compile("([A-z]{2})([1-7]{0,1})([A-z]{0,1})");
    private final Study mStudy;
    private final Semester mSemester;
    private final Letter mLetter;

    public GroupImpl(final Study study, final Semester semester,
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
    public Semester getSemester() {
        return mSemester;
    }

    /**
     * @return the letter.
     */
    public Letter getLetter() {
        return mLetter;
    }

    @Override
    public String toString() {
        final StringBuilder strBuilder = new StringBuilder();
        strBuilder.append(getStudy().toString());
        if (getSemester() != null) {
            strBuilder.append(getSemester().getNumber());
        }
        if (getLetter() != null) {
            strBuilder.append(getLetter().toString());
        }
        return strBuilder.toString();
    }

    @Override
    public boolean equals(final Object o) {
        if (o instanceof GroupImpl) {
            GroupImpl equalObject = (GroupImpl) o;
            return toString().equals(equalObject.toString());
        }
        return super.equals(o);
    }

    /**
     * @param name
     *         of the study group (for example: "IB1A" or "IB" or "IC3").
     * @return the study group.
     */
    public static GroupImpl of(final String name) {
        final Matcher matcher = PATTERN.matcher(name);

        if (!matcher.find()) {
            return null;
        }
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
            letter = Letter.valueOf(letterMatch.toUpperCase(Locale.getDefault()));
        } else {
            letter = null;
        }

        return new GroupImpl(studyGroup, semester, letter);
    }
}
