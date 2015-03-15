package edu.hm.cs.fs.app.ui.presence;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.fk07.R;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import edu.hm.cs.fs.app.datastore.helper.Callback;
import edu.hm.cs.fs.app.datastore.helper.PresenceHelper;
import edu.hm.cs.fs.app.datastore.model.Presence;

/**
 * Created by Fabio on 04.03.2015.
 */
public class PresenceFragment extends Fragment {
    @InjectView(R.id.presenceListView)
    ListView mListView;

    private PresenceAdapter mAdapter;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_presence, container, false);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onViewCreated(final View view, final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAdapter = new PresenceAdapter(getActivity());
        mListView.setAdapter(mAdapter);

        refresh();
    }

    @Override
    public void onCreateOptionsMenu(final Menu menu, final MenuInflater inflater) {
        inflater.inflate(R.menu.presence, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_refresh: {
                refresh();
                return true;
            }
            case R.id.menu_call: {
                final Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.putExtra(Intent.EXTRA_PHONE_NUMBER, "08912653777");
                startActivity(intent);
                return true;
            }
            case R.id.menu_mail: {
                final Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("plain/text");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"info@fs.cs.hm.edu"});
                startActivity(intent);
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void refresh() {
        PresenceHelper.listAll(getActivity(), new Callback<List<Presence>>() {
            @Override
            public void onResult(final List<Presence> result) {
                mAdapter.clear();
                for (Presence presence : result) {
                    mAdapter.add(presence);
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
