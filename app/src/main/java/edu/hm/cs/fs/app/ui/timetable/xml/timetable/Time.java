package edu.hm.cs.fs.app.ui.timetable.xml.timetable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root(name="time", strict=false)
public class Time implements Serializable {

	private static final long serialVersionUID = -1045122701896954839L;

	private int currentEntry = 0;
	
	@ElementList(inline=true)
	private List<Entry> entry;
	
	public List<Entry> getEntry() {
		return entry;
	}
	
	public void setCurrentEntry(final int currentEntry) {
		this.currentEntry = currentEntry;
	}
	
	public int getCurrentEntry() {
		return currentEntry;
	}
	
	public void setEntry(final List<Entry> newEntry) {
		this.entry = newEntry;
	}
	
	public void init() {
		entry = new ArrayList<Entry>();
	}
}
