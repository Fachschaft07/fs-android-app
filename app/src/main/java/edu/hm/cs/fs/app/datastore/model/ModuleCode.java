package edu.hm.cs.fs.app.datastore.model;

import java.util.List;

import edu.hm.cs.fs.app.datastore.model.constants.Offer;
import edu.hm.cs.fs.app.datastore.model.constants.Semester;

public interface ModuleCode {
	String getRegulation();

	Offer getOffer();

	String getServices();

	String getCode();

	List<Semester> getSemester();

	String getCurriculum();
}
