package edu.hm.cs.fs.app.ui.job;

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
import edu.hm.cs.fs.app.presenter.JobPresenter;
import edu.hm.cs.fs.app.ui.BaseFragment;
import edu.hm.cs.fs.app.ui.MainActivity;
import edu.hm.cs.fs.app.view.IJobView;
import edu.hm.cs.fs.common.model.simple.SimpleJob;

/**
 * Created by FHellman on 10.08.2015.
 */
public class JobFragment extends BaseFragment<JobPresenter> implements IJobView,
        JobAdapter.OnItemClickListener {

    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Bind(R.id.swipeContainer)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Bind(R.id.listView)
    RecyclerView mListView;

    private JobAdapter mAdapter;

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

        mAdapter = new JobAdapter(getActivity());
        mAdapter.setListener(this);
        mListView.setAdapter(mAdapter);
        mListView.setLayoutManager(new LinearLayoutManager(getActivity()));

        initSwipeRefreshLayout(mSwipeRefreshLayout);

        setPresenter(new JobPresenter(this));
        getPresenter().loadJobs(false);
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_job;
    }

    @Override
    public boolean hasCustomToolbar() {
        return true;
    }

    @Override
    protected int getTitle() {
        return R.string.jobs;
    }

    @Override
    public void onRefresh() {
        getPresenter().loadJobs(true);
    }

    @Override
    public void showContent(@NonNull final List<SimpleJob> content) {
        mAdapter.setData(content);
    }

    @Override
    public void onItemClicked(@NonNull SimpleJob job) {
        JobDetailFragment fragment = new JobDetailFragment();
        Bundle arguments = new Bundle();
        arguments.putString(JobDetailFragment.ARGUMENT_TITLE, job.getTitle());
        fragment.setArguments(arguments);
        MainActivity.getNavigator().goTo(fragment);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
