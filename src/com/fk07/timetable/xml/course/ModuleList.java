package com.fk07.timetable.xml.course;

import java.util.List;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;


@Root(name = "modullist", strict = false)
public class ModuleList {
	@ElementList(entry = "modul", inline = true)
	private List<Module> modules;

	public List<Module> getModules() {
		return this.modules;
	}

	public void setModules(final List<Module> modules) {
		this.modules = modules;
	}
}
