package edu.hm.cs.fs.app.ui.timetable.wizard;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.fk07.R;

import java.util.Arrays;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnItemClick;

/**
 * Created by Fabio on 29.03.2015.
 */
public abstract class BaseWizardFragment<T> extends Fragment {
    @InjectView(R.id.wizardSideTitle)
    TextView mTitle;
    @InjectView(android.R.id.list)
    ListView mList;

    private WizardListAdapter mAdapter;

    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wizard_page, container, false);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onViewCreated(final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mTitle.setText(getTitle());

        mAdapter = new WizardListAdapter(getActivity());
        mList.setAdapter(mAdapter);
    }

    public abstract int getTitle();

    public abstract void onItemText(T item, TextView textView);

    public void addItems(List<T> items) {
        for (T item : items) {
            mAdapter.add(item);
        }
    }

    public void addItems(T... items) {
        addItems(Arrays.asList(items));
    }

    @OnItemClick(android.R.id.list)
    public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {

    }

    private final class WizardListAdapter extends ArrayAdapter<T> {
        public WizardListAdapter(final Context context) {
            super(context, android.R.layout.simple_list_item_1);
        }

        @Override
        public View getView(final int position, final View convertView, final ViewGroup parent) {
            return super.getView(position, convertView, parent);
        }
    }
}
