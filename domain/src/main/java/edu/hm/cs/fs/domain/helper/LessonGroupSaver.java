package edu.hm.cs.fs.domain.helper;

import edu.hm.cs.fs.common.model.LessonGroup;

public class LessonGroupSaver {
    private LessonGroup mLessonGroup;
    private int mSelectedPk;
    private boolean manuelAdded;

    public LessonGroupSaver(LessonGroup lessonGroup, int pk) {
        mLessonGroup = lessonGroup;
        mSelectedPk = pk;
    }

    public LessonGroup getLessonGroup() {
        return mLessonGroup;
    }

    public void setLessonGroup(LessonGroup mLessonGroup) {
        this.mLessonGroup = mLessonGroup;
    }

    public int getSelectedPk() {
        return mSelectedPk;
    }

    public void setSelectedPk(int mSelectedPk) {
        this.mSelectedPk = mSelectedPk;
    }

    public boolean isManuelAdded() {
        return manuelAdded;
    }

    public void setManuelAdded(boolean manuelAdded) {
        this.manuelAdded = manuelAdded;
    }

    public boolean hasPk() {
        return mSelectedPk != -1;
    }
}
