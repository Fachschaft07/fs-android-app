/*
 * Copyright (C) 2012 Mobs and Geeks
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */

package com.fk07.util.adapter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * A very simple adapter that adds sections to adapters written for {@link ListView}s. <br />
 * <b>NOTE: The adapter assumes that the data source of the decorated list adapter is sorted.</b>
 * 
 * @author Ragunath Jawahar R <rj@mobsandgeeks.com>
 * @version 0.2
 */
public class SimpleSectionAdapter<I, O extends Comparable<O>> extends BaseAdapter {
	private static final DateFormat DATE_FORMAT = SimpleDateFormat.getDateInstance(DateFormat.MEDIUM);

	// Constants
	private static final int VIEW_TYPE_SECTION_HEADER = 0;

	// Attributes
	private final Context mContext;
	private final BaseAdapter mListAdapter;
	private final int mSectionHeaderLayoutId;
	private final int mSectionTitleTextViewId;
	private final Sectionizer<I, O> mSectionizer;
	private final Map<O, Integer> mSections;
	private final boolean mDesc;
	private final DataSetObserver dataSetObserver = new DataSetObserver() {
		@Override
		public void onChanged() {
			super.onChanged();
			findSections();
		}
	};

	/**
	 * Constructs a {@linkplain SimpleSectionAdapter}.
	 * 
	 * @param context
	 *            The context for this adapter.
	 * @param listAdapter
	 *            A {@link ListAdapter} that has to be sectioned.
	 * @param sectionHeaderLayoutId
	 *            Layout Id of the layout that is to be used for the header.
	 * @param sectionTitleTextViewId
	 *            Id of a TextView present in the section header layout.
	 * @param sectionizer
	 *            Sectionizer for sectioning the {@link ListView}.
	 * @param desc
	 *            if true then sort by descending otherwise sort by ascending
	 */
	public SimpleSectionAdapter(final Context context, final BaseAdapter listAdapter, final int sectionHeaderLayoutId,
			final int sectionTitleTextViewId, final Sectionizer<I, O> sectionizer, final boolean desc) {
		if (context == null) {
			throw new IllegalArgumentException("context cannot be null.");
		} else if (listAdapter == null) {
			throw new IllegalArgumentException("listAdapter cannot be null.");
		} else if (sectionizer == null) {
			throw new IllegalArgumentException("sectionizer cannot be null.");
		} else if (!isTextView(context, sectionHeaderLayoutId, sectionTitleTextViewId)) {
			throw new IllegalArgumentException("sectionTitleTextViewId should be a TextView.");
		}

		this.mContext = context;
		this.mListAdapter = listAdapter;
		this.mSectionHeaderLayoutId = sectionHeaderLayoutId;
		this.mSectionTitleTextViewId = sectionTitleTextViewId;
		this.mSectionizer = sectionizer;
		this.mDesc = desc;
		this.mSections = new LinkedHashMap<O, Integer>();

		registerDataSetObserver(dataSetObserver);

		// Find sections
		findSections();
	}

	private boolean isTextView(final Context context, final int layoutId, final int textViewId) {
		final View inflatedView = View.inflate(context, layoutId, null);
		final View foundView = inflatedView.findViewById(textViewId);

		return foundView instanceof TextView;
	}

	public int getCount() {
		return mListAdapter.getCount() + getSectionCount();
	}

	public View getView(final int position, final View convertView, final ViewGroup parent) {
		View view = convertView;
		SectionHolder sectionHolder = null;

		switch (getItemViewType(position)) {
			case VIEW_TYPE_SECTION_HEADER:
				if (view == null) {
					view = View.inflate(mContext, mSectionHeaderLayoutId, null);

					sectionHolder = new SectionHolder();
					sectionHolder.titleTextView = (TextView) view.findViewById(mSectionTitleTextViewId);

					view.setTag(sectionHolder);
				} else {
					sectionHolder = (SectionHolder) view.getTag();
				}
				break;

			default:
				view = mListAdapter.getView(getIndexForPosition(position), convertView, parent);
				break;
		}

		if (sectionHolder != null) {
			final O sectionName = sectionTitleForPosition(position);
			String sectionNameString = sectionName.toString();
			if (sectionName instanceof Date) {
				sectionNameString = DATE_FORMAT.format((Date) sectionName);
			}
			sectionHolder.titleTextView.setText(sectionNameString);
		}

		return view;
	}

	@Override
	public boolean areAllItemsEnabled() {
		return mListAdapter.areAllItemsEnabled() ? mSections.size() == 0 : false;
	}

	@Override
	public int getItemViewType(final int position) {
		final int positionInCustomAdapter = getIndexForPosition(position);
		return mSections.values().contains(position) ? VIEW_TYPE_SECTION_HEADER : mListAdapter
				.getItemViewType(positionInCustomAdapter) + 1;
	}

	@Override
	public int getViewTypeCount() {
		return mListAdapter.getViewTypeCount() + 1;
	}

	@Override
	public boolean isEnabled(final int position) {
		return mSections.values().contains(position) ? false : mListAdapter.isEnabled(getIndexForPosition(position));
	}

	public Object getItem(final int position) {
		return mListAdapter.getItem(getIndexForPosition(position));
	}

	public long getItemId(final int position) {
		return mListAdapter.getItemId(getIndexForPosition(position));
	}

	@Override
	public void notifyDataSetChanged() {
		mListAdapter.notifyDataSetChanged();
		findSections();
		super.notifyDataSetChanged();
	}

	/**
	 * Returns the actual index of the object in the data source linked to the this list item.
	 * 
	 * @param position
	 *            List item position in the {@link ListView}.
	 * @return Index of the item in the wrapped list adapter's data source.
	 */
	public int getIndexForPosition(final int position) {
		int nSections = 0;

		final Set<Entry<O, Integer>> entrySet = mSections.entrySet();
		for (final Entry<O, Integer> entry : entrySet) {
			if (entry.getValue() < position) {
				nSections++;
			}
		}

		return position - nSections;
	}

	static class SectionHolder {
		public TextView titleTextView;
	}

	private void findSections() {
		final int n = mListAdapter.getCount();
		int nSections = 0;
		mSections.clear();

		final Map<O, O> sortedMap = new TreeMap<O, O>(new Comparator<O>() {
			public int compare(final O lhs, final O rhs) {
				if (mDesc) {
					return -1 * lhs.compareTo(rhs);
				}
				return lhs.compareTo(rhs);
			}
		});

		for (int i = 0; i < n; i++) {
			@SuppressWarnings("unchecked")
			final I item = (I) mListAdapter.getItem(i);
			final O sortItem = mSectionizer.getSortForItem(item);
			final O sectionName = mSectionizer.getSectionTitleForItem(item);

			sortedMap.put(sortItem, sectionName);
		}

		int index = 0;
		for (final O value : sortedMap.values()) {
			if (!mSections.containsKey(value)) {
				mSections.put(value, index + nSections);
				nSections++;
			}
			index++;
		}
	}

	private int getSectionCount() {
		return mSections.size();
	}

	private O sectionTitleForPosition(final int position) {
		O title = null;

		final Set<Entry<O, Integer>> entrySet = mSections.entrySet();
		for (final Entry<O, Integer> entry : entrySet) {
			if (entry.getValue() == position) {
				title = entry.getKey();
				break;
			}
		}

		return title;
	}

	@Override
	public void registerDataSetObserver(final DataSetObserver observer) {
		mListAdapter.registerDataSetObserver(observer);
	}

	@Override
	public void unregisterDataSetObserver(final DataSetObserver observer) {
		mListAdapter.unregisterDataSetObserver(observer);
	}
}