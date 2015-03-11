package edu.hm.cs.fs.app.ui.blackboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.fk07.R;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import edu.hm.cs.fs.app.datastore.helper.Callback;
import edu.hm.cs.fs.app.datastore.helper.NewsHelper;
import edu.hm.cs.fs.app.datastore.model.News;
import edu.hm.cs.fs.app.util.multipane.MultiPaneListFragment;

/**
 * Created by Fabio on 18.02.2015.
 */
public class BlackBoardListFragment extends MultiPaneListFragment<News> {
	@InjectView(android.R.id.list)
	ListView mListView;

	private BlackBoardAdapter mAdapter;

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

        NewsHelper.listAll(getActivity(), new Callback<List<News>>() {
            @Override
            public void onResult(final List<News> result) {
                mAdapter.clear();
                for (News news : result) {
                    mAdapter.add(news);
                }
            }
        });
	}

    @Override
    public int getAbsListViewId() {
        return android.R.id.list;
    }

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		ButterKnife.reset(this);
	}
}
