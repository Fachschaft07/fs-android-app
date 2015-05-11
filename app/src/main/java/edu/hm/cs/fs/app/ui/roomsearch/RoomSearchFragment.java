package edu.hm.cs.fs.app.ui.roomsearch;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.fk07.R;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import edu.hm.cs.fs.app.datastore.helper.Callback;
import edu.hm.cs.fs.app.datastore.helper.RoomHelper;
import edu.hm.cs.fs.app.datastore.model.Room;
import edu.hm.cs.fs.app.datastore.model.constants.Day;
import edu.hm.cs.fs.app.datastore.model.constants.Time;
import edu.hm.cs.fs.app.ui.MainActivity;

/**
 * Created by Fabio on 27.04.2015.
 */
public class RoomSearchFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    @InjectView(R.id.toolbarExtension)
    View mToolbarExtension;
    @InjectView(R.id.daySpinner)
    Spinner mDays;
    @InjectView(R.id.timeSpinner)
    Spinner mTimes;
    @InjectView(R.id.listView)
    ListView mList;
    private RoomSearchAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_room_search, container, false);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            final MainActivity activity = (MainActivity) getActivity();

            activity.getSupportActionBar().setElevation(0f);
            mToolbarExtension.setElevation(10f);

            final Toolbar toolbar = activity.getToolbar();
            mToolbarExtension.setBackground(toolbar.getBackground());

        } else {
            mToolbarExtension.setBackgroundColor(Color.TRANSPARENT);
        }

        final ArrayAdapter<Day> dayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, Day.values());
        mDays.setAdapter(dayAdapter);
        mDays.setOnItemSelectedListener(this);
        mDays.setSelection(dayAdapter.getPosition(Day.now()));

        final ArrayAdapter<Time> timeAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, Time.rawValues());
        mTimes.setAdapter(timeAdapter);
        mTimes.setOnItemSelectedListener(this);
        mTimes.setSelection(0);
        mTimes.setSelection(timeAdapter.getPosition(Time.now()));

        mAdapter = new RoomSearchAdapter(getActivity());
        mList.setAdapter(mAdapter);

        refresh();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        refresh();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    private void refresh() {
        final Day day = (Day) mDays.getSelectedItem();
        final Time time = (Time) mTimes.getSelectedItem();

        RoomHelper.listAllFreeRooms(getActivity(), day, time, new Callback<List<Room>>() {
            @Override
            public void onResult(List<Room> result) {
                mAdapter.clear();
                for (Room room : result) {
                    mAdapter.add(room);
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);

        ((MainActivity) getActivity()).getSupportActionBar().setElevation(10f);
    }
}
