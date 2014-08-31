package com.fk07.timetable.xml.groups;

import java.io.Serializable;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "group", strict = false)
public class FK07Group implements Serializable {

	private static final long serialVersionUID = 2044164497752462426L;

	@Element(required = false)
	private String program;
	
	@Element(required = false)
	private String semester;
	
	@Element(required = false)
	private String part;

	public String getProgram() {
		return program;
	}

	public void setProgram(String program) {
		this.program = program;
	}

	public String getSemester() {
		return semester;
	}

	public void setSemester(String semester) {
		this.semester = semester;
	}

	public String getPart() {
		return part;
	}

	public void setPart(String part) {
		this.part = part;
	}
	
	@Override
	public String toString() {
		String programString = (program == null)? "" : program.toUpperCase();
		String semesterString = (semester == null)? "" : semester;
		String partString = (part == null)? "" : part.toUpperCase();
		return programString + " " + semesterString + " " + partString;
	}

	public String getDownloadTag() {
		String programString = (program == null)? "" : program;
		String semesterString = (semester == null)? "" : semester;
		String partString = (part == null)? "" : part;
		String downloadTag = programString + semesterString + partString;
		return downloadTag.toLowerCase();
	}
}
