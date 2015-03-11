package edu.hm.cs.fs.app.datastore.web;

import java.util.ArrayList;
import java.util.List;

import javax.xml.xpath.XPathConstants;

import android.content.Context;
import android.text.TextUtils;
import edu.hm.cs.fs.app.datastore.model.constants.Day;
import edu.hm.cs.fs.app.datastore.model.constants.Time;
import edu.hm.cs.fs.app.datastore.model.impl.LessonImpl;
import edu.hm.cs.fs.app.datastore.model.impl.TimetableImpl;
import edu.hm.cs.fs.app.datastore.web.fetcher.AbstractXmlFetcher;

public class TimetableFk07Fetcher extends AbstractXmlFetcher<TimetableFk07Fetcher, TimetableImpl> {
	private static final String URL = "http://fi.cs.hm.edu/fi/rest/public/timetable/group.xml";
	private static final String ROOT_NODE = "/list/timetable";
	
	public TimetableFk07Fetcher(Context context) {
		super(context, URL, ROOT_NODE);
	}

	@Override
	protected TimetableImpl onCreateItem(String rootPath) throws Exception {
		// TODO Fetch Timetables....
		String group;
		List<LessonImpl> lessons = new ArrayList<>();
		
		group = findByXPath(rootPath + "/value/text()",
				XPathConstants.STRING);
		final int countDays = getCountByXPath(rootPath + "/day");
		for (int indexDay = 1; indexDay <= countDays; indexDay++) {
			Day day;
			
			final String weekday = findByXPath(rootPath + "/day["
					+ indexDay + "]/weekday/text()", XPathConstants.STRING);
			if (!TextUtils.isEmpty(weekday)) {
				day = Day.of(weekday);
			}
			
			final int countTimes = getCountByXPath(rootPath + "/day["+indexDay+"]/time");
			for (int indexTime = 1; indexTime <= countTimes; indexTime++) {
				Time time;
				String room;
				
				final String startTimeStr = findByXPath(rootPath + "/day["+indexDay+"]/time["
						+ indexTime + "]/starttime/text()", XPathConstants.STRING);
				if (!TextUtils.isEmpty(startTimeStr)) {
					time = Time.of(startTimeStr);
				}
				room = findByXPath(rootPath + "/day["+indexDay+"]/time["
						+ indexTime + "]/booking/room/text()", XPathConstants.STRING);
				
				final int countCourses = getCountByXPath(rootPath + "/day["+indexDay+"]/time["
						+ indexTime + "]/courses/course");
				for (int indexCourse = 1; indexCourse <= countCourses; indexCourse++) {
					
				}
			}
		}
		return null;
	}
}
