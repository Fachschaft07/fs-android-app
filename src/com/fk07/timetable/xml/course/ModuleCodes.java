package com.fk07.timetable.xml.course;

import java.util.List;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root(name = "modulcodes", strict = false)
public class ModuleCodes {
	@ElementList(entry = "modulcode", required = false, inline = true)
	private List<ModuleCode> modulecodes;

	public List<ModuleCode> getModulecodes() {
		return this.modulecodes;
	}

	public void setModulcodes(final List<ModuleCode> modulecodes) {
		this.modulecodes = modulecodes;
	}
}
