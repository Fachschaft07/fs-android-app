package edu.hm.cs.fs.app.util.multipane;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;

/**
 * This class must be extended from the list fragment.
 *
 * @param <T> Type of the items which will be displayed in the list and detail fragment.
 */
public abstract class MultiPaneListFragment<T> extends Fragment implements AdapterView.OnItemClickListener {
	private MultiPaneDetailFragment<T> mDetailSegment;
	private AbsListView mListView;

	@Override
	public void onViewCreated(final View view, @Nullable final Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		mListView = (AbsListView) view.findViewById(getAbsListViewId());
		mListView.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
		mDetailSegment.onListItemClicked(getItemAt(position), position);
	}

	/**
	 * Set the detail fragment to communicate with.
	 *
	 * @param fragment which will display the item more detailed.
	 */
	void setDetailSegment(MultiPaneDetailFragment<T> fragment) {
		mDetailSegment = fragment;
	}

	/**
	 * Get the selected position.
	 *
	 * @return the position.
	 */
	protected int getSelectedPosition() {
		return mListView.getSelectedItemPosition();
	}

	/**
	 * Get the item at the position from the list adapter.
	 *
	 * @param position of the item.
	 * @return the item.
	 */
	protected T getItemAt(int position) {
		return (T) mListView.getItemAtPosition(position);
	}

	/**
	 * Get the resource-ID from the AbsListView.
	 *
	 * @return the ID.
	 */
	public abstract int getAbsListViewId();
}
