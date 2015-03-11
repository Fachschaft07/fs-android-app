package edu.hm.cs.fs.app.datastore.model;

import java.util.List;

import edu.hm.cs.fs.app.datastore.model.constants.Day;
import edu.hm.cs.fs.app.datastore.model.constants.Time;

public interface Lesson {
	Day getDay();

	Time getTime();

	Module getModule();

	Person getTeacher();

	String getRoom();
	
	List<Course> getCourses();
}
