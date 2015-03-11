package edu.hm.cs.fs.app.ui.mensa;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.fk07.R;

import edu.hm.cs.fs.app.util.NetworkUtils;
import butterknife.ButterKnife;
import butterknife.InjectView;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

/**
 * Created by Fabio on 04.03.2015.
 */
public class MensaFragment extends ListFragment {
    @InjectView(R.id.stickyList)
    StickyListHeadersListView mListView;

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onViewCreated(final View view, final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MensaAdapter adapter = new MensaAdapter(getActivity());
        mListView.setAdapter(adapter);

        // TODO Call MensaHelper.listAll(...);
    }

	@Override
	public void onCreateOptionsMenu(final Menu menu, final MenuInflater inflater) {
		inflater.inflate(R.menu.mensa, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {
		switch (item.getItemId()) {
			case R.id.menu_legend:
				// TODO Show legend
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
}
