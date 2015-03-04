package edu.hm.cs.fs.app.datastore.model;

import java.util.List;

import edu.hm.cs.fs.app.datastore.model.constants.ExamGroup;
import edu.hm.cs.fs.app.datastore.model.constants.ExamType;
import edu.hm.cs.fs.app.datastore.model.constants.Study;

public interface Exam {
	String getCode();

	Study getGroup();

	Module getModule();

	String getSubtitle();

	List<Study> getReferences();

	List<Person> getExaminers();

	ExamType getType();

	String getMaterial();

	ExamGroup getAllocation();
}
