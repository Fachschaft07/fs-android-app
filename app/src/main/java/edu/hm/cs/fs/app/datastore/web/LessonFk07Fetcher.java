package edu.hm.cs.fs.app.datastore.web;

import android.content.Context;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.xml.xpath.XPathConstants;

import edu.hm.cs.fs.app.datastore.model.Group;
import edu.hm.cs.fs.app.datastore.model.constants.Day;
import edu.hm.cs.fs.app.datastore.model.constants.Time;
import edu.hm.cs.fs.app.datastore.model.impl.LessonImpl;
import edu.hm.cs.fs.app.datastore.web.fetcher.AbstractXmlFetcher;

public class LessonFk07Fetcher extends AbstractXmlFetcher<LessonFk07Fetcher, List<LessonImpl>> {
    private static final String URL = "http://fi.cs.hm.edu/fi/rest/public/timetable/group/";
    private static final String ROOT_NODE = "/timetable/day";

    public LessonFk07Fetcher(Context context, Group group) {
        super(context, URL + group.toString().toLowerCase(Locale.getDefault()) + ".xml", ROOT_NODE);
    }

    @Override
    protected List<LessonImpl> onCreateItem(String rootPath) throws Exception {
        List<LessonImpl> result = new ArrayList<>();
        Day day = null;
        Time time = null;

        final String weekday = findByXPath(rootPath + "/weekday/text()", XPathConstants.STRING);
        if (!TextUtils.isEmpty(weekday)) {
            day = Day.of(weekday);
        }

        final int countTime = getCountByXPath(rootPath + "/time");
        for (int indexTime = 1; indexTime <= countTime; indexTime++) {
            final int countEntry = getCountByXPath(rootPath + "/time[" + indexTime + "]/entry");

            for (int indexEntry = 1; indexEntry <= countEntry; indexEntry++) {
                final String secondPath = rootPath + "/time[" + indexTime + "]/entry[" + indexEntry + "]";

                // If field 'type' contains >filler< continue with the next entry
                final String type = findByXPath(secondPath + "/type/text()", XPathConstants.STRING);
                if ("filler".equalsIgnoreCase(type)) {
                    continue;
                }

                // Else go on...
                final String startTimeStr = findByXPath(secondPath + "/starttime/text()", XPathConstants.STRING);
                if (!TextUtils.isEmpty(startTimeStr)) {
                    time = Time.of(startTimeStr);
                }
                final String room = findByXPath(secondPath + "/room/text()", XPathConstants.STRING);
                final String teacherId = findByXPath(secondPath + "/teacher/text()", XPathConstants.STRING);
                final String suffix = findByXPath(secondPath + "/suffix/text()", XPathConstants.STRING);
                final String moduleId = findByXPath(secondPath + "/title/text()", XPathConstants.STRING);

                LessonImpl lesson = new LessonImpl();
                lesson.setDay(day.toString());
                lesson.setTeacherId(teacherId);
                lesson.setRoom(room);
                lesson.setSuffix(suffix);
                lesson.setTime(time.toString());
                lesson.setModuleId(moduleId);

                result.add(lesson);
            }
        }
        return result;
    }
}
