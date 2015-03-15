package edu.hm.cs.fs.app.datastore.model.impl;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Fabio on 18.02.2015.
 */
public class PublicTransportImpl extends RealmObject {
    @PrimaryKey
    private String id;
	private String line;
	private String destination;
	private Date departure;

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }
	
	public String getLine() {
		return line;
	}
	
	public void setLine(String line) {
		this.line = line;
	}
	
	public String getDestination() {
		return destination;
	}
	
	public void setDestination(String destination) {
		this.destination = destination;
	}
	
	public Date getDeparture() {
		return departure;
	}
	
	public void setDeparture(Date departure) {
		this.departure = departure;
	}
}
