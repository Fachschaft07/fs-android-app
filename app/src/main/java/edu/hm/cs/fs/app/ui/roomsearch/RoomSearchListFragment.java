package edu.hm.cs.fs.app.ui.roomsearch;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.fk07.R;

import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import edu.hm.cs.fs.app.App;
import edu.hm.cs.fs.app.presenter.RoomSearchListPresenter;
import edu.hm.cs.fs.app.ui.BaseFragment;
import edu.hm.cs.fs.common.constant.Day;
import edu.hm.cs.fs.common.constant.Time;
import edu.hm.cs.fs.common.model.simple.SimpleRoom;

/**
 * @author Fabio
 */
public class RoomSearchListFragment extends BaseFragment<RoomSearchListComponent, RoomSearchListPresenter>
        implements RoomSearchListView {

    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Bind(R.id.swipeContainer)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Bind(R.id.listView)
    RecyclerView mListView;

    @Bind(R.id.spinnerDay)
    Spinner mSpinnerDay;

    @Bind(R.id.spinnerTime)
    Spinner mSpinnerTime;

    private RoomSearchListAdapter mAdapter;

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

        mAdapter = new RoomSearchListAdapter(getActivity());
        mListView.setAdapter(mAdapter);
        mListView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mSpinnerDay.setAdapter(new ArrayAdapter<Day>(getActivity(), android.R.layout.simple_spinner_dropdown_item, Day.values()) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                return setText(super.getView(position, convertView, parent), position, Color.WHITE);
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                return setText(super.getDropDownView(position, convertView, parent), position, Color.BLACK);
            }

            private View setText(View view, int position, int color) {
                final Day day = getItem(position);

                final Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.DAY_OF_WEEK, day.getCalendarId());

                final String value = String.format("%1$tA", calendar);
                final TextView textView = (TextView) view.findViewById(android.R.id.text1);
                textView.setText(value);
                textView.setTextColor(color);
                return view;
            }
        });
        mSpinnerDay.setSelection(0);

        mSpinnerTime.setAdapter(new ArrayAdapter<Time>(getActivity(), android.R.layout.simple_spinner_dropdown_item, Time.values()) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                return setText(super.getView(position, convertView, parent), position, Color.WHITE);
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                return setText(super.getDropDownView(position, convertView, parent), position, Color.BLACK);
            }

            private View setText(View view, int position, int color) {
                final Time time = getItem(position);
                final String value = String.format("%1$tH:%1$tM", time.getStart());
                final TextView textView = (TextView) view.findViewById(android.R.id.text1);
                textView.setText(value);
                textView.setTextColor(color);
                return view;
            }
        });
        mSpinnerTime.setSelection(0);

        mSwipeRefreshLayout.setEnabled(false);
        initSwipeRefreshLayout(mSwipeRefreshLayout);

        getPresenter().loadCurrentFreeRooms();
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_room_search;
    }

    @Override
    public boolean hasCustomToolbar() {
        return true;
    }

    @Override
    protected int getTitle() {
        return R.string.roomsearch;
    }

    @OnClick(R.id.search)
    @Override
    public void onRefresh() {
        getPresenter().loadFreeRooms(getSelectedDay(), getSelectedTime());
    }

    @SuppressWarnings("unchecked")
    @Override
    public void showCurrentDay(@NonNull final Day day) {
        final ArrayAdapter<Day> adapter = (ArrayAdapter<Day>) mSpinnerDay.getAdapter();
        mSpinnerDay.setSelection(adapter.getPosition(day));
    }

    @SuppressWarnings("unchecked")
    @Override
    public void showCurrentTime(@NonNull final Time time) {
        final ArrayAdapter<Time> adapter = (ArrayAdapter<Time>) mSpinnerTime.getAdapter();
        mSpinnerTime.setSelection(adapter.getPosition(time));
    }

    @NonNull
    private Day getSelectedDay() {
        return (Day) mSpinnerDay.getSelectedItem();
    }

    @NonNull
    private Time getSelectedTime() {
        return (Time) mSpinnerTime.getSelectedItem();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    protected RoomSearchListComponent onCreateNonConfigurationComponent() {
        return DaggerRoomSearchListComponent.builder()
                .appComponent(App.getAppComponent(getMainActivity()))
                .build();
    }

    @Override
    public void clear() {
        mAdapter.clear();
    }

    @Override
    public void add(@NonNull SimpleRoom item) {
        mAdapter.add(item);
    }
}
