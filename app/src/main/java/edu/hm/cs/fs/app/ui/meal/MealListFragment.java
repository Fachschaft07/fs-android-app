package edu.hm.cs.fs.app.ui.meal;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.fk07.R;

import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

import butterknife.Bind;
import butterknife.ButterKnife;
import edu.hm.cs.fs.app.App;
import edu.hm.cs.fs.app.presenter.MealListPresenter;
import edu.hm.cs.fs.app.ui.BaseFragment;
import edu.hm.cs.fs.app.ui.PerActivity;
import edu.hm.cs.fs.common.constant.Additive;
import edu.hm.cs.fs.common.model.Meal;

@PerActivity
public class MealListFragment extends BaseFragment<MealListComponent, MealListPresenter> implements MealListView,
        MealListAdapter.OnMealClickListener {

    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Bind(R.id.swipeContainer)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Bind(R.id.stickyList)
    StickyListHeadersListView mListView;

    private MealListAdapter mAdapter;

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

        mAdapter = new MealListAdapter(getActivity());
        mAdapter.setOnMealClickListener(this);
        mListView.setAdapter(mAdapter);

        initSwipeRefreshLayout(mSwipeRefreshLayout);

        getPresenter().loadMeals(false);
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
    public void onMealClick(@NonNull Meal meal) {
        if (!meal.getAdditives().isEmpty()) {
            new MaterialDialog.Builder(getActivity()).title(R.string.additional_info).adapter(new ArrayAdapter<Additive>(getActivity(), android.R.layout.simple_list_item_1, meal.getAdditives()) {
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    final View view = super.getView(position, convertView, parent);
                    final String additive;
                    switch (getItem(position)) {
                        case ALCOHOL:
                            additive = getString(R.string.alcohol);
                            break;
                        case ANTIOXIDANT:
                            additive = getString(R.string.antioxidant);
                            break;
                        case BEEF:
                            additive = getString(R.string.beef);
                            break;
                        case BLACKENED:
                            additive = getString(R.string.blackened);
                            break;
                        case DYE:
                            additive = getString(R.string.dye);
                            break;
                        case FLAVOR_ENHANCERS:
                            additive = getString(R.string.flavor_enhancers);
                            break;
                        case PHENYLALANINQUELLE:
                            additive = getString(R.string.phenylalaninquelle);
                            break;
                        case PHOSPHATE:
                            additive = getString(R.string.phosphate);
                            break;
                        case PIG:
                            additive = getString(R.string.pig);
                            break;
                        case PRESERVATIVE:
                            additive = getString(R.string.preservative);
                            break;
                        case SUGAR_AND_SWEETENERS:
                            additive = getString(R.string.sugar_and_sweeteners);
                            break;
                        case SULPHURED:
                            additive = getString(R.string.sulphured);
                            break;
                        case SWEETENERS:
                            additive = getString(R.string.sweeteners);
                            break;
                        default:
                            additive = getItem(position).toString();
                            break;
                    }
                    ((TextView) view.findViewById(android.R.id.text1)).setText(additive);
                    return view;
                }
            }, null).show();
        }
    }

    @Override
    public void onRefresh() {
        getPresenter().loadMeals(true);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    protected MealListComponent onCreateNonConfigurationComponent() {
        return DaggerMealListComponent.builder()
                .appComponent(App.getAppComponent(getMainActivity()))
                .build();
    }

    @Override
    public void clear() {

    }

    @Override
    public void add(@NonNull Meal item) {

    }
}
