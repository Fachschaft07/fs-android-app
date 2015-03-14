package edu.hm.cs.fs.app.datastore.model;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public interface PublicTransport {
	int getLine();
	
	String getDestination();

    long getDepartureIn(TimeUnit timeUnit);
	
	Date getDeparture();

    public enum Location {
        PASING, LOTHSTR
    }
}
