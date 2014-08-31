package com.fk07.backend.data;

/**
 * The PublicTransport stores the data for the mvv (bus, tram, etc.) witch the specified line, destination and
 * departure.
 * 
 * @author Fabio
 * @version 2
 */
public class PublicTransport {
	private final int line;
	private final String destination;
	private final int departureTimeInMinutes;

	/**
	 * Creates a new pubic transport.
	 * 
	 * @param line
	 *            is the number of the bus, tram, etc.
	 * @param destination
	 *            is the place where the last stop is.
	 * @param departureTimeInMinutes
	 *            is the departure time.
	 */
	public PublicTransport(final int line, final String destination, final int departureTimeInMinutes) {
		this.line = line;
		this.destination = destination;
		this.departureTimeInMinutes = departureTimeInMinutes;
	}

	/**
	 * Get the linenummber.
	 * 
	 * @return the line.
	 */
	public int getLineNumber() {
		return line;
	}

	/**
	 * Get the destination.
	 * 
	 * @return the destination.
	 */
	public String getDestination() {
		return destination;
	}

	/**
	 * Get the departure time in minutes.
	 * 
	 * @return the departure time.
	 */
	public int getDepartureTime() {
		return departureTimeInMinutes;
	}
}
