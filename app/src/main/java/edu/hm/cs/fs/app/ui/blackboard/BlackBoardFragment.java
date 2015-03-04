package edu.hm.cs.fs.app.ui.blackboard;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import edu.hm.cs.fs.app.datastore.helper.Callback;
import edu.hm.cs.fs.app.datastore.helper.NewsHelper;
import edu.hm.cs.fs.app.datastore.model.News;

/**
 * Created by Fabio on 18.02.2015.
 */
public class BlackBoardFragment extends ListFragment {
	@InjectView(android.R.id.list)
	ListView mListView;

	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
		View view = super.onCreateView(inflater, container, savedInstanceState);
		ButterKnife.inject(this, view);
		return view;
	}

	@Override
	public void onViewCreated(final View view, final Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

        final BlackBoardAdapter adapter = new BlackBoardAdapter(getActivity());
        mListView.setAdapter(adapter);

        NewsHelper.listAll(getActivity(), new Callback<List<News>>() {
            @Override
            public void onResult(final List<News> result) {
                for (News news : result) {
                    adapter.add(news);
                }
            }
        });
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		ButterKnife.reset(this);
	}
}
