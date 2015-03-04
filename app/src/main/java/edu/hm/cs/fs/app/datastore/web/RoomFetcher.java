package edu.hm.cs.fs.app.datastore.web;

import android.content.Context;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.xpath.XPathConstants;

import edu.hm.cs.fs.app.datastore.web.fetcher.AbstractXmlFetcher;
import edu.hm.cs.fs.app.datastore.model.constants.Day;
import edu.hm.cs.fs.app.datastore.model.constants.Time;
import edu.hm.cs.fs.app.datastore.model.impl.RoomImpl;
import edu.hm.cs.fs.app.datastore.web.utils.DataUtils;

/**
 * All the rooms with their occupancy. (Url: <a href="http://fi.cs.hm.edu/fi/rest/public/timetable/room"
 * >http://fi.cs.hm.edu/fi/rest/public/timetable/room</a>)
 *
 * @author Fabio
 */
public class RoomFetcher extends AbstractXmlFetcher<RoomFetcher, RoomImpl> {
	private static final String URL = "http://fi.cs.hm.edu/fi/rest/public/timetable/room.xml";
	private static final String ROOT_NODE = "/list/timetable";

	/**
	 * @param context
	 */
	public RoomFetcher(final Context context) {
		super(context, URL, ROOT_NODE);
	}

	@Override
	protected RoomImpl onCreateItem(final String rootPath) throws Exception {
		String mName;
		int mCapacity;
		Map<Day, List<Time>> mOccupied = new HashMap<Day, List<Time>>();
		Day mDay = null;

		// Parse Elements...
		mName = findByXPath(rootPath + "/value/text()",
				XPathConstants.STRING);
		mCapacity = Integer.parseInt((String) findByXPath(rootPath
				+ "/capacity/text()", XPathConstants.STRING));

		final int countDays = getCountByXPath(rootPath + "/day");
		for (int indexDay = 1; indexDay <= countDays; indexDay++) {
			final String weekDay = findByXPath(rootPath + "/day["
					+ indexDay + "]/weekday/text()", XPathConstants.STRING);
			if (!TextUtils.isEmpty(weekDay)) {
				mDay = Day.of(weekDay);
			}

			final int countTime = getCountByXPath(rootPath + "/day["
					+ indexDay + "]/time");
			for (int indexTime = 1; indexTime <= countTime; indexTime++) {
				final String time = findByXPath(rootPath + "/day["
						+ indexDay + "]/time[" + indexTime
						+ "]/starttime/text()", XPathConstants.STRING);
				if (!TextUtils.isEmpty(time)) {
					addToMap(mOccupied, mDay, Time.of(time).get());
				}
			}
		}

		return new RoomFetcher(this);
	}

	private void addToMap(final Map<Day, List<Time>> map, final Day day,
						  final Time time) {
		if (!map.containsKey(day)) {
			map.put(day, new ArrayList<Time>());
		}
		map.get(day).add(time);
	}
}
