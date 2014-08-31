package com.fk07.timetable.xml.groups;

import java.io.Serializable;
import java.util.List;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root(name = "groups", strict = false)
public class FK07Groups implements Serializable {
	
	private static final long serialVersionUID = 1251635654549611444L;
	
	@ElementList(entry = "group", inline = true, required = false)
	private List<FK07Group> groups;

	public List<FK07Group> getGroups() {
		return this.groups;
	}

	public void setPersons(final List<FK07Group> groups) {
		this.groups = groups;
	}

	@Override
	public String toString() {
		String groupString = "";
		for (FK07Group group : groups) {
			groupString += group.toString() + "\n";
		}
		return groupString;
	}
}
