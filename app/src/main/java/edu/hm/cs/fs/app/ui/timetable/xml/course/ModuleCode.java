package edu.hm.cs.fs.app.ui.timetable.xml.course;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "modulcode", strict = false)
public class ModuleCode {
	@Element(required = false)
	private String modul;

	public String getModul() {
		return this.modul;
	}

	public void setModul(final String modul) {
		this.modul = modul;
	}
}
