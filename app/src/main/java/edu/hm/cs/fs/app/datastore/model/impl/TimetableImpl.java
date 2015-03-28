package edu.hm.cs.fs.app.datastore.model.impl;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class TimetableImpl extends RealmObject {
    @PrimaryKey
    private String id;
	private RealmList<LessonImpl> lessons;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

	public RealmList<LessonImpl> getLessons() {
		return lessons;
	}

	public void setLessons(RealmList<LessonImpl> lessons) {
		this.lessons = lessons;
	}
}
