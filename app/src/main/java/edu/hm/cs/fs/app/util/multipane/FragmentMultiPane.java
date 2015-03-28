package edu.hm.cs.fs.app.util.multipane;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fk07.R;

import butterknife.ButterKnife;

/**
 * Created by Fabio on 19.12.2014.
 */
public class FragmentMultiPane<T> extends Fragment implements OnMultiPaneDetailSegment<T> {
    private static final String LEFT_PANE = "left_pane";
    private static final String RIGHT_PANE = "right_pane";
    private static final String SELECTED_ITEM = "selected_item";

    private OnMultiPaneListSegment<T> mListSegment;
    private Fragment mListFragment;
    private OnMultiPaneDetailSegment<T> mDetailSegment;
    private Fragment mDetailFragment;
    @Nullable
    private Bundle mSavedInstanceState;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_multi_pane, container, false);
    }

    @SuppressWarnings({"unchecked"})
    @Override
    public void onViewCreated(final View view, @Nullable final Bundle savedInstanceState) {
        mSavedInstanceState = savedInstanceState;
        super.onViewCreated(view, savedInstanceState);

        // Load left side fragment - List Fragment
        String fragmentClassNameLeftPane = getArguments().getString(LEFT_PANE);
        mListFragment = Fragment.instantiate(getActivity(), fragmentClassNameLeftPane);
        if (!(mListFragment instanceof OnMultiPaneListSegment)) {
            throw new IllegalArgumentException("The left side must implement the " +
                    "OnMultiPaneListSegment interface");
        }
        mListFragment.setRetainInstance(true);
        mListSegment = (OnMultiPaneListSegment<T>) mListFragment;
        mListSegment.setDetailSegment(this);

        // Load right side fragment - Detail Fragment
        String fragmentClassNameRightPane = getArguments().getString(RIGHT_PANE);
        mDetailFragment = Fragment.instantiate(getActivity(), fragmentClassNameRightPane);
        if (!(mDetailFragment instanceof OnMultiPaneDetailSegment)) {
            throw new IllegalArgumentException("The right side must implement the " +
                    "OnMultiPaneDetailSegment interface");
        }
        mDetailFragment.setRetainInstance(true);
        mDetailSegment = (OnMultiPaneDetailSegment<T>) mDetailFragment;

        // Add Fragments to FrameLayouts
        final FragmentManager supportFragmentManager = getActivity().getSupportFragmentManager();
        final FragmentTransaction transaction = supportFragmentManager.beginTransaction();
        transaction.setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        transaction.replace(R.id.list_pane, mListFragment);
        if (isMultiPane()) {
            view.findViewById(R.id.content_pane).setVisibility(View.VISIBLE);
            transaction.replace(R.id.content_pane, mDetailFragment);
        }
        transaction.disallowAddToBackStack();
        transaction.commit();
    }

    @Override
    public void onStart() {
        super.onStart();

        if (isMultiPane()) {
            if (mSavedInstanceState == null || mSavedInstanceState.isEmpty()) {
                // Select first element only if this is a multi pane layout
                onListItemClicked(mListSegment.getItemAt(0));
            } else {
                // Select last selected element
                onListItemClicked(mListSegment.getItemAt(mSavedInstanceState.getInt(SELECTED_ITEM)));
            }
        }
    }

    @Override
    public void onListItemClicked(final T item) {
        mDetailSegment.onListItemClicked(item);
        if (!isMultiPane()) {
            // Single-Pane Layout
            ActivityMultiPaneDetail.setDetailFragment(mDetailFragment);
            startActivity(new Intent(getActivity(), ActivityMultiPaneDetail.class));
        }
    }

    @Override
    public void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
        if(mListSegment.getSelectedPosition() == -1) {
            outState.putInt(SELECTED_ITEM, 0);
        } else {
            outState.putInt(SELECTED_ITEM, mListSegment.getSelectedPosition());
        }
    }

    @Override
    public void onConfigurationChanged(final Configuration newConfig) {
        // Remove both fragments before the device rotates...
        final FragmentManager supportFragmentManager = getActivity().getSupportFragmentManager();
        final FragmentTransaction transaction = supportFragmentManager.beginTransaction();
        transaction.remove(mListFragment);
        transaction.remove(mDetailFragment);
        transaction.commit();
        // change the configuration
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    private boolean isMultiPane() {
        return getResources().getBoolean(R.bool.multi_pane);
    }

    public static <T> FragmentMultiPane<T> build(Class<? extends Fragment> leftSide,
                                                 Class<? extends Fragment> rightSide,
                                                 Class<T> objectType) {
        Bundle bundle = new Bundle();
        bundle.putString(LEFT_PANE, leftSide.getName());
        bundle.putString(RIGHT_PANE, rightSide.getName());

        FragmentMultiPane<T> fragmentMultiPane = new FragmentMultiPane<>();
        fragmentMultiPane.setArguments(bundle);
        return fragmentMultiPane;
    }
}
