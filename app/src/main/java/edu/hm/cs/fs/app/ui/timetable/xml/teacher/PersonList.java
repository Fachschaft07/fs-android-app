package edu.hm.cs.fs.app.ui.timetable.xml.teacher;

import java.util.List;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root(name = "persons", strict = false)
public class PersonList {
	@ElementList(entry = "person", inline = true, required = false)
	private List<Person> persons;

	public List<Person> getPersons() {
		return this.persons;
	}

	public void setPersons(final List<Person> persons) {
		this.persons = persons;
	}
}
