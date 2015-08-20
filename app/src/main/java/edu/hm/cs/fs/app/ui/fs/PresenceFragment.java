package edu.hm.cs.fs.app.ui.fs;

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
import edu.hm.cs.fs.app.presenter.PresencePresenter;
import edu.hm.cs.fs.app.ui.BaseFragment;
import edu.hm.cs.fs.app.view.IPresenceView;
import edu.hm.cs.fs.common.model.Presence;

/**
 * Created by FHellman on 11.08.2015.
 */
public class PresenceFragment extends BaseFragment<PresencePresenter> implements IPresenceView, SwipeRefreshLayout.OnRefreshListener {
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.swipeContainer)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @Bind(R.id.listView)
    RecyclerView mListView;
    private PresenceAdapter mAdapter;

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

        mAdapter = new PresenceAdapter(getActivity());
        mListView.setAdapter(mAdapter);
        mListView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mSwipeRefreshLayout.setOnRefreshListener(this);
        initSwipeRefreshLayout(mSwipeRefreshLayout);

        setPresenter(new PresencePresenter(this));
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
    public void showContent(@NonNull List<Presence> content) {
        mAdapter.setData(content);
    }

    @Override
    public void onRefresh() {
        getPresenter().loadPresence();
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
