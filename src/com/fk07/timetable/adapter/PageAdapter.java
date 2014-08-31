package com.fk07.timetable.adapter;

import java.util.List;

import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * PageAdapter.
 * @author Rene
 */
public class PageAdapter extends PagerAdapter {

	private List<View> pages;

	/**
	 * Constructor.
	 * 
	 * @param pages
	 */
	public PageAdapter(final List<View> pages) {
		this.pages = pages;
	}

	@Override
	public Object instantiateItem(final View collection, final int position) {
		final View v = pages.get(position);
		((ViewPager) collection).addView(v, 0);
		return v;
	}

	@Override
	public void destroyItem(final View collection, final int position,
			final Object view) {
		((ViewPager) collection).removeView((View) view);
	}

	@Override
	public int getCount() {
		return pages.size();
	}

	@Override
	public boolean isViewFromObject(final View view, final Object object) {
		return view.equals(object);
	}

	@Override
	public void finishUpdate(final View arg0) {
	}

	@Override
	public void restoreState(final Parcelable arg0, final ClassLoader arg1) {
	}

	@Override
	public Parcelable saveState() {
		return null;
	}

	@Override
	public void startUpdate(final View arg0) {
	}
}
