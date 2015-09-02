package edu.hm.cs.fs.app.ui.blackboard;

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
import edu.hm.cs.fs.app.presenter.BlackBoardDetailPresenter;
import edu.hm.cs.fs.app.ui.BaseFragment;
import edu.hm.cs.fs.app.view.IBlackBoardDetailView;

/**
 * Created by FHellman on 12.08.2015.
 */
public class BlackBoardDetailFragment extends BaseFragment<BlackBoardDetailPresenter> implements IBlackBoardDetailView, SwipeRefreshLayout.OnRefreshListener {

    public static final String ARGUMENT_ID = "id";

    @Bind(R.id.swipeContainer)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Bind(R.id.textSubject)
    TextView mSubject;

    @Bind(R.id.textGroups)
    TextView mGroups;

    @Bind(R.id.textDescription)
    TextView mDescription;

    @Bind(R.id.textUrl)
    TextView mUrl;

    @Bind(R.id.textAuthor)
    TextView mAuthor;

    private String mBlackBoardEntryId;

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

        mBlackBoardEntryId = getArguments().getString(ARGUMENT_ID);
        setPresenter(new BlackBoardDetailPresenter(this));
        getPresenter().loadBlackBoardEntry(mBlackBoardEntryId);
    }

    @Override
    public boolean hasCustomToolbar() {
        return true;
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_blackboard_detail;
    }

    @Override
    public boolean isDetailFragment() {
        return true;
    }

    @Override
    public void showSubject(@NonNull Spanned subject) {
        mSubject.setText(subject);
    }

    @Override
    public void showGroups(@NonNull String groups) {
        if (groups.length() > 0) {
            mGroups.setText(groups);
        } else {
            mGroups.setVisibility(View.GONE);
        }
    }

    @Override
    public void showDescription(@NonNull Spanned description) {
        mDescription.setText(description);
    }

    @Override
    public void showUrl(@NonNull String url) {
        if (url.length() > 0) {
            mUrl.setText(url);
        } else {
            mUrl.setVisibility(View.GONE);
        }
    }

    @Override
    public void showAuthor(@NonNull String author) {
        mAuthor.setText(author);
    }

    @Override
    public void onRefresh() {
        getPresenter().loadBlackBoardEntry(mBlackBoardEntryId);
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
