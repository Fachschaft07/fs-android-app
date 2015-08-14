package edu.hm.cs.fs.app.ui.job;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.fk07.R;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import edu.hm.cs.fs.app.database.error.IError;
import edu.hm.cs.fs.app.presenter.JobPresenter;
import edu.hm.cs.fs.app.util.BaseFragment;
import edu.hm.cs.fs.app.view.IJobView;
import edu.hm.cs.fs.common.model.Job;

/**
 * Created by FHellman on 10.08.2015.
 */
public class JobFragment extends BaseFragment<JobPresenter> implements IJobView,
        SwipeRefreshLayout.OnRefreshListener, JobAdapter.OnItemClickListener {
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

        mSwipeRefreshLayout.setOnRefreshListener(this);
        initSwipeRefreshLayout(mSwipeRefreshLayout);

        setPresenter(new JobPresenter(this));
        getPresenter().loadJobs();
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
        return R.string.job_offers;
    }

    @Override
    public void onRefresh() {
        getPresenter().loadJobs();
    }

    @Override
    public void showContent(@NonNull final List<Job> content) {
        mAdapter.setData(content);
    }

    @Override
    public void onItemClicked(@NonNull Job job) {
        JobDetailFragment fragment = new JobDetailFragment();
        Bundle arguments = new Bundle();
        arguments.putString(JobDetailFragment.ARGUMENT_TITLE, job.getTitle());
        fragment.setArguments(arguments);
        getMainActivity().getNavigator().goToDetail(fragment);
    }

    @Override
    public void onErrorSnackbar(@NonNull Snackbar snackbar, @NonNull IError error) {
        if (!error.isConnected()) {
            snackbar.setDuration(Snackbar.LENGTH_INDEFINITE);
            snackbar.setAction(R.string.retry, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onRefresh();
                }
            });
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
