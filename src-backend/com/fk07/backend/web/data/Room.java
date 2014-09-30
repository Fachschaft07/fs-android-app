package com.fk07.backend.web.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.xpath.XPathConstants;

import android.content.Context;
import android.text.TextUtils;

import com.fk07.backend.web.data.builder.AbstractBuilder;
import com.fk07.backend.web.data.builder.IFilter;
import com.fk07.backend.web.data.constants.Day;
import com.fk07.backend.web.data.constants.Time;
import com.fk07.backend.web.data.utils.DataUtils;

/**
 * All the rooms with their occupancy. (Url: <a
 * href="http://fi.cs.hm.edu/fi/rest/public/timetable/room"
 * >http://fi.cs.hm.edu/fi/rest/public/timetable/room</a>)
 *
 * @author Fabio
 *
 */
public class Room {
	private final String mName;
	private final int mCapacity;
	private final Map<Day, List<Time>> mOccupied;

	private Room(final Builder builder) {
		mName = builder.mName;
		mCapacity = builder.mCapacity;
		mOccupied = builder.mOccupied;
	}

	/**
	 * @return the name.
	 */
	public String getName() {
		return mName;
	}

	/**
	 * @return the capacity.
	 */
	public int getCapacity() {
		return mCapacity;
	}

	/**
	 * Check whether the room is free or not.
	 *
	 * @param day
	 *            to get the result for.
	 * @param time
	 *            to get the result for.
	 * @return <code>true</code> if the room is free at this day & time.
	 */
	public boolean isEmpty(final Day day, final Time time) {
		return !mOccupied.containsKey(day)
				|| !mOccupied.get(day).contains(time);
	}

	/**
	 * @return the occupied.
	 */
	public Map<Day, List<Time>> getOccupied() {
		return new HashMap<Day, List<Time>>(mOccupied);
	}

	/**
	 * @author Fabio
	 *
	 */
	public static class Builder extends AbstractBuilder<Builder, Room> {
		private static final String URL = "http://fi.cs.hm.edu/fi/rest/public/timetable/room.xml";
		private static final String ROOT_NODE = "/list/timetable";
		private String mName;
		private int mCapacity;
		private Map<Day, List<Time>> mOccupied;
		private Day mDay;

		/**
		 * @param context
		 */
		public Builder(final Context context) {
			super(context, URL, ROOT_NODE);
		}

		/**
		 * Filter for a key word or number inside of the room name.
		 *
		 * @param keyWord
		 *            to filter for.
		 * @return the builder.
		 */
		public Builder addRoomFilter(final String keyWord) {
			addFilter(new IFilter<Room>() {
				@Override
				public boolean apply(final Room data) {
					return DataUtils
							.containsIgnoreCase(data.getName(), keyWord);
				}
			});
			return this;
		}

		/**
		 * Filter for all free rooms at a specified time and day.
		 *
		 * @param day
		 *            to filter for.
		 * @param time
		 *            to filter for.
		 * @return the builder.
		 */
		public Builder addEmptyFilter(final Day day, final Time time) {
			addFilter(new IFilter<Room>() {
				@Override
				public boolean apply(final Room data) {
					return data.isEmpty(day, time);
				}
			});
			return this;
		}

		@Override
		protected Room onCreateItem(final String rootPath) throws Exception {
			// reset Variables...
			mOccupied = new HashMap<Day, List<Time>>();
			mDay = null;

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

			return new Room(this);
		}

		private void addToMap(final Map<Day, List<Time>> map, final Day day,
				final Time time) {
			if (!map.containsKey(day)) {
				map.put(day, new ArrayList<Time>());
			}
			map.get(day).add(time);
		}
	}
}
