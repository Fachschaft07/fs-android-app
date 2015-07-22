package edu.hm.cs.fs.app.util;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fk07.R;

import butterknife.ButterKnife;
import edu.hm.cs.fs.app.presenter.IPresenter;
import edu.hm.cs.fs.app.ui.MainActivity;
import edu.hm.cs.fs.app.view.IView;

public abstract class BaseFragment<P extends IPresenter> extends Fragment implements IView<P> {
    Toolbar mToolbar;
    private P presenter;

    public MainActivity getMainActivity(){
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
    public void setPresenter(final P presenter) {
        this.presenter = presenter;
    }

    protected void setToolbar(View view) {
        if(!hasCustomToolbar()) return;
        mToolbar = ButterKnife.findById(view,getToolbarId());
        mToolbar.setTitle(getTitle());
        //mToolbar.setNavigationIcon(R.drawable.ic_menu);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMainActivity().openDrawer();
            }
        });
    }

    protected @IdRes int getToolbarId(){
        return R.id.toolbar;
    }

    public boolean hasCustomToolbar(){
        return false;
    }

    protected @StringRes int getTitle(){
        return R.string.not_title_set;
    }

    protected abstract  @LayoutRes int getLayout();

    public P getPresenter() {
        return presenter;
    }
}