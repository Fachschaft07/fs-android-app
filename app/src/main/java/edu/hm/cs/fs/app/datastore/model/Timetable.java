package edu.hm.cs.fs.app.datastore.model;

import java.util.List;

import edu.hm.cs.fs.app.datastore.model.constants.Day;

public interface Timetable {
	void save();
	
	void delete();

	List<Lesson> getLessons(Day day);
	
	void addLesson(Lesson lesson);
	
	void removeLesson(Lesson lesson);
}
