package edu.hm.cs.fs.app.ui.publictransport;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.fk07.R;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import edu.hm.cs.fs.app.database.error.IError;
import edu.hm.cs.fs.app.presenter.PublicTransportLothstrPresenter;
import edu.hm.cs.fs.app.ui.BaseFragment;
import edu.hm.cs.fs.app.view.IPublicTransportView;
import edu.hm.cs.fs.common.model.PublicTransport;

/**
 * Created by FHellman on 10.08.2015.
 */
public class LothstrFragment extends BaseFragment<PublicTransportLothstrPresenter> implements
        IPublicTransportView<PublicTransportLothstrPresenter>, SwipeRefreshLayout.OnRefreshListener {
    @Bind(R.id.swipeContainer)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @Bind(R.id.listView)
    RecyclerView mListView;
    private PublicTransportAdapter mAdapter;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        mAdapter = new PublicTransportAdapter(getActivity());
        mListView.setAdapter(mAdapter);
        mListView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mSwipeRefreshLayout.setOnRefreshListener(this);
        initSwipeRefreshLayout(mSwipeRefreshLayout);

        setPresenter(new PublicTransportLothstrPresenter(this));
        getPresenter().loadPublicTransports();
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_public_transport_list;
    }

    @Override
    public void onRefresh() {
        getPresenter().loadPublicTransports();
    }

    @Override
    public void showContent(@NonNull List<PublicTransport> content) {
        mAdapter.setData(content);
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
