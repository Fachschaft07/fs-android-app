package edu.hm.cs.fs.app.ui.fs.news;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.TextView;

import com.fk07.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import edu.hm.cs.fs.app.presenter.FsNewsDetailPresenter;
import edu.hm.cs.fs.app.ui.BaseFragment;
import edu.hm.cs.fs.app.ui.MainActivity;
import edu.hm.cs.fs.app.view.IFsNewsDetailView;

/**
 * @author Fabio
 */
public class FsNewsDetailFragment extends BaseFragment<FsNewsDetailPresenter>
        implements IFsNewsDetailView {

    public static final String ARGUMENT_TITLE = "title";

    @Bind(R.id.swipeContainer)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Bind(R.id.textTitle)
    TextView mTitle;

    @Bind(R.id.textDate)
    TextView mDate;

    @Bind(R.id.textDescription)
    TextView mDescription;

    @Bind(R.id.textLink)
    TextView mLink;

    private String mNewsTitle;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        mSwipeRefreshLayout.setEnabled(false);
        initSwipeRefreshLayout(mSwipeRefreshLayout);

        getToolbar().setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                close();
            }
        });

        mNewsTitle = getArguments().getString(ARGUMENT_TITLE);
        setPresenter(new FsNewsDetailPresenter(this));
        getPresenter().loadNews(mNewsTitle);
    }
    @Override
    protected int getLayout() {
        return R.layout.fragment_news_detail;
    }

    @Override
    public boolean isDetailFragment() {
        return true;
    }

    @Override
    public boolean hasCustomToolbar() {
        return true;
    }

    @Override
    public void onRefresh() {
        getPresenter().loadNews(mNewsTitle);
    }

    @Override
    public void showTitle(@NonNull final String title) {
        mTitle.setText(title);
    }

    @Override
    public void showDescription(@NonNull final String description) {
        mDescription.setText(description);
    }

    @Override
    public void showDate(@NonNull final String date) {
        mDate.setText(date);
    }

    @Override
    public void showLink(@NonNull final String link) {
        mLink.setText(link);
    }

    @Override
    public void close() {
        MainActivity.getNavigator().goOneBack();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
