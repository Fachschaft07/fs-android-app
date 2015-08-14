package edu.hm.cs.fs.app.ui.meal;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.fk07.R;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import edu.hm.cs.fs.app.database.error.IError;
import edu.hm.cs.fs.app.presenter.MealPresenter;
import edu.hm.cs.fs.app.util.BaseFragment;
import edu.hm.cs.fs.app.view.IMealView;
import edu.hm.cs.fs.common.model.Meal;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

/**
 * Created by Fabio on 12.07.2015.
 */
public class MealFragment extends BaseFragment<MealPresenter> implements IMealView, SwipeRefreshLayout.OnRefreshListener {
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.swipeContainer)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @Bind(R.id.stickyList)
    StickyListHeadersListView mListView;
    private MealAdapter mAdapter;

    @Override
    public void onViewCreated(final View view, final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        mToolbar.setNavigationIcon(getMainActivity().getToolbar().getNavigationIcon());
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMainActivity().openDrawer();
            }
        });

        mAdapter = new MealAdapter(getActivity());
        mListView.setAdapter(mAdapter);

        mSwipeRefreshLayout.setOnRefreshListener(this);
        initSwipeRefreshLayout(mSwipeRefreshLayout);

        setPresenter(new MealPresenter(this));
        getPresenter().loadMeals();
    }

    @Override
    public boolean hasCustomToolbar() {
        return true;
    }

    @Override
    protected int getTitle() {
        return R.string.mensa;
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_meal;
    }

    @Override
    public void showContent(@NonNull final List<Meal> content) {
        mAdapter.clear();
        for (Meal meal : content) {
            mAdapter.add(meal);
        }
    }

    @Override
    public void onRefresh() {
        getPresenter().loadMeals();
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
