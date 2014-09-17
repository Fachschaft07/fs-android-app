package com.fk07.backend.web.data.constants;

import android.annotation.SuppressLint;

/**
 * @author Fabio
 *
 */
public enum Day {
	MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY;

	@SuppressLint("DefaultLocale")
	public static Day getDayByName(final String name) {
		final String dayCutOff = name.substring(0, 1).toLowerCase();
		if ("mo".equals(dayCutOff)) {
			return Day.MONDAY;
		} else if ("di".equals(dayCutOff)) {
			return Day.TUESDAY;
		} else if ("mi".equals(dayCutOff)) {
			return Day.WEDNESDAY;
		} else if ("do".equals(dayCutOff)) {
			return Day.THURSDAY;
		} else if ("fr".equals(dayCutOff)) {
			return Day.FRIDAY;
		} else if ("sa".equals(dayCutOff)) {
			return Day.SATURDAY;
		}
		return Day.SUNDAY;
	}
}
