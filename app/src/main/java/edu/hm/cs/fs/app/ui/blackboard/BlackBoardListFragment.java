package edu.hm.cs.fs.app.ui.blackboard;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.fk07.R;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import edu.hm.cs.fs.app.App;
import edu.hm.cs.fs.app.presenter.BlackBoardListPresenter;
import edu.hm.cs.fs.app.ui.BaseFragment;
import edu.hm.cs.fs.app.ui.MainActivity;
import edu.hm.cs.fs.common.model.BlackboardEntry;

public class BlackBoardListFragment extends BaseFragment<BlackboardListComponent, BlackBoardListPresenter> implements
        BlackBoardListView, BlackBoardListAdapter.OnItemClickListener {

    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Bind(R.id.swipeContainer)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Bind(R.id.listView)
    RecyclerView mListView;

    private BlackBoardListAdapter mAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getComponent().inject(this);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        mToolbar.setNavigationIcon(getMainActivity().getToolbar().getNavigationIcon());
        mToolbar.setNavigationOnClickListener(v -> getMainActivity().openDrawer());
        mToolbar.inflateMenu(R.menu.blackboard);

        final MenuItem searchItem = mToolbar.getMenu().findItem(R.id.menu_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        if (searchView != null) {
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    getPresenter().search(newText);
                    return true;
                }
            });
        }

        mAdapter = new BlackBoardListAdapter(getActivity());
        mAdapter.setListener(this);
        mListView.setAdapter(mAdapter);
        mListView.setLayoutManager(new LinearLayoutManager(getActivity()));

        initSwipeRefreshLayout(mSwipeRefreshLayout);

        getPresenter().loadBlackBoard(false);
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_blackboard;
    }

    @Override
    public boolean hasCustomToolbar() {
        return true;
    }

    @Override
    public void onRefresh() {
        getPresenter().loadBlackBoard(true);
    }

    @Override
    public void clear() {
        mAdapter.clear();
    }

    @Override
    public void add(@NonNull BlackboardEntry blackboardEntry) {
        mAdapter.add(blackboardEntry);
    }

    @Override
    public void onItemClicked(@NonNull BlackboardEntry entry) {
        BlackBoardDetailFragment fragment = new BlackBoardDetailFragment();
        Bundle arguments = new Bundle();
        arguments.putString(BlackBoardDetailFragment.ARGUMENT_ID, entry.getId());
        fragment.setArguments(arguments);
        MainActivity.getNavigator().goTo(fragment);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void showError(@NonNull Throwable error) {

    }

    @Override
    protected BlackboardListComponent onCreateNonConfigurationComponent() {
        return DaggerBlackboardListComponent.builder()
                .appComponent(App.getAppComponent(getMainActivity()))
                .build();
    }
}
