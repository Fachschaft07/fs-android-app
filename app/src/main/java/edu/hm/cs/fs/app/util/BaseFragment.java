package edu.hm.cs.fs.app.util;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fk07.R;

import butterknife.ButterKnife;
import edu.hm.cs.fs.app.presenter.IPresenter;
import edu.hm.cs.fs.app.ui.MainActivity;
import edu.hm.cs.fs.app.view.IView;
import edu.hm.cs.fs.common.constant.StudentWorkMunich;

public abstract class BaseFragment<P extends IPresenter> extends Fragment implements IView<P> {
    Toolbar mToolbar;
    private P presenter;

    public MainActivity getMainActivity() {
        return ((MainActivity) super.getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
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

    @Override
    public void setPresenter(@NonNull final P presenter) {
        this.presenter = presenter;
    }

    protected void setToolbar(@NonNull final View view) {
        if (hasCustomToolbar()) {
            mToolbar = ButterKnife.findById(view, getToolbarId());
            mToolbar.setTitle(getTitle());
            //mToolbar.setNavigationIcon(R.drawable.ic_menu);
            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getMainActivity().openDrawer();
                }
            });
        }
    }

    protected Toolbar getToolbar() {
        return mToolbar;
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

    public P getPresenter() {
        return presenter;
    }

    @Override
    public void showLoading() {
    }

    @Override
    public void hideLoading() {
    }

    @Override
    public void showError(@NonNull final String error) {
    }

    public void initSwipeRefreshLayout(SwipeRefreshLayout swipeRefreshLayout) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                    android.R.color.holo_green_light,
                    android.R.color.holo_orange_light,
                    android.R.color.holo_red_light);
        }
    }
}