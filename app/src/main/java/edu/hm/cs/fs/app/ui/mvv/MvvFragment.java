package edu.hm.cs.fs.app.ui.mvv;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.fk07.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import edu.hm.cs.fs.app.util.NetworkUtils;

public class MvvFragment extends Fragment {
	@InjectView(R.id.listViewLoth) ListView listLoth;
	@InjectView(R.id.listViewPasing) ListView listPasing;

	private MvvAdapter lothAdapter;
	private MvvAdapter pasiAdapter;

	@Override
	public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.activity_mvv, container, false);
		ButterKnife.inject(this, view);
		return view;
	}

	@Override
	public void onViewCreated(final View view, @Nullable final Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		listLoth.setAdapter(new MvvAdapter());
	}

	@Override
	public void onCreateOptionsMenu(final Menu menu, final MenuInflater inflater) {
		if (NetworkUtils.isConnected(getActivity())) {
			inflater.inflate(R.menu.actionmenu_mvv, menu);
		} else {
			inflater.inflate(R.menu.actionmenu, menu);
		}
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {
		switch (item.getItemId()) {
			case R.id.menu_refresh:
				onRefresh();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	private void onRefresh() {
		new MvvParser(this, lothAdapter).execute(Urls.MVV_LOTHSTR, Files.MVV_LOTHSTR);
		new MvvParser(this, pasiAdapter).execute(Urls.MVV_PASING, Files.MVV_PASING);
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		ButterKnife.reset(this);
	}
}
