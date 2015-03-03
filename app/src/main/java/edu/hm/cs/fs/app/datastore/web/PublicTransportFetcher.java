package edu.hm.cs.fs.app.datastore.web;

import android.content.Context;

import org.json.JSONArray;

import edu.hm.cs.fs.app.datastore.model.impl.PublicTransport;
import edu.hm.cs.fs.app.datastore.web.fetcher.AbstractHtmlFetcher;
import edu.hm.cs.fs.app.util.FileUtils;

/**
 * The PublicTransport stores the data for the mvv (bus, tram, etc.) witch the specified line, destination and
 * departure.
 * 
 * @author Fabio
 * @version 2
 */
public class PublicTransportFetcher {
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
	public PublicTransportFetcher(final int line, final String destination, final int departureTimeInMinutes) {
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

	public static class Builder extends AbstractHtmlFetcher<Builder, PublicTransport> {
		public static final int TYPE_PASING = 0;
		public static final int TYPE_LOTHSTR = 1;
		private static final String MVV_LOTHSTR = "http://www.mvg-live.de/ims/dfiStaticAnzeige.svc?haltestelle=Hochschule+M%fcnchen+%28Lothstra%dfe%29&tram=checked";
		private static final String MVV_PASING = "http://www.mvg-live.de/ims/dfiStaticAnzeige.svc?haltestelle=Avenariusplatz&bus=checked";

		public Builder(final Context context, int type) {
			super(context, FileUtils.createFile("mvv.json"), type == TYPE_PASING ? MVV_PASING : MVV_LOTHSTR);
		}

		@Override
		protected JSONArray read(final String url) {
			return null;
		}
	}
}
