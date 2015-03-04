package edu.hm.cs.fs.app.datastore.model.impl;

/**
 * Created by Fabio on 18.02.2015.
 */
public class PublicTransportImpl {
	private int line;
	private String destination;
	private int departureTimeInMinutes;
	
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
	
	public int getDepartureTimeInMinutes() {
		return departureTimeInMinutes;
	}
	
	public void setDepartureTimeInMinutes(int departureTimeInMinutes) {
		this.departureTimeInMinutes = departureTimeInMinutes;
	}
}
