package edu.hm.cs.fs.app.ui.mensa;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;

import com.fk07.R;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import edu.hm.cs.fs.app.presenter.MealPresenter;
import edu.hm.cs.fs.app.util.BaseFragment;
import edu.hm.cs.fs.app.view.IMealView;
import edu.hm.cs.fs.common.model.Meal;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

/**
 * Created by Fabio on 12.07.2015.
 */
public class MealFragment extends BaseFragment<MealPresenter> implements IMealView, SwipeRefreshLayout.OnRefreshListener {
    @Bind(R.id.swipeContainer) SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.stickyList) StickyListHeadersListView listView;
    private MealAdapter adapter;

    @Override
    public void onViewCreated(final View view, final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        adapter = new MealAdapter(getActivity());
        listView.setAdapter(adapter);

        setPresenter(new MealPresenter(this));
        getPresenter().loadMeals();

        swipeRefreshLayout.setOnRefreshListener(this);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            swipeRefreshLayout.setColorSchemeColors(android.R.color.holo_blue_bright,
                    android.R.color.holo_green_light,
                    android.R.color.holo_orange_light,
                    android.R.color.holo_red_light);
        }
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_meal;
    }

    @Override
    public void showContent(final List<Meal> content) {
        adapter.clear();
        for (Meal meal : content) {
            adapter.add(meal);
        }
    }

    @Override
    public void onRefresh() {
        getPresenter().loadMeals();
    }

    @Override
    public void showLoading() {
        swipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void hideLoading() {
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showError(final String error) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
