package edu.hm.cs.fs.app.datastore.model;

public interface Course {
	Module getModule();

	Group getGroup();

	Person getTeacher();
}
