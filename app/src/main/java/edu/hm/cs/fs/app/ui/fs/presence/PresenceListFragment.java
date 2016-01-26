package edu.hm.cs.fs.app.ui.fs.presence;

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
import edu.hm.cs.fs.app.presenter.PresenceListPresenter;
import edu.hm.cs.fs.app.ui.BaseFragment;
import edu.hm.cs.fs.app.ui.PerActivity;
import edu.hm.cs.fs.common.model.Presence;

@PerActivity
public class PresenceListFragment extends BaseFragment<PresenceListComponent, PresenceListPresenter> implements PresenceListView {

    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Bind(R.id.swipeContainer)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Bind(R.id.listView)
    RecyclerView mListView;

    private PresenceListAdapter mAdapter;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        mToolbar.setNavigationIcon(getMainActivity().getToolbar().getNavigationIcon());
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMainActivity().openDrawer();
            }
        });

        mAdapter = new PresenceListAdapter(getActivity());
        mListView.setAdapter(mAdapter);
        mListView.setLayoutManager(new LinearLayoutManager(getActivity()));

        initSwipeRefreshLayout(mSwipeRefreshLayout);

        getPresenter().loadPresence();
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_presence;
    }

    @Override
    public boolean hasCustomToolbar() {
        return true;
    }

    @Override
    protected int getTitle() {
        return R.string.student_council;
    }

    @Override
    public void onRefresh() {
        getPresenter().loadPresence();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    protected PresenceListComponent onCreateNonConfigurationComponent() {
        return DaggerPresenceListComponent.builder()
                .appComponent(App.getAppComponent(getMainActivity()))
                .build();
    }

    @Override
    public void clear() {
        mAdapter.clear();
    }

    @Override
    public void add(@NonNull Presence item) {
        mAdapter.add(item);
    }
}
