package edu.hm.cs.fs.app.ui.timetable.xml;

import android.app.Activity;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import edu.hm.cs.fs.app.ui.timetable.xml.timetable.Day;
import edu.hm.cs.fs.app.ui.timetable.xml.timetable.Entry;
import edu.hm.cs.fs.app.ui.timetable.xml.timetable.Time;
import edu.hm.cs.fs.app.ui.timetable.xml.timetable.Timetable;
import edu.hm.cs.fs.app.ui.timetable.xml.timetablefk10.FK10Day;
import edu.hm.cs.fs.app.ui.timetable.xml.timetablefk10.FK10Group;
import edu.hm.cs.fs.app.ui.timetable.xml.timetablefk10.Lecture;
import edu.hm.cs.fs.app.util.DownloadException;

public class TimetableHandler {
	
	private final static String TAG = "TimetableHandler";
	
	private final static String timetableURL = "http://fi.cs.hm.edu/fi/rest/public/timetable/group/";
	
	/**
	 * Filename the Timetable Object will be stored to the Internal Storage.
	 */
	public final static String TIMETABLE_FILE = "timetable";
	
	public static Timetable downloadTimetable(final String course) throws DownloadException {
		Timetable timetable = FK7Handler.downloadXMLtoObject(Timetable.class, timetableURL + course + ".xml");
		FK7Handler.parseEntries(timetable);
		return timetable;
	}
	
	public static Timetable downloadTimetableFK10(final Activity activity, final FK10Group group) throws IOException {
		
		List<FK10Day> fk10Days = FK10Handler.downloadHTMLtoObject(group);
		
		Timetable timetable = new Timetable();
		
		timetable.init();
		
		timetable.setValue(group.getGroupName());
		
		for (FK10Day fk10Day : fk10Days) {
			int weekIndex = FK10Handler.getFK10WeekDayIndex(fk10Day.getWeekDay());
			if (weekIndex != -1) {
				Day day = timetable.getDay().get(weekIndex);
				for (Lecture lecture : fk10Day.getLectures()) {
					int timeIndex = FK10Handler.getFK10TimeIndex(lecture.getTime());
					if (timeIndex != -1) {
						Time time = day.getTime().get(timeIndex);
						Entry entry = new Entry();
						List<String> roomList = new ArrayList<String>();
						roomList.add(lecture.getRoom());
						entry.setRoom(roomList);
						List<String> teacherList = new ArrayList<String>();
						teacherList.add(lecture.getTeacher());
						entry.setTeacher(teacherList);
						entry.setTitle(lecture.getName());
						entry.setStartTime(lecture.getTime());
						entry.setType("FK10");
						time.getEntry().add(entry);
					}
				}
			}
			
		}
		
		return timetable;
	}
	
	public static Timetable readTimetable(final File file) {
		ObjectInputStream objectInputStream;
		Timetable timetable = null;
		try {
			objectInputStream = new ObjectInputStream(new FileInputStream(file));
			timetable = (Timetable) objectInputStream.readObject();
			objectInputStream.close();
		} catch (StreamCorruptedException e) {
			Log.e(TAG, e.toString());
		} catch (FileNotFoundException e) {
			Log.e(TAG, e.toString());
		} catch (IOException e) {
			Log.e(TAG, e.toString());
		} catch (ClassNotFoundException e) {
			Log.e(TAG, e.toString());
		}
		return  timetable;
	}
	
	public static void saveTimetable(final Timetable timetable, final File file) {
		ObjectOutputStream objectOutputStream;
		try {
			objectOutputStream = new ObjectOutputStream(new FileOutputStream(file));
			objectOutputStream.writeObject(timetable);
			objectOutputStream.close();
		} catch (FileNotFoundException e) {
			Log.e(TAG, e.getMessage());
		} catch (IOException e) {
			Log.e(TAG, e.getMessage());
		}
	}
	
	public static Entry getCurrentTimeEntry(final Timetable timetable) {
		Calendar calendar = Calendar.getInstance();
		int weekday = calendar.get(Calendar.DAY_OF_WEEK) - 2;
		
		int currentTimeIndex = getTimeIndex(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));
		if (currentTimeIndex == -1 && !(weekday == -1 || weekday == 5)) {
			weekday = (weekday + 1) % 5;
			currentTimeIndex = 0;
		} else if(weekday == -1 || weekday == 5) {
			weekday = 0;
			currentTimeIndex = 0;
		}
		
		int weekCounter = weekday;
		int timeIndexCounter = currentTimeIndex;
		
		boolean entryFound = false;
		Entry entry = null;
		
		int counter = 0;
		
		while (!entryFound && counter < 5) {
			counter++;
			Day day = timetable.getDay().get(weekCounter);
			
			int weekCounterTmp = weekCounter;
			
			while (weekCounter == weekCounterTmp && !entryFound) {
				Time time = day.getTime().get(timeIndexCounter);
				List<Entry> entries = time.getEntry();
				if (!entries.isEmpty()) {
					entry = entries.get(time.getCurrentEntry());
					entryFound = true;
					weekday = weekCounterTmp;
					currentTimeIndex = timeIndexCounter;
				} else if (timeIndexCounter < 6) {
					timeIndexCounter++;
				} else {
					weekCounter++;
					timeIndexCounter = 0;
				}
			}
			weekCounter %= 5;
		}
		
		if (entry != null) {
			entry.setTime(getTime(timeIndexCounter));
			entry.setDay(getWeekDay(weekday));
		}
		
		return entry;
	}
	
	public static int getTimeIndex(final int hour, final int minute) {
		
		if (hour < 9 || (hour < 10 && minute < 15)) { // - 9:14
			return 0;
		} else if (hour < 11) { // 9:15 - 10:59
			return 1;
		} else if (hour < 12 || (hour < 13 && minute < 45)) { // 11:00 - 12:44
			return 2;
		} else if (hour < 14 || (hour < 15 && minute < 30)) { // 12:45 - 14:29
			return 3;
		} else if (hour < 16 || (hour < 17 && minute < 15)) { // 14:30 - 16:14
			return 4;
		} else if (hour < 18) { // 16:15 - 17:59
			return 5;
		} else if (hour < 20) { // 18:00 - 19:59
			return 6;
		} else {
			return -1;
		}
	}
	
	public static String getTime(final int position) {
		switch (position) {
		case 0:
			return "08:15-09:45";
		case 1:
			return "10:00-11:30";
		case 2:
			return "11:45-13:15";
		case 3:
			return "13:30-15:00";
		case 4:
			return "15:15-16:45";
		case 5:
			return "17:00-18:30";
		case 6:
			return "18:45-20:15";
		default:
			return "00:00";
		}
	}
	
	public static String getWeekDay(final int index) {
		
		switch (index) {
		case 0:
			return "Mo";
		case 1: 
			return "Di";
		case 2:
			return "Mi";
		case 3:
			return "Do";
		case 4:
			return "Fr";
		default:
			return "Mo";
		}
	}
}
