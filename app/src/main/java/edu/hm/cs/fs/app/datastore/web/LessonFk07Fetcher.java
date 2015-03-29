package edu.hm.cs.fs.app.datastore.web;

import android.content.Context;
import android.text.TextUtils;

import javax.xml.xpath.XPathConstants;

import edu.hm.cs.fs.app.datastore.model.constants.Day;
import edu.hm.cs.fs.app.datastore.model.constants.Time;
import edu.hm.cs.fs.app.datastore.model.impl.LessonImpl;
import edu.hm.cs.fs.app.datastore.web.fetcher.AbstractXmlFetcher;

public class LessonFk07Fetcher extends AbstractXmlFetcher<LessonFk07Fetcher, LessonImpl> {
    private static final String URL = "http://fi.cs.hm.edu/fi/rest/public/timetable/group.xml";
    private static final String ROOT_NODE = "/list/timetable/day/time/booking";

    public LessonFk07Fetcher(Context context) {
        super(context, URL, ROOT_NODE);
    }

    @Override
    protected LessonImpl onCreateItem(String rootPath) throws Exception {
        String group;
        Day day = null;
        Time time = null;
        String room;
        String teacherId;
        String suffix;
        String moduleId;

        group = findByXPath(rootPath.substring(0, "/list/timetable[x]".length()) + "/value/text()",
                XPathConstants.STRING);
        final String weekday = findByXPath(rootPath + "/weekday/text()", XPathConstants.STRING);
        if (!TextUtils.isEmpty(weekday)) {
            day = Day.of(weekday);
        }
        final String startTimeStr = findByXPath(rootPath + "/starttime/text()", XPathConstants.STRING);
        if (!TextUtils.isEmpty(startTimeStr)) {
            time = Time.of(startTimeStr);
        }
        room = findByXPath(rootPath + "/room/text()", XPathConstants.STRING);
        teacherId = findByXPath(rootPath + "/teacher/text()", XPathConstants.STRING);
        suffix = findByXPath(rootPath + "/suffix/text()", XPathConstants.STRING);
        moduleId = findByXPath(rootPath + "/courses/course[0]/modul/text()", XPathConstants.STRING);

        LessonImpl lesson = new LessonImpl();
        lesson.setId(day.toString() + time.toString() + group + moduleId);
        lesson.setDay(day.toString());
        lesson.setGroup(group);
        lesson.setTeacherId(teacherId);
        lesson.setRoom(room);
        lesson.setSuffix(suffix);
        lesson.setTime(time.toString());
        lesson.setModuleId(moduleId);

        return lesson;
    }
}
