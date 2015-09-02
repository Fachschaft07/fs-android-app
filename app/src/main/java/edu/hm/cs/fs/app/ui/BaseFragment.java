package edu.hm.cs.fs.app.ui;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fk07.R;

import butterknife.ButterKnife;
import edu.hm.cs.fs.app.database.error.IError;
import edu.hm.cs.fs.app.presenter.IPresenter;
import edu.hm.cs.fs.app.view.IView;

/**
 * @author Fabio
 */
public abstract class BaseFragment<P extends IPresenter> extends Fragment implements IView {

    Toolbar mToolbar;

    private P presenter;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    private boolean mRefresh;

    private View mViewError;

    public MainActivity getMainActivity() {
        return ((MainActivity) super.getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(getLayout(), container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setToolbar(view);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    protected Toolbar getToolbar() {
        return mToolbar;
    }

    protected void setToolbar(@NonNull final View view) {
        if (hasCustomToolbar()) {
            mToolbar = ButterKnife.findById(view, getToolbarId());
            if (getTitle() != R.string.not_title_set) {
                mToolbar.setTitle(getTitle());
            }
            //mToolbar.setNavigationIcon(R.drawable.ic_menu);
            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getMainActivity().openDrawer();
                }
            });
        }
    }

    @IdRes
    protected int getToolbarId() {
        return R.id.toolbar;
    }

    public boolean hasCustomToolbar() {
        return false;
    }

    @StringRes
    protected int getTitle() {
        return R.string.not_title_set;
    }

    @LayoutRes
    protected abstract int getLayout();

    public boolean isDetailFragment() {
        return false;
    }

    public P getPresenter() {
        return presenter;
    }

    /**
     * Sets the presenter to communicate with.
     *
     * @param presenter to communicate with.
     */
    public void setPresenter(@NonNull final P presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showLoading() {
        mRefresh = true;
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    mSwipeRefreshLayout.setRefreshing(mRefresh);
                }
            });
        }
    }

    @Override
    public void hideLoading() {
        mRefresh = false;
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void showError(@NonNull final IError error) {
        if (mViewError != null && getActivity() != null) {
            final Snackbar snackbar = Snackbar.make(mViewError, error.getMessage(getActivity()), Snackbar.LENGTH_LONG);
            onErrorSnackbar(snackbar, error);
            snackbar.show();
        }
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public void initSwipeRefreshLayout(@NonNull final SwipeRefreshLayout swipeRefreshLayout) {
        mSwipeRefreshLayout = swipeRefreshLayout;
        initErrorSnackbar(mSwipeRefreshLayout);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light, android.R.color.holo_orange_light, android.R.color.holo_red_light);
        }
    }

    public void initErrorSnackbar(@NonNull final View view) {
        mViewError = view;
    }

    public void onErrorSnackbar(@NonNull final Snackbar snackbar, @NonNull final IError error) {
    }
}