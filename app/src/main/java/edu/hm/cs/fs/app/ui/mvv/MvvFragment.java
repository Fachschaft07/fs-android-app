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

import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import edu.hm.cs.fs.app.datastore.helper.Callback;
import edu.hm.cs.fs.app.datastore.helper.PublicTransportHelper;
import edu.hm.cs.fs.app.datastore.model.PublicTransport;

public class MvvFragment extends Fragment {
	@InjectView(R.id.listViewLoth) ListView mListLoth;
	@InjectView(R.id.listViewPasing) ListView mListPasing;

	private MvvAdapter mAdapterLoth;
	private MvvAdapter mAdapterPasing;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

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
        PublicTransportHelper.listAll(getActivity(), PublicTransport.Location.LOTHSTR, new Callback<List<PublicTransport>>() {
            @Override
            public void onResult(final List<PublicTransport> result) {
                mAdapterLoth.clear();
                Date now = new Date();
                for (PublicTransport publicTransport : result) {
                    if(publicTransport.getDeparture().before(now)) {
                        mAdapterLoth.add(publicTransport);
                    }
                }
            }
        });

        PublicTransportHelper.listAll(getActivity(), PublicTransport.Location.PASING, new Callback<List<PublicTransport>>() {
            @Override
            public void onResult(final List<PublicTransport> result) {
                mAdapterPasing.clear();
                Date now = new Date();
                for (PublicTransport publicTransport : result) {
                    if(publicTransport.getDeparture().before(now)) {
                        mAdapterPasing.add(publicTransport);
                    }
                }
            }
        });
	}
}
