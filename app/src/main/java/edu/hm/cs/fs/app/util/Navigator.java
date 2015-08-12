package edu.hm.cs.fs.app.util;

import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.fk07.R;

import edu.hm.cs.fs.app.ui.MainActivity;
import edu.hm.cs.fs.app.ui.job.JobDetailFragment;


public class Navigator {
    @NonNull
    private MainActivity mMainActivity;
    @NonNull
    protected final FragmentManager mFragmentManager;

    @IdRes
    protected final int mDefaultContainer;
    @IdRes
    protected int mDetailContainer = -1;

    /**
     * This constructor should be only called once per
     *
     * @param mainActivity     Your MainActivity
     * @param fragmentManager  Your FragmentManger
     * @param defaultContainer Your container id where the fragments should be placed
     */
    public Navigator(@NonNull final MainActivity mainActivity,
                     @NonNull final FragmentManager fragmentManager,
                     @IdRes final int defaultContainer) {
        mMainActivity = mainActivity;
        mFragmentManager = fragmentManager;
        mDefaultContainer = defaultContainer;
    }

    public void setDetailContainer(@IdRes final int mDetailContainer) {
        this.mDetailContainer = mDetailContainer;
    }

    public boolean hasDetailContainer() {
        return mDetailContainer != -1;
    }

    /**
     * @return the current active fragment. If no fragment is active it return null.
     */
    public Fragment getActiveFragment() {
        if (mFragmentManager.getBackStackEntryCount() == 0) {
            return null;
        }
        String tag = mFragmentManager
                .getBackStackEntryAt(mFragmentManager.getBackStackEntryCount() - 1).getName();
        return mFragmentManager.findFragmentByTag(tag);
    }

    /**
     * Pushes the fragment, and add it to the history (BackStack)
     *
     * @param fragment the fragment which
     */
    public void goTo(@NonNull final BaseFragment fragment) {
        swapFragment(fragment, mDefaultContainer);
    }

    /**
     * @param fragment
     */
    public void goToDetail(@NonNull final BaseFragment fragment) {
        if(hasDetailContainer()) {
            replaceFragment(fragment, mDetailContainer);
        } else {
            swapFragment(fragment, mDefaultContainer);
        }
    }

    private void swapFragment(@NonNull final BaseFragment fragment, @IdRes final int container) {
        mFragmentManager.beginTransaction()
                .addToBackStack(getName(fragment))
                .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left,
                        android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(container, fragment, getName(fragment))
                .commit();
        mFragmentManager.executePendingTransactions();
    }

    /**
     * This is just a helper method which returns the simple name of
     * the fragment.
     *
     * @param fragment that get added to the history (BackStack)
     * @return the simple name of the given fragment
     */
    protected String getName(@NonNull final BaseFragment fragment) {
        return fragment.getClass().getSimpleName();
    }

    /**
     * Set the new root fragment. If there is any entry in the history (BackStack)
     * it will be deleted.
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
     * Replace the current fragment with the given one, without to add it to backstack.
     * So when the users navigates away from the given fragment it will not appaer in
     * the history.
     *
     * @param fragment the new fragment
     */
    private void replaceFragment(@NonNull final BaseFragment fragment,
                                 @IdRes final int container) {
        mFragmentManager.beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .replace(container, fragment, getName(fragment))
                .commit();
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
     * Goes the whole history back until to the first fragment in the history.
     * It would be the same if the user would click so many times the back button until
     * he reach the first fragment of the app.
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
        while (mFragmentManager.popBackStackImmediate()) ;
    }
}