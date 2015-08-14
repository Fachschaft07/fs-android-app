package edu.hm.cs.fs.app.ui.job;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Spanned;
import android.view.View;
import android.widget.TextView;

import com.fk07.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import edu.hm.cs.fs.app.database.error.IError;
import edu.hm.cs.fs.app.presenter.JobDetailPresenter;
import edu.hm.cs.fs.app.util.BaseFragment;
import edu.hm.cs.fs.app.view.IJobDetailView;

/**
 * Created by FHellman on 10.08.2015.
 */
public class JobDetailFragment extends BaseFragment<JobDetailPresenter> implements IJobDetailView, SwipeRefreshLayout.OnRefreshListener {
    public static final String ARGUMENT_TITLE = "id";
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

    private String mJobTitle;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setEnabled(false);
        initSwipeRefreshLayout(mSwipeRefreshLayout);

        getToolbar().setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                close();
            }
        });

        mJobTitle = getArguments().getString(ARGUMENT_TITLE);
        setPresenter(new JobDetailPresenter(this));
        getPresenter().loadJob(mJobTitle);
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
    public void showSubject(@NonNull Spanned subject) {
        mSubject.setText(subject);
    }

    @Override
    public void showProvider(@NonNull String provider) {
        mProvider.setText(provider);
    }

    @Override
    public void showDescription(@NonNull Spanned description) {
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
        getPresenter().loadJob(mJobTitle);
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
    public void close() {
        getMainActivity().getNavigator().goOneBack();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
