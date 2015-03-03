package edu.hm.cs.fs.app.ui.timetable.xml.course;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "modul", strict = false)
public class Module {
	@Element(required = false)
	private String name;

	@Element(name = "modulcodes", required = false)
	private ModuleCodes modulecodes;

	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public ModuleCodes getModulecodes() {
		return this.modulecodes;
	}

	public void setModulecodes(final ModuleCodes modulecodes) {
		this.modulecodes = modulecodes;
	}
}
