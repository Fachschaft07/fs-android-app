package edu.hm.cs.fs.app.datastore.model.impl;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class TimetableImpl extends RealmObject {
	@PrimaryKey
	private String studyGroup;
	private RealmList<LessonImpl> lessons;

	public String getStudyGroup() {
		return studyGroup;
	}

	public void setStudyGroup(String studyGroup) {
		this.studyGroup = studyGroup;
	}

	public RealmList<LessonImpl> getLessons() {
		return lessons;
	}

	public void setLessons(RealmList<LessonImpl> lessons) {
		this.lessons = lessons;
	}
}
