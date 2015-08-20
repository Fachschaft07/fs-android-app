package edu.hm.cs.fs.app.ui.home;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.dexafree.materialList.card.Card;
import com.dexafree.materialList.view.IMaterialListAdapter;
import com.dexafree.materialList.view.MaterialListView;
import com.fk07.R;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import edu.hm.cs.fs.app.presenter.HomePresenter;
import edu.hm.cs.fs.app.ui.BaseFragment;
import edu.hm.cs.fs.app.view.IHomeView;

/**
 * @author Fabio
 */
public class HomeFragment extends BaseFragment<HomePresenter> implements IHomeView,
        SwipeRefreshLayout.OnRefreshListener {
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.swipeContainer)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @Bind(R.id.listView)
    MaterialListView mListView;

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

        mSwipeRefreshLayout.setOnRefreshListener(this);
        initSwipeRefreshLayout(mSwipeRefreshLayout);

        setPresenter(new HomePresenter(getActivity(), this));
        getPresenter().loadHappenings();
    }

    @Override
    public boolean hasCustomToolbar() {
        return true;
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_home;
    }

    @Override
    public void onRefresh() {
        getPresenter().loadHappenings();
    }

    @Override
    public void showContent(@NonNull Card card) {
        mListView.add(card);
    }

    @Override
    public void clear() {
        mListView.clear();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
