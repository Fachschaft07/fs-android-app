package edu.hm.cs.fs.app.ui.timetable.xml.timetable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root(name="day", strict=false)
public class Day implements Serializable {
	
	private static final long serialVersionUID = 3053582110643291864L;
	
	@ElementList(inline=true)
	private List<Time> time;
	
	public List<Time> getTime() {
		return time;
	}
	
	public void init() {
		time = new ArrayList<Time>();
		for (int i = 0; i < 7; i++) {
			Time innerTime = new Time();
			innerTime.init();
			time.add(innerTime);
		}
	}
}
