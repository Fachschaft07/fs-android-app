package com.fk07.timetable.xml.timetablefk10;

import java.util.ArrayList;
import java.util.List;

public class FK10Day {
	
	private final String weekDay;
	private final List<Lecture> lectures;

	public FK10Day(final String weekDay) {
		this.weekDay = weekDay;
		this.lectures = new ArrayList<Lecture>();
	}

	public String getWeekDay() {
		return this.weekDay;
	}

	public List<Lecture> getLectures() {
		return this.lectures;
	}

	public void addLecture(final Lecture one) {
		this.lectures.add(one);
	}

	@Override
	public String toString() {
		final StringBuffer result = new StringBuffer();
		result.append("FK10Day[");
		result.append("weekDay=");
		result.append(this.weekDay);
		result.append(",");
		for (int i = 0; i < this.lectures.size(); i++) {
			result.append(this.lectures.get(i));
			if (i != this.lectures.size() - 1) {
				result.append(",");
			}
		}
		result.append("]");
		return result.toString();
	}
}
