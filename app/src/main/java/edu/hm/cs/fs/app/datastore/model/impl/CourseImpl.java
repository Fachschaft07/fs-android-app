package edu.hm.cs.fs.app.datastore.model.impl;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class CourseImpl extends RealmObject {
	@PrimaryKey
	private String id;
	private String module;
	private String group;
	private String teacher;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public String getTeacher() {
		return teacher;
	}

	public void setTeacher(String teacher) {
		this.teacher = teacher;
	}
}
