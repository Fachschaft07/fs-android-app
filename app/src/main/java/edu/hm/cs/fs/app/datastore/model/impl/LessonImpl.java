package edu.hm.cs.fs.app.datastore.model.impl;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class LessonImpl extends RealmObject {
	@PrimaryKey
	private String id;
	private String day;
	private String time;
	private String module;
	private String teacher;
	private String room;
	private RealmList<CourseImpl> courses;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public String getTeacher() {
		return teacher;
	}

	public void setTeacher(String teacher) {
		this.teacher = teacher;
	}

	public String getRoom() {
		return room;
	}

	public void setRoom(String room) {
		this.room = room;
	}

	public RealmList<CourseImpl> getCourses() {
		return courses;
	}

	public void setCourses(RealmList<CourseImpl> courses) {
		this.courses = courses;
	}
}
