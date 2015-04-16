package edu.hm.cs.fs.app.ui.blackboard;

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
import android.widget.ListView;

import com.fk07.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnItemClick;
import edu.hm.cs.fs.app.datastore.helper.Callback;
import edu.hm.cs.fs.app.datastore.helper.NewsHelper;
import edu.hm.cs.fs.app.datastore.model.Group;
import edu.hm.cs.fs.app.datastore.model.News;
import edu.hm.cs.fs.app.datastore.model.constants.Study;
import edu.hm.cs.fs.app.util.multipane.OnMultiPaneDetailSegment;
import edu.hm.cs.fs.app.util.multipane.OnMultiPaneListSegment;

/**
 * Created by Fabio on 18.02.2015.
 */
public class BlackBoardListFragment extends Fragment implements OnMultiPaneListSegment<News> {
    @InjectView(android.R.id.list)
    ListView mListView;

    private MenuItem mRefresh;

    private BlackBoardAdapter mAdapter;
    private OnMultiPaneDetailSegment<News> mDetailSegment;

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_blackboard_list, container, false);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onViewCreated(final View view, final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAdapter = new BlackBoardAdapter(getActivity());
        mListView.setAdapter(mAdapter);

        refresh();
        NewsHelper.listAll(getActivity(), new Callback<List<News>>() {
            @Override
            public void onResult(final List<News> result) {
                Collections.sort(result, new Comparator<News>() {
                    @Override
                    public int compare(final News lhs, final News rhs) {
                        return getGroups(lhs).compareTo(getGroups(rhs));
                    }

                    private String getGroups(News news) {
                        StringBuilder studyGroupsStr = new StringBuilder();
                        List<Study> studyGroups = new ArrayList<>();
                        for (Group group : news.getGroups()) {
                            if(!studyGroups.contains(group.getStudy())) {
                                if(!studyGroups.isEmpty()) {
                                    studyGroupsStr.append(", ");
                                }
                                studyGroups.add(group.getStudy());
                                studyGroupsStr.append(group.getStudy().toString());
                            }
                        }
                        return studyGroupsStr.toString();
                    }
                });

                mAdapter.clear();
                for (News news : result) {
                    mAdapter.add(news);
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

    @OnItemClick(android.R.id.list)
    public void onItemClicked(final AdapterView<?> adapter, final View parent, final int position, final long id) {
        mDetailSegment.onListItemClicked(getItemAt(position));
    }

    @Override
    public void setDetailSegment(final OnMultiPaneDetailSegment<News> detailSegment) {
        mDetailSegment = detailSegment;
    }

    @Override
    public int getSelectedPosition() {
        return mListView.getSelectedItemPosition();
    }

    @Override
    public News getItemAt(final int position) {
        if(mListView != null && position < mListView.getCount()) {
            return (News) mListView.getItemAtPosition(position);
        }
        return null;
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
