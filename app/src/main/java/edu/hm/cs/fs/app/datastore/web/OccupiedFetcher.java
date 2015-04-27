package edu.hm.cs.fs.app.datastore.web;

import android.content.Context;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.xpath.XPathConstants;

import edu.hm.cs.fs.app.datastore.model.constants.Day;
import edu.hm.cs.fs.app.datastore.model.constants.Time;
import edu.hm.cs.fs.app.datastore.model.impl.OccupiedImpl;
import edu.hm.cs.fs.app.datastore.web.fetcher.AbstractXmlFetcher;

/**
 * All the rooms with their occupancy. (Url: <a href="http://fi.cs.hm.edu/fi/rest/public/timetable/room"
 * >http://fi.cs.hm.edu/fi/rest/public/timetable/room</a>)
 *
 * @author Fabio
 */
public class OccupiedFetcher extends AbstractXmlFetcher<OccupiedFetcher, OccupiedImpl> {
    private static final String URL = "http://fi.cs.hm.edu/fi/rest/public/timetable/room.xml";
    private static final String ROOT_NODE = "/list/timetable/day/time/booking";

    /**
     * @param context
     */
    public OccupiedFetcher(final Context context) {
        super(context, URL, ROOT_NODE);
    }

    @Override
    protected OccupiedImpl onCreateItem(final String rootPath) throws Exception {
        String room;
        Day day = null;
        Time time = null;

        // Parse Elements...
        room = findByXPath(rootPath + "/value/text()",
                XPathConstants.STRING);
        final String weekDay = findByXPath(rootPath + "/weekday/text()", XPathConstants.STRING);
        if (!TextUtils.isEmpty(weekDay)) {
            day = Day.of(weekDay);
        }
        final String timeStr = findByXPath(rootPath + "/starttime/text()", XPathConstants.STRING);
        if (!TextUtils.isEmpty(timeStr)) {
            time = Time.of(timeStr);
        }

        OccupiedImpl occupied = new OccupiedImpl();
        occupied.setRoom(room);
        occupied.setDay(day.toString());
        occupied.setTime(time.toString());

        return occupied;
    }
}
