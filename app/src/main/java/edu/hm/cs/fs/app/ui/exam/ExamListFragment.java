package edu.hm.cs.fs.app.ui.exam;

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
import edu.hm.cs.fs.app.presenter.ExamListPresenter;
import edu.hm.cs.fs.app.ui.BaseFragment;
import edu.hm.cs.fs.app.ui.PerActivity;
import edu.hm.cs.fs.common.model.Exam;

@PerActivity
public class ExamListFragment extends BaseFragment<ExamListComponent, ExamListPresenter> implements ExamListView, ExamListAdapter.OnPinListener {

    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Bind(R.id.swipeContainer)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Bind(R.id.listView)
    RecyclerView mListView;

    private ExamListAdapter mAdapter;

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

        /*
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
                    //getPresenter().search(newText);
                    return true;
                }
            });
        }
        */

        mAdapter = new ExamListAdapter(getActivity());
        mAdapter.setListener(this);
        mListView.setAdapter(mAdapter);
        mListView.setLayoutManager(new LinearLayoutManager(getActivity()));

        initSwipeRefreshLayout(mSwipeRefreshLayout);

        getPresenter().loadExams(false);
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_exam;
    }

    @Override
    public boolean hasCustomToolbar() {
        return true;
    }

    @Override
    public void onRefresh() {
        getPresenter().loadExams(true);
    }

    @Override
    public void onPin(@NonNull Exam exam) {

    }

    @Override
    protected ExamListComponent onCreateNonConfigurationComponent() {
        return DaggerExamListComponent.builder()
                .appComponent(App.getAppComponent(getMainActivity()))
                .build();
    }

    @Override
    public void clear() {

    }

    @Override
    public void add(@NonNull Exam item) {

    }
}
