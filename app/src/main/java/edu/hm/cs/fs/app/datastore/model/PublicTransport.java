package edu.hm.cs.fs.app.datastore.model;

import java.util.Date;

public interface PublicTransport {
	int getLine();
	
	String getDestination();
	
	Date getDeparture();
}
