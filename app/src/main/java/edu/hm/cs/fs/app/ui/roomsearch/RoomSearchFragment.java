package edu.hm.cs.fs.app.ui.roomsearch;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
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
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import edu.hm.cs.fs.app.presenter.RoomSearchPresenter;
import edu.hm.cs.fs.app.util.BaseFragment;
import edu.hm.cs.fs.app.view.IRoomSearchView;
import edu.hm.cs.fs.common.constant.Day;
import edu.hm.cs.fs.common.constant.Time;
import edu.hm.cs.fs.common.model.Room;

/**
 * Created by FHellman on 11.08.2015.
 */
public class RoomSearchFragment extends BaseFragment<RoomSearchPresenter> implements IRoomSearchView, SwipeRefreshLayout.OnRefreshListener {
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
    private RoomSearchAdapter mAdapter;

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

        mAdapter = new RoomSearchAdapter(getActivity());
        mListView.setAdapter(mAdapter);
        mListView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mSpinnerDay.setAdapter(new ArrayAdapter<Day>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item, Day.values()) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                return setText(super.getView(position, convertView, parent), position);
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                return setText(super.getDropDownView(position, convertView, parent), position);
            }

            private View setText(View view, int position) {
                final Day day = getItem(position);

                final Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.DAY_OF_WEEK, day.getCalendarId());

                final String value = String.format("%1$tA", calendar);
                final TextView textView = (TextView) view.findViewById(android.R.id.text1);
                textView.setText(value);
                textView.setTextColor(Color.WHITE);
                return view;
            }
        });
        mSpinnerDay.setSelection(0);

        mSpinnerTime.setAdapter(new ArrayAdapter<Time>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item, Time.values()) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                return setText(super.getView(position, convertView, parent), position);
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                return setText(super.getDropDownView(position, convertView, parent), position);
            }

            private View setText(View view, int position) {
                final Time time = getItem(position);
                final String value = String.format("%1$tH:%1$tM", time.getStart());
                final TextView textView = (TextView) view.findViewById(android.R.id.text1);
                textView.setText(value);
                textView.setTextColor(Color.WHITE);
                return view;
            }
        });
        mSpinnerTime.setSelection(0);

        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setEnabled(false);
        initSwipeRefreshLayout(mSwipeRefreshLayout);

        setPresenter(new RoomSearchPresenter(this));
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

    @Override
    public void showLoading() {
        mSwipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void hideLoading() {
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showContent(@NonNull List<Room> content) {
        mAdapter.setData(content);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void showCurrentDay(@NonNull final Day day) {
        mSpinnerDay.setSelection(((ArrayAdapter<Day>)mSpinnerDay.getAdapter())
                .getPosition(day));
    }

    @SuppressWarnings("unchecked")
    @Override
    public void showCurrentTime(@NonNull final Time time) {
        mSpinnerTime.setSelection(((ArrayAdapter<Time>)mSpinnerTime.getAdapter())
                .getPosition(time));
    }

    @Override
    public void showError(@NonNull String error) {
        if (mSwipeRefreshLayout != null) {
            Snackbar.make(mSwipeRefreshLayout, error, Snackbar.LENGTH_LONG)
                    .setAction(R.string.retry, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onRefresh();
                        }
                    }).show();
        }
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
}
