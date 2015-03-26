package edu.hm.cs.fs.app.ui.mvv;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.fk07.R;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.ButterKnife;
import butterknife.InjectView;
import edu.hm.cs.fs.app.datastore.helper.Callback;
import edu.hm.cs.fs.app.datastore.helper.PublicTransportHelper;
import edu.hm.cs.fs.app.datastore.model.PublicTransport;

public class MvvFragment extends Fragment implements Runnable {
    private final Handler mHandler = new Handler();

	@InjectView(R.id.listViewLoth) ListView mListLoth;
	@InjectView(R.id.listViewPasing) ListView mListPasing;

    private MenuItem mRefresh;

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

        mHandler.post(this);
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

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		ButterKnife.reset(this);
        mHandler.removeCallbacks(this);
	}

    @Override
    public void run() {
        Log.d(getClass().getSimpleName(), "Update MVV data");
        onRefresh();
        PublicTransportHelper.listAll(getActivity(), PublicTransport.Location.LOTHSTR, new Callback<List<PublicTransport>>() {
            @Override
            public void onResult(final List<PublicTransport> result) {
                mAdapterLoth.clear();
                for (PublicTransport publicTransport : result) {
                    mAdapterLoth.add(publicTransport);
                }
                onRefreshFinished();
            }
        });

        PublicTransportHelper.listAll(getActivity(), PublicTransport.Location.PASING, new Callback<List<PublicTransport>>() {
            @Override
            public void onResult(final List<PublicTransport> result) {
                mAdapterPasing.clear();
                for (PublicTransport publicTransport : result) {
                    mAdapterPasing.add(publicTransport);
                }
                onRefreshFinished();
            }
        });
        mHandler.postDelayed(this, TimeUnit.MILLISECONDS.convert(1l, TimeUnit.MINUTES));
    }

    private void onRefresh() {
        if(mRefresh != null) {
            mRefresh.setVisible(true);
        }
    }

    private void onRefreshFinished() {
        if(mRefresh != null) {
            mRefresh.setVisible(false);
        }
    }
}
