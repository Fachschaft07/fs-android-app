package edu.hm.cs.fs.app.ui.timetable.xml.timetablefk10;

public class Lecture {
	private String time;
	private String name;
	private String room;
	private String teacher;

	public Lecture(final String time) {
		this.time = time;
	}

	public String getTime() {
		return this.time;
	}

	public void setTime(final String time) {
		this.time = time;
	}

	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public String getRoom() {
		return this.room;
	}

	public void setRoom(final String room) {
		this.room = room;
	}

	public String getTeacher() {
		return this.teacher;
	}

	public void setTeacher(final String teacher) {
		this.teacher = teacher;
	}

	@Override
	public String toString() {
		final StringBuffer result = new StringBuffer();
		result.append("Lecture[");
		result.append("time=");
		result.append(this.time);
		result.append(",name=");
		result.append(this.name);
		result.append(",room=");
		result.append(this.room);
		result.append(",teacher=");
		result.append(this.teacher);
		result.append("]");
		return result.toString();
	}
}
