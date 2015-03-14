package edu.hm.cs.fs.app.datastore.model.impl;

import java.util.Date;

import io.realm.RealmObject;

/**
 * Created by Fabio on 18.02.2015.
 */
public class PublicTransportImpl extends RealmObject {
	private int line;
	private String destination;
	private Date departure;
	
	public int getLine() {
		return line;
	}
	
	public void setLine(int line) {
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
