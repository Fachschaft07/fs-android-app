package edu.hm.cs.fs.app.ui.fs.news;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.fk07.R;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import edu.hm.cs.fs.app.App;
import edu.hm.cs.fs.app.presenter.FsNewsListPresenter;
import edu.hm.cs.fs.app.ui.BaseFragment;
import edu.hm.cs.fs.app.ui.MainActivity;
import edu.hm.cs.fs.app.ui.PerActivity;
import edu.hm.cs.fs.common.model.News;

/**
 * @author Fabio
 */
@PerActivity
public class FsNewsListFragment extends BaseFragment<FsNewsListComponent, FsNewsListPresenter> implements FsNewsListView,
        FsNewsListAdapter.OnItemClickListener {

    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Bind(R.id.swipeContainer)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Bind(R.id.listView)
    RecyclerView mListView;

    private FsNewsListAdapter mAdapter;

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

        mAdapter = new FsNewsListAdapter(getActivity());
        mAdapter.setListener(this);
        mListView.setAdapter(mAdapter);
        mListView.setLayoutManager(new LinearLayoutManager(getActivity()));

        initSwipeRefreshLayout(mSwipeRefreshLayout);

        getPresenter().loadNews(false);
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_news;
    }

    @Override
    public boolean hasCustomToolbar() {
        return true;
    }

    @Override
    public void onRefresh() {
        getPresenter().loadNews(true);
    }

    @Override
    public void onItemClicked(@NonNull final News news) {
        final FsNewsDetailFragment fragment = new FsNewsDetailFragment();
        Bundle args = new Bundle();
        args.putString(FsNewsDetailFragment.ARGUMENT_TITLE, news.getTitle());
        fragment.setArguments(args);
        MainActivity.getNavigator().goTo(fragment);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    protected FsNewsListComponent onCreateNonConfigurationComponent() {
        return DaggerFsNewsListComponent.builder()
                .appComponent(App.getAppComponent(getMainActivity()))
                .build();
    }

    @Override
    public void clear() {
        mAdapter.clear();
    }

    @Override
    public void add(@NonNull News item) {
        mAdapter.add(item);
    }
}
