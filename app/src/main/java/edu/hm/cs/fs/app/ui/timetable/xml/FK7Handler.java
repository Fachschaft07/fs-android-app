package edu.hm.cs.fs.app.ui.timetable.xml;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import android.util.Log;

import edu.hm.cs.fs.app.ui.timetable.xml.course.Module;
import edu.hm.cs.fs.app.ui.timetable.xml.course.ModuleCode;
import edu.hm.cs.fs.app.ui.timetable.xml.course.ModuleCodes;
import edu.hm.cs.fs.app.ui.timetable.xml.course.ModuleList;
import edu.hm.cs.fs.app.ui.timetable.xml.groups.FK07Group;
import edu.hm.cs.fs.app.ui.timetable.xml.groups.FK07Groups;
import edu.hm.cs.fs.app.ui.timetable.xml.teacher.Person;
import edu.hm.cs.fs.app.ui.timetable.xml.teacher.PersonList;
import edu.hm.cs.fs.app.ui.timetable.xml.timetable.Day;
import edu.hm.cs.fs.app.ui.timetable.xml.timetable.Entry;
import edu.hm.cs.fs.app.ui.timetable.xml.timetable.Time;
import edu.hm.cs.fs.app.ui.timetable.xml.timetable.Timetable;
import edu.hm.cs.fs.app.util.DownloadException;

public class FK7Handler {
	
	private final static String TAG = "FK7Handler";
	
	private final static String personURL = "http://fi.cs.hm.edu/fi/rest/public/person.xml";
	
	private final static String modulURL = "http://fi.cs.hm.edu/fi/rest/public/modul.xml";
	
	private final static String groupURL = "http://fi.cs.hm.edu/fi/rest/public/group.xml";
	
	public static void parseEntries(final Timetable timetable) throws DownloadException {
		final PersonList personList = downloadXMLtoObject(PersonList.class, personURL);
		ModuleList moduleList = downloadXMLtoObject(ModuleList.class, modulURL);
		HashMap<String, Person> personMap = getPersonMap(personList);
		HashMap<String, String> courseMap = getCourseMap(moduleList);
		
		for (Day day : timetable.getDay()) {
			day.getTime().remove(day.getTime().size() - 1);
			day.getTime().remove(0);
			for (Time time : day.getTime()) {
				List<Entry> cleanEntries = new ArrayList<Entry>();
				for (Entry entry : time.getEntry()) {
					if (!entry.getType().equals("filler")) {
						cleanEntries.add(entry);
						entry.setTitle(courseMap.get(entry.getTitle()));
						if (entry.getTeacher() != null) {
							ArrayList<String> newTeacherList = new ArrayList<String>();
							for (String teacher : entry.getTeacher()) {
								String fullName = personMap.get(teacher).getFirstname();
								fullName += " " + personMap.get(teacher).getLastname();
								newTeacherList.add(fullName);
							}
							entry.setTeacher(newTeacherList);
						}
					}
				}
				time.setEntry(cleanEntries);
			}
		}
		
	}
	
	private static HashMap<String, Person> getPersonMap(final PersonList personList) {
		final HashMap<String, Person> id2Person = new HashMap<String, Person>();
		for (final Person person : personList.getPersons()) {
			id2Person.put(person.getId(), person);
		}
		return id2Person;
	}
	
	private static HashMap<String, String> getCourseMap(final ModuleList moduleList) {
		final HashMap<String, String> id2ModuleName = new HashMap<String, String>();
		for (final Module module : moduleList.getModules()) {
			final ModuleCodes moduleCodes = module.getModulecodes();
			if (moduleCodes != null && moduleCodes.getModulecodes() != null) {
				for (final ModuleCode moduleCode : moduleCodes.getModulecodes()) {
					if (!id2ModuleName.containsKey(moduleCode.getModul())) {
						id2ModuleName.put(moduleCode.getModul(), module.getName());
					}
				}
			}
		}
		return id2ModuleName;
	}
	
	public static List<FK07Group> getGroupList() throws DownloadException {
		final FK07Groups groups = downloadXMLtoObject(FK07Groups.class, groupURL);
		return groups.getGroups();
	}

	public static <T> T downloadXMLtoObject(final Class<T> type, final String url) throws DownloadException {
		T parsedXMLObject = null;
		try {
			final URL source = new URL(url);
			HttpURLConnection conn;
			conn = (HttpURLConnection) source.openConnection();
			conn.setReadTimeout(10000);
			conn.setConnectTimeout(15000);
			conn.setRequestMethod("GET");
			conn.setDoInput(true);
			conn.connect();
			final Serializer serializer = new Persister();
			parsedXMLObject = (T) serializer.read(type, conn.getInputStream());
		} catch (final Exception e) {
			Log.e(TAG, e.getMessage());
			throw new DownloadException(e.getMessage());
		}
		return parsedXMLObject;
	}
}
