package edu.hm.cs.fs.app.ui.job;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import com.fk07.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import edu.hm.cs.fs.app.presenter.JobDetailPresenter;
import edu.hm.cs.fs.app.util.BaseFragment;
import edu.hm.cs.fs.app.view.IJobDetailView;

/**
 * Created by FHellman on 10.08.2015.
 */
public class JobDetailFragment extends BaseFragment<JobDetailPresenter> implements IJobDetailView, SwipeRefreshLayout.OnRefreshListener {
    @Bind(R.id.swipeContainer)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @Bind(R.id.textSubject)
    TextView mSubject;
    @Bind(R.id.textProvider)
    TextView mProvider;
    @Bind(R.id.textDescription)
    TextView mDescription;
    @Bind(R.id.textUrl)
    TextView mUrl;
    @Bind(R.id.textAuthor)
    TextView mAuthor;

    private String mJobId;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        mSwipeRefreshLayout.setOnRefreshListener(this);
        initSwipeRefreshLayout(mSwipeRefreshLayout);

        getToolbar().setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                close();
            }
        });

        mJobId = getArguments().getString("id");
        setPresenter(new JobDetailPresenter(this));
        getPresenter().loadJob(mJobId);
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
    protected int getLayout() {
        return R.layout.fragment_job_detail;
    }

    @Override
    public void showSubject(@NonNull String subject) {
        mSubject.setText(subject);
    }

    @Override
    public void showProvider(@NonNull String provider) {
        mProvider.setText(provider);
    }

    @Override
    public void showDescription(@NonNull String description) {
        mDescription.setText(description);
    }

    @Override
    public void showUrl(@NonNull String url) {
        mUrl.setText(url);
    }

    @Override
    public void showAuthor(@NonNull String author) {
        mAuthor.setText(author);
    }

    @Override
    public void onRefresh() {
        getPresenter().loadJob(mJobId);
    }

    @Override
    public void showLoading() {
        mSwipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void hideLoading() {
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showError(@NonNull String error) {
        if(mSwipeRefreshLayout != null) {
            Snackbar.make(mSwipeRefreshLayout, error, Snackbar.LENGTH_LONG)
                    .setAction(R.string.retry, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            getPresenter().loadJob(mJobId);
                        }
                    }).show();
        }
    }

    @Override
    public void close() {
        getMainActivity().getNavigator().goOneBack();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
