package com.fk07.timetable.xml.timetable;

import java.io.Serializable;
import java.util.List;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root(strict=false)
public class Entry implements Serializable {
	
	private static final long serialVersionUID = 3532522392838271741L;

	@Element(required=false)
	private String startTime;
	
	@Element(required=false)
	private String stopTime;
	
	@Element(required=false)
	private String type;
	
	@Element(required=false)
	private String title;
	
	@Element(required=false)
	private String suffix;
	
	@ElementList(inline=true, entry="teacher", required=false)
	private List<String> teacher;
	
	@Element(required=false)
	private String group;
	
	@ElementList(inline=true, entry="room", required=false)
	private List<String> room;
	
	private String day;
	
	private String time;
	
	public String getStartTime() {
		return startTime;
	}

	public String getStopTime() {
		return stopTime;
	}

	public String getType() {
		return type;
	}

	public String getTitle() {
		return title;
	}

	public String getSuffix() {
		return suffix;
	}
	
	public List<String> getTeacher() {
		return teacher;
	}

	public String getGroup() {
		return group;
	}

	public String getRoom() {
		String roomString = "";
		if (room != null && room.size() > 0) {
			roomString = room.get(0);
			if (roomString.matches("[r0-9.0-90-90-9]+")) {
				roomString = roomString.substring(0, 2) + "." + roomString.substring(2);
				if (room.size() > 1) {
					for (int i = 1; i < room.size(); i++) {
						String current = room.get(i);
						roomString += "/" + current.substring(0, 2) + "." + current.substring(2);
					}
				}
			}
		}
		return roomString.toUpperCase();
	}
	
	public String getDay() {
		return day;
	}
	
	public String getTime() {
		return time;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public void setStopTime(String stopTime) {
		this.stopTime = stopTime;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	public void setTeacher(List<String> teacher) {
		this.teacher = teacher;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public void setRoom(List<String> room) {
		this.room = room;
	}
	
	public void setDay(final String day) {
		this.day = day;
	}
	
	public void setTime(final String time) {
		this.time = time;
	}
	
	public void delete() {
		type = "filler";
	}
}