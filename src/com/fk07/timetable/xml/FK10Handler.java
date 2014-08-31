package com.fk07.timetable.xml;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import com.fk07.timetable.xml.timetablefk10.FK10Day;
import com.fk07.timetable.xml.timetablefk10.FK10Group;
import com.fk07.timetable.xml.timetablefk10.Lecture;

/**
 * aka BitchHandler
 *
 */
public class FK10Handler {
	
	private final static String fk10URL = "http://w3bw-o.hm.edu/iframe/studieninfo_vorlesungsplan.php";
	
	private final static String userAgent = "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:28.0) Gecko/20100101 Firefox/28.0";

	public static List<FK10Day> downloadHTMLtoObject(final FK10Group group) throws IOException {
		
		Connection conn = Jsoup.connect(fk10URL);
		conn = conn.referrer(fk10URL);
		conn = conn.userAgent(userAgent);
//		conn = conn.data("studiengang", group.getCourse());
		conn = conn.data("semestergr", group.getGroupId());
		conn = conn.data("modul", "");
		conn = conn.data("lv", "");
		conn = conn.data("dozent", "");
		conn = conn.data("kategorie", "Stundenplan");
		conn = conn.data("sem", "");
		conn = conn.data("Stundenplan+anzeigen", "suchen");
		final Document doc = conn.post();
		final Element table = doc.select("table").get(4);
		final Elements rows = table.getElementsByTag("td");
		final List<FK10Day> days = new ArrayList<FK10Day>();
		for (final Element row : rows) {
			if (row.toString().contains("<h1")) {
				final FK10Day day = new FK10Day(row.text());
				days.add(day);
			} else if (row.toString().contains("<h3")) {
				final Lecture one = new Lecture(row.text());
				final FK10Day lastDay = days.get(days.size() - 1);
				lastDay.addLecture(one);
			} else if (row.text().toString().matches("[A-Za-z]+[0-9]+ .*")) {
				final FK10Day lastDay = days.get(days.size() - 1);
				final Lecture lastClass = lastDay.getLectures().get(lastDay.getLectures().size() - 1);
				lastClass.setName(row.text());
			} else if (row.text().toString().matches("[A-Za-z]+[0-9]+")) {
				final FK10Day lastDay = days.get(days.size() - 1);
				final Lecture lastClass = lastDay.getLectures().get(lastDay.getLectures().size() - 1);
				lastClass.setRoom(row.text());
			} else {
				if (!row.text().equals("")) {
					final FK10Day lastDay = days.get(days.size() - 1);
					final Lecture lastClass = lastDay.getLectures().get(lastDay.getLectures().size() - 1);
					if (lastClass.getTeacher() == null) {
						lastClass.setTeacher(row.text());
					}
				}
			}
		}
		
		return days;
	}
	
	public static List<FK10Group> getBWLGroups() throws IOException {
		Connection conn = Jsoup.connect(fk10URL);
		conn = conn.referrer(fk10URL);
		conn = conn.userAgent(userAgent);
		final Document doc = conn.get();
		final Elements scripts = doc.select("script");
		final Pattern pattern = Pattern.compile("addOption\\([^\\)]*\\)");
		final Matcher matcher = pattern.matcher(scripts.get(6).toString());
		final List<FK10Group> groups = new ArrayList<FK10Group>();
		while (matcher.find()) {
			final String match = matcher.group();
			if (match.contains("WIF ")) {
				String groupName = match.replaceFirst("addOption\\(\\\"schs[0-9]+\", \"", "");
				groupName = groupName.replaceFirst("", "");
				String groupId = groupName;
				groupName = groupName.replaceFirst("\", \"[0-9]*\"\\)", "");
				groupId = groupId.replaceFirst("WIF [0-9]*[ ]?[A-Za-z]*\", \"", "");
				groupId = groupId.replaceFirst("\"\\)", "");

				groups.add(new FK10Group(groupName, groupId));
			}
		}
		return groups;
	}
	
	public static int getFK10TimeIndex(final String time) {
		
		if (time.contains(":")) {
			String[] timeArray = time.split(":");
			int hour = Integer.valueOf(timeArray[0]);
			switch (hour) {
			case 7:
				return 0;
			case 8:
				return 0;
			case 9:
				return 1;
			case 10:
				return 1;
			case 11:
				return 2;
			case 12:
				return 2;
			case 13:
				return 3;
			case 14:
				return 4;
			case 15:
				return 4;
			case 16:
				return 5;
			case 17:
				return 5;
			case 18:
				return 6;
			case 19:
				return 6;
			case 20:
				return 6;
			default:
				return -1;
			}
		} else {
			return -1;
		}
	}
	
	public static int getFK10WeekDayIndex(final String weekday) {
		
		String weekdayLow = weekday.toLowerCase();
		
		if (weekdayLow.equals("montag")||weekdayLow.equals("mo")) {
			return 0;
		} else if(weekdayLow.equals("dienstag")||weekdayLow.equals("di")) {
			return 1;
		} else if(weekdayLow.equals("mittwoch")||weekdayLow.equals("mi")) {
			return 2;
		} else if(weekdayLow.equals("donnerstag")||weekdayLow.equals("do")) {
			return 3;
		} else if(weekdayLow.equals("freitag")||weekdayLow.equals("fr")) {
			return 4;
		} else {
			return -1;
		}
		
	}
}
