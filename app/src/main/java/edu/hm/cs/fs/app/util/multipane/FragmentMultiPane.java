package edu.hm.cs.fs.app.util.multipane;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fk07.R;

/**
 * This fragment handel's all the stuff for multi-panel layout.
 *
 * @param <T> Type of the items which will be displayed in the list and detail fragment.
 */
public class FragmentMultiPane<T> extends MultiPaneDetailFragment<T> {
	private static final String LIST_FRAGMENT = "left_pane";
	private static final String DETAIL_FRAGMENT = "right_pane";
	private static final String LAYOUT_ID = "layout_id";
	private static final String SELECTED_ITEM = "selected_item";
	private static final int DEFAULT_POSITION = 0;
	private static final int UNEXPECTED_POSITION = -1;

	private MultiPaneListFragment<T> mListFragment;
    private MultiPaneDetailFragment<T> mDetailFragment;

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Set to true to make it possible for list and detail fragment to show a option menu
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
		return inflater.inflate(R.layout.multi_pane_show_hide, container, false);
	}

	@SuppressWarnings({"unchecked"})
	@Override
	public void onViewCreated(final View view, @Nullable final Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		final FragmentManager fragmentManager = getFragmentManager();

		if(mListFragment == null) {
			// Load left side fragment - List Fragment
			String fragmentClassNameLeftPane = getArguments().getString(LIST_FRAGMENT);
            mListFragment = (MultiPaneListFragment<T>) Fragment.instantiate(getActivity(), fragmentClassNameLeftPane);
            mListFragment.setRetainInstance(true);
            mListFragment.setDetailSegment(this);
		}

		if(mDetailFragment == null) {
			// Load right side fragment - Detail Fragment
			String fragmentClassNameRightPane = getArguments().getString(DETAIL_FRAGMENT);
            mDetailFragment = (MultiPaneDetailFragment<T>) Fragment.instantiate(getActivity(), fragmentClassNameRightPane);
            mDetailFragment.setRetainInstance(true);
		}
		view.findViewById(R.id.detail_pane).setVisibility(isMultiPane() ? View.VISIBLE : View.GONE);

		// Load and instantiate the fragments
		loadFragment(R.id.list_pane, mListFragment, LIST_FRAGMENT);
		if(isMultiPane()) {
			loadFragment(R.id.detail_pane, mDetailFragment, DETAIL_FRAGMENT);
			if (savedInstanceState == null) {
				// Select first element
				onListItemClicked(mListFragment.getItemAt(DEFAULT_POSITION), DEFAULT_POSITION);
			} else {
				// Select last selected element
				int position = savedInstanceState.getInt(SELECTED_ITEM, DEFAULT_POSITION);
				onListItemClicked(mListFragment.getItemAt(position), position);
			}
		}
	}

	@Override
	public void onListItemClicked(final T item, final int position) {
		mDetailFragment.onListItemClicked(item, position);
		if(!isMultiPane()) {
			// Single-Pane Layout

			// We can not only give the activity the name of the detail fragment because the
			// fragment already received the data from the onListItemClicked event.
			ActivityMultiPaneDetail.setDetailFragment(mDetailFragment);

			startActivity(new Intent(getActivity(), ActivityMultiPaneDetail.class));
		}
	}

	@Override
	public void onDisplayData(final T item, final int position) {
		// need this method for design pattern...
	}

	@Override
	public void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
		int position = mListFragment.getSelectedPosition();
		if(position == UNEXPECTED_POSITION) {
			position = DEFAULT_POSITION;
		}
		outState.putInt(SELECTED_ITEM, position);
	}

    private void loadFragment(int containerId, Fragment fragment, String tag) {
		final FragmentManager fragmentManager = getFragmentManager();
		final FragmentTransaction transaction = fragmentManager.beginTransaction();

		//Log.d(getClass().getSimpleName(), "Searching for fragment "+fragment.getClass().getName()+"...");
		Fragment searchFragment = fragmentManager.findFragmentByTag(tag);
		if(searchFragment == null) {
		//	Log.d(getClass().getSimpleName(), "Fragment "+fragment.getClass().getName()+" not found -> initialize it");
		//	transaction.setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
			transaction.replace(containerId, fragment, tag);

            transaction.disallowAddToBackStack();
            transaction.commit();
		} // else {
		//	Log.d(getClass().getSimpleName(), "Fragment " + fragment.getClass().getName() + " found -> show it");
		//	transaction.show(searchFragment);
		//}
	}

	private boolean isMultiPane() {
		// Don't save this value in a field variable due to the fact that this is dynamically
		// changing on layout changes.
		return getResources().getBoolean(R.bool.multi_pane_show_hide);
	}

	/**
	 * Build a multi-pane fragment with list and detail fragments.
	 *
	 * @param listFragment which displays the items to choose.
	 * @param detailFragment which displays the item which was chosen.
	 * @param <T> type of the items.
	 * @return the multi-pane fragment.
	 */
	public static <T> FragmentMultiPane<T> build(Class<? extends MultiPaneListFragment<T>> listFragment,
												 Class<? extends MultiPaneDetailFragment<T>> detailFragment) {
		Bundle bundle = new Bundle();
		bundle.putString(LIST_FRAGMENT, listFragment.getName());
		bundle.putString(DETAIL_FRAGMENT, detailFragment.getName());

		FragmentMultiPane<T> fragmentMultiPane = new FragmentMultiPane<>();
		fragmentMultiPane.setArguments(bundle);

		return fragmentMultiPane;
	}
}
