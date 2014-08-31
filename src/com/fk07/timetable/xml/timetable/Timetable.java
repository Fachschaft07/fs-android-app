package com.fk07.timetable.xml.timetable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root(name="timetable", strict=false)
public class Timetable implements Serializable {
	
	private static final long serialVersionUID = 5042130436559643246L;

	@Element
	private String value;
	
	private List<String> values = new ArrayList<String>();
	
	@ElementList(inline=true)
	private List<Day> day;
	
	public String getValue() {
		return value;
	}
	
	public List<String> getValues() {
		if (value != null && !values.contains(value)) {
			values.add(value);
		}
		return values;
	}
	
	public void setValue(final String value) {
		this.value = value;
	}
	
	public void addValue(final String value) {
		if (value != null && !values.contains(value)) {
			values.add(value);
		}
	}
	
	public List<Day> getDay() {
		return day;
	}
	
	public void setDay(List<Day> day) {
		this.day = day;
	}
	
	public void addAll(final Timetable newtable) {
		
		addValue(newtable.getValue());
		
		for (int i = 0; i < newtable.getDay().size(); i++) {
			Day newDay = newtable.getDay().get(i);
			Day thisDay = day.get(i);
			for (int j = 0; j < newDay.getTime().size(); j++) {
				Time newTime = newDay.getTime().get(j);
				Time thisTime = thisDay.getTime().get(j);
				thisTime.getEntry().addAll(newTime.getEntry());
			}
		}
	}
	
	public void init() {
		day = new ArrayList<Day>();
		for (int i = 0; i < 7; i++) {
			Day innerDay = new Day();
			innerDay.init();
			day.add(innerDay);
		}
	}
}