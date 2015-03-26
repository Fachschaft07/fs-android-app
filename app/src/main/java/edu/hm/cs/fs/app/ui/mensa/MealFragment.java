package edu.hm.cs.fs.app.ui.mensa;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.fk07.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import edu.hm.cs.fs.app.datastore.helper.Callback;
import edu.hm.cs.fs.app.datastore.helper.MealHelper;
import edu.hm.cs.fs.app.datastore.model.Meal;
import edu.hm.cs.fs.app.datastore.model.constants.Additive;
import edu.hm.cs.fs.app.datastore.model.constants.StudentWorkMunich;
import edu.hm.cs.fs.app.util.DataUtils;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

/**
 * Created by Fabio on 04.03.2015.
 */
public class MealFragment extends Fragment implements AdapterView.OnItemClickListener{
    @InjectView(R.id.stickyList)
    StickyListHeadersListView mListView;
    private MealAdapter mAdapter;

    private MenuItem mRefresh;

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_meal, container, false);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onViewCreated(final View view, final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAdapter = new MealAdapter(getActivity());
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);

        refresh();

        // TODO Let the user decide which mensa/stucafé he want's to see...
        // For this feature --> add a Spinner to the Toolbar where the user can pick the target

        MealHelper.listAll(getActivity(), StudentWorkMunich.MENSA_LOTHSTRASSE, new Callback<List<Meal>>() {
            @Override
            public void onResult(final List<Meal> result) {
                mAdapter.clear();
                Date today = new Date();
                for (Meal meal : result) {
                    if(meal.getDate().after(today) || DataUtils.isSameDate(meal.getDate(), today)) {
                        mAdapter.add(meal);
                    }
                }

                refresh();
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(final Menu menu, final MenuInflater inflater) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            mRefresh = menu.add(R.string.refresh);
            mRefresh.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
            mRefresh.setActionView(R.layout.toolbar_item_refresh);
            mRefresh.setVisible(false);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        final Meal meal = mAdapter.getItem(position);

        List<String> items = new ArrayList<>();
        for (Additive additive : meal.getAdditives()) {
            switch (additive) {
                case DYE:
                    items.add(getString(R.string.dye));
                    break;
                case PRESERVATIVE:
                    items.add(getString(R.string.preservative));
                    break;
                case ANTIOXIDANT:
                    items.add(getString(R.string.antioxidant));
                    break;
                case FLAVOR_ENHANCERS:
                    items.add(getString(R.string.flavor_enhancers));
                    break;
                case SULPHURED:
                    items.add(getString(R.string.sulphured));
                    break;
                case BLACKENED:
                    items.add(getString(R.string.blackened));
                    break;
                case PHOSPHATE:
                    items.add(getString(R.string.phosphate));
                    break;
                case SWEETENERS:
                    items.add(getString(R.string.sweeteners));
                    break;
                case PHENYLALANINQUELLE:
                    items.add(getString(R.string.phenylalaninquelle));
                    break;
                case SUGAR_AND_SWEETENERS:
                    items.add(getString(R.string.sugar_and_sweeteners));
                    break;
                case ALCOHOL:
                    items.add(getString(R.string.alcohol));
                    break;
                case PIG:
                    items.add(getString(R.string.pig));
                    break;
                case BEEF:
                    items.add(getString(R.string.beef));
                    break;
            }
        }

        if(!items.isEmpty()) {
            new MaterialDialog.Builder(getActivity())
                    .title(R.string.additives)
                    .items(items.toArray(new String[items.size()]))
                    .show();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    private void refresh() {
        if(mRefresh != null) {
            mRefresh.setVisible(!mRefresh.isVisible());
        }
    }
}
