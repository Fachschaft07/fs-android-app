package edu.hm.cs.fs.app.ui.roomsearch;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

/**
 * Created by Fabio on 27.04.2015.
 */
public class RoomSearchFragment extends Fragment implements AdapterView.OnItemClickListener {
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
        ButterKnife.inject(view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final ArrayAdapter<Day> dayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, Day.values());
        mDays.setAdapter(dayAdapter);
        mDays.setOnItemClickListener(this);

        final ArrayAdapter<Time> timeAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, Time.rawValues());
        mTimes.setAdapter(timeAdapter);
        mTimes.setOnItemClickListener(this);

        mAdapter = new RoomSearchAdapter(getActivity());
        mList.setAdapter(mAdapter);

        refresh(Day.now(), Time.now());
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Day day = (Day) mDays.getSelectedItem();
        Time time = (Time) mTimes.getSelectedItem();

        refresh(day, time);
    }

    private void refresh(Day day, Time time) {
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
}
