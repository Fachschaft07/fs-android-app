package edu.hm.cs.fs.app.ui.lostfound;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.fk07.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import edu.hm.cs.fs.app.App;
import edu.hm.cs.fs.app.presenter.LostFoundListPresenter;
import edu.hm.cs.fs.app.ui.BaseFragment;
import edu.hm.cs.fs.app.ui.PerActivity;
import edu.hm.cs.fs.common.model.LostFound;

/**
 * @author Fabio
 */
@PerActivity
public class LostFoundListFragment extends BaseFragment<LostFoundListComponent, LostFoundListPresenter> implements LostFoundListView {

    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Bind(R.id.swipeContainer)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Bind(R.id.listView)
    RecyclerView mListView;

    private LostFoundListAdapter mAdapter;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        mToolbar.setNavigationIcon(getMainActivity().getToolbar().getNavigationIcon());
        mToolbar.setNavigationOnClickListener(v -> getMainActivity().openDrawer());

        mAdapter = new LostFoundListAdapter(getActivity());
        mListView.setAdapter(mAdapter);
        mListView.setLayoutManager(new LinearLayoutManager(getActivity()));

        initSwipeRefreshLayout(mSwipeRefreshLayout);

        getPresenter().loadLostFound();
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_lostfound;
    }

    @Override
    public boolean hasCustomToolbar() {
        return true;
    }

    @Override
    public void onRefresh() {
        getPresenter().loadLostFound();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    protected LostFoundListComponent onCreateNonConfigurationComponent() {
        return DaggerLostFoundListComponent.builder()
                .appComponent(App.getAppComponent(getMainActivity()))
                .build();
    }

    @Override
    public void clear() {

    }

    @Override
    public void add(@NonNull LostFound item) {

    }
}
