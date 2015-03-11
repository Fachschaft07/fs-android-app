package edu.hm.cs.fs.app.datastore.model;

import java.util.Date;

import edu.hm.cs.fs.app.datastore.model.constants.Study;

public interface Job {
	String getTitle();

	String getProvider();

	String getDescription();

	Study getProgram();

	String getContact();

	Date getExpire();

	String getUrl();
}
