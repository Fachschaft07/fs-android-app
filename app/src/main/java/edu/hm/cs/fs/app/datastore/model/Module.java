package edu.hm.cs.fs.app.datastore.model;

import java.util.List;
import java.util.Locale;

import edu.hm.cs.fs.app.datastore.model.constants.Study;
import edu.hm.cs.fs.app.datastore.model.constants.TeachingForm;

public interface Module {
	String getName();

	int getCredits();

	int getSemesterWeekHours();

	Person getResponsible();

	List<Person> getTeachers();

	List<Locale> getLanguages();

	TeachingForm getTeachingForm();

	String getExpenditure();

	String getRequirements();

	String getGoals();

	String getContent();

	String getMedia();

	String getLiterature();

	Study getProgram();

	List<ModuleCode> getModulCodes();
}
