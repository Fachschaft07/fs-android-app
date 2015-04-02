package edu.hm.cs.fs.app.datastore.model.constants;

import java.util.Calendar;
import java.util.Locale;

/**
 * @author Fabio
 * 
 */
public enum Time {
	/** 08:15 - 9:45 */
	LESSON_1(8, 15),
	/** 10:00 - 11:30 */
	LESSON_2(10, 0),
	/** 11:45 - 13:15 */
	LESSON_3(11, 45),
	/** 13:30 - 15:00 */
	LESSON_4(13, 30),
	/** 15:15 - 16:45 */
	LESSON_5(15, 15),
	/** 17:00 - 18:30 */
	LESSON_6(17, 0),
	/** 18:45 - 20:15 */
	LESSON_7(18, 45);

	private final int hour;
	private final int minute;

	private Time(final int hour, final int minute) {
		this.hour = hour;
		this.minute = minute;
	}

	/**
	 * @return the start hour.
	 */
	public int getHour() {
		return hour;
	}

	/**
	 * @return the start minute.
	 */
    public int getMinute() {
		return minute;
	}

	/**
	 * @return the start.
	 */
	public Calendar getStart() {
		final Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, hour);
		cal.set(Calendar.MINUTE, minute);
		return cal;
	}

	/**
	 * @return the end.
	 */
	public Calendar getEnd() {
		final Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, hour);
		cal.set(Calendar.MINUTE, minute);
		cal.add(Calendar.MINUTE, 90);
		return cal;
	}

    @Override
    public String toString() {
        return String.format(Locale.getDefault(), "%1$02d:%2$02d", hour, minute);
    }

    /**
	 * @param timeString
	 * @return
	 */
	public static Time of(final String timeString) {
		for (final Time time : values()) {
			if (time.toString().equals(timeString)) {
				return time;
			}
		}
		throw new IllegalArgumentException("Not a valid time form: "
				+ timeString);
	}
	
	public static Time getCurrentTime() {
		Calendar current = Calendar.getInstance();
		for (Time time : values()) {
			if(time.getStart().before(current) && time.getEnd().after(current)) {
				return time;
			}
		}
		return Time.LESSON_1;
	}
}
