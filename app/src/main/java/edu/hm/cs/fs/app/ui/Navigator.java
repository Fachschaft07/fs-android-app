package edu.hm.cs.fs.app.ui;

import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.view.View;

import com.fk07.R;


/**
 * @author Fabio
 */
public class Navigator {
    @NonNull
    private MainActivity mMainActivity;

    @NonNull
    protected final FragmentManager mFragmentManager;
    @IdRes
    protected final int mDefaultContainer;
    @IdRes
    protected final int mDetailContainer;

    /**
     * This constructor should be only called once per
     *
     * @param mainActivity     Your MainActivity
     * @param fragmentManager  Your FragmentManger
     * @param defaultContainer Your container id where the fragments should be placed
     */
    public Navigator(@NonNull final MainActivity mainActivity,
                     @NonNull final FragmentManager fragmentManager,
                     @IdRes final int defaultContainer,
                     @IdRes final int detailContainer) {
        mMainActivity = mainActivity;
        mFragmentManager = fragmentManager;
        mDefaultContainer = defaultContainer;
        mDetailContainer = detailContainer;
    }

    private boolean isMultiPaneLayout() {
        return mMainActivity.findViewById(mDetailContainer) != null;
    }

    /**
     * @return the current active fragment. If no fragment is active it return null.
     */
    public BaseFragment getActiveFragment() {
        return (BaseFragment) mFragmentManager.findFragmentById(mDefaultContainer);
    }

    /**
     * Pushes the fragment, and addEmpty it to the history (BackStack)
     *
     * @param fragment the fragment which
     */
    public void goTo(@NonNull final Fragment fragment) {
        if (fragment instanceof BaseFragment && isMultiPaneLayout()
                && ((BaseFragment) fragment).isDetailFragment()) {
            replaceFragment((BaseFragment) fragment, mDetailContainer);
        } else {
            mFragmentManager.beginTransaction()
                    .addToBackStack(getName(fragment))
                    .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left,
                            android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .replace(mDefaultContainer, fragment, getName(fragment))
                    .commitAllowingStateLoss();
            mFragmentManager.executePendingTransactions();
        }
    }

    /**
     * This is just a helper method which returns the simple name of the fragment.
     *
     * @param fragment that get added to the history (BackStack)
     * @return the simple name of the given fragment
     */
    protected String getName(@NonNull final Fragment fragment) {
        return fragment.getClass().getSimpleName();
    }

    /**
     * Set the new root fragment. If there is any entry in the history (BackStack) it will be
     * deleted.
     *
     * @param startFragment the new root fragment
     */
    public void setRootFragment(@NonNull final BaseFragment startFragment) {
        if (getSize() > 0) {
            this.clearHistory();
        }
        this.replaceFragment(startFragment, mDefaultContainer);
    }

    /**
     * Replace the current fragment with the given one, without to addEmpty it to backstack. So when
     * the users navigates away from the given fragment it will not appaer in the history.
     *
     * @param fragment the new fragment
     */
    private void replaceFragment(@NonNull final BaseFragment fragment, @IdRes final int container) {
        if (isMultiPaneLayout()) {
            if (fragment.isDetailFragment()) {
                mMainActivity.findViewById(mDetailContainer).setVisibility(View.VISIBLE);
            } else {
                mMainActivity.findViewById(mDetailContainer).setVisibility(View.GONE);
            }
        }
        ActionBar actionBar = mMainActivity.getSupportActionBar();
        if (actionBar != null) {
            if (fragment.hasCustomToolbar()) {
                actionBar.hide();
            } else {
                actionBar.show();
            }
        }

        mFragmentManager.beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(container, fragment, getName(fragment))
                .commitAllowingStateLoss();
        mFragmentManager.executePendingTransactions();
    }

    /**
     * Goes one entry back in the history
     */
    public void goOneBack() {
        mFragmentManager.popBackStackImmediate();
    }

    /**
     * @return The current size of the history (BackStack)
     */
    public int getSize() {
        return mFragmentManager.getBackStackEntryCount();
    }

    /**
     * @return True if no Fragment is in the History (BackStack)
     */
    public boolean isEmpty() {
        return getSize() == 0;
    }

    /**
     * Goes the whole history back until to the first fragment in the history. It would be the same
     * if the user would click so many times the back button until he reach the first fragment of
     * the app.
     */
    public void gotToTheRootFragmentBack() {
        for (int i = 0; i <= mFragmentManager.getBackStackEntryCount(); ++i) {
            goOneBack();
        }
    }

    /**
     * Clears the whole history so it will no BackStack entry there any more.
     */
    public void clearHistory() {
        //noinspection StatementWithEmptyBody - it works as wanted
        boolean run = true;
        while (run) {
            run = mFragmentManager.popBackStackImmediate();
        }
    }
}