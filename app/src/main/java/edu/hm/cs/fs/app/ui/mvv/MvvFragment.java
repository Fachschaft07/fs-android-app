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

public class MvvFragment extends Fragment {
	@InjectView(R.id.listViewLoth) ListView mListLoth;
	@InjectView(R.id.listViewPasing) ListView mListPasing;

	private MvvAdapter mAdapterLoth;
	private MvvAdapter mAdapterPasing;

	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_mvv, container, false);
		ButterKnife.inject(this, view);
		return view;
	}

	@Override
	public void onViewCreated(final View view, @Nullable final Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		mAdapterLoth = new MvvAdapter(getActivity());
		mListLoth.setAdapter(mAdapterLoth);

		mAdapterPasing = new MvvAdapter(getActivity());
		mListPasing.setAdapter(mAdapterPasing);
	}

    @Override
    public void onResume() {
        super.onResume();
		refresh();
    }

	@Override
	public void onCreateOptionsMenu(final Menu menu, final MenuInflater inflater) {
		inflater.inflate(R.menu.mvv, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {
		switch (item.getItemId()) {
			case R.id.menu_refresh:
				refresh();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
	
	@Override
	public void onDestroyView() {
		super.onDestroyView();
		ButterKnife.reset(this);
	}
	
	private void refresh() {
        // TODO Call PublicTransportHelper.listAll(...);
	}
}
