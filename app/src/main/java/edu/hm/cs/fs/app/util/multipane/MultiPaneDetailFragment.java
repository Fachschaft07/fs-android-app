package edu.hm.cs.fs.app.util.multipane;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

/**
 * This class must be implemented from the detail fragment.
 *
 * @param <T> Type of the items which will be displayed in the list and detail fragment.
 */
public abstract class MultiPaneDetailFragment<T> extends Fragment {
	private T mObject;
	private int mPosition;
	private boolean mViewCreated;

	@Override
	public void onViewCreated(final View view, @Nullable final Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		mViewCreated = true;
	}

	/**
	 * When the user clicked on an item in the list fragment this method is called to inform the
	 * detail fragment to update the content.
	 *
	 * @param item to be shown in the detail fragment.
	 * @param position of the item in the list fragment.
	 */
	public void onListItemClicked(T item, int position) {
		mObject = item;
		mPosition = position;
		notifyDataSetChanged();
	}

	/**
	 * Updates the views.
	 */
	public void notifyDataSetChanged() {
		if(mObject != null && mViewCreated) {
			onDisplayData(mObject, mPosition);
		}
	}

	/**
	 * Load the data into the view and display it.
	 *
	 * @param item to display.
	 * @param position where the item is in the list fragment.
	 */
	public abstract void onDisplayData(T item, int position);
}
