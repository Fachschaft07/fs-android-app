package edu.hm.cs.fs.app.datastore.web;

import java.util.Date;

/**
 * The meal stores the data for a meal with the name of the meal and the date.
 * 
 * @author Fabio
 * @version 2
 */
public class MealFetcher {
	private final Date date;
	private final String description;

	/**
	 * Creates a new meal.
	 * 
	 * @param date
	 *            when the meal is offered in the mensa.
	 * @param description
	 *            of the meal.
	 */
	public MealFetcher(final Date date, final String description) {
		this.date = date;
		this.description = description;
	}

	/**
	 * The date when the mensa offers this meal.
	 * 
	 * @return the date.
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * The description of the meal.
	 * 
	 * @return the description.
	 */
	public String getDescription() {
		return description;
	}
}
