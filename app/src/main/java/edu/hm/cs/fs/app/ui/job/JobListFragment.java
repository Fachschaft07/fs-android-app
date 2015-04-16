package edu.hm.cs.fs.app.ui.job;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListView;

import java.util.List;

import butterknife.ButterKnife;
import edu.hm.cs.fs.app.datastore.helper.Callback;
import edu.hm.cs.fs.app.datastore.helper.JobHelper;
import edu.hm.cs.fs.app.datastore.model.Job;
import edu.hm.cs.fs.app.util.multipane.OnMultiPaneDetailSegment;
import edu.hm.cs.fs.app.util.multipane.OnMultiPaneListSegment;

/**
 * Created by Fabio on 16.04.2015.
 */
public class JobListFragment extends ListFragment implements OnMultiPaneListSegment<Job> {
    private JobAdapter mAdapter;
    private OnMultiPaneDetailSegment<Job> mDetailSegment;

    @Override
    public void onViewCreated(final View view, final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAdapter = new JobAdapter(getActivity());
        setListAdapter(mAdapter);

        JobHelper.listAll(getActivity(), new Callback<List<Job>>() {
            @Override
            public void onResult(final List<Job> result) {
                mAdapter.clear();
                for (Job job : result) {
                    mAdapter.add(job);
                }
                setListShown(true);
            }
        });
    }

    @Override
    public void onListItemClick(final ListView l, final View v, final int position, final long id) {
        super.onListItemClick(l, v, position, id);
        mDetailSegment.onListItemClicked(getItemAt(position));
    }

    @Override
    public void setDetailSegment(final OnMultiPaneDetailSegment<Job> detailSegment) {
        mDetailSegment = detailSegment;
    }

    @Override
    public int getSelectedPosition() {
        return getSelectedItemPosition();
    }

    @Override
    public Job getItemAt(final int position) {
        if(getListView() != null && position < getListView().getCount()) {
            return (Job) getListView().getItemAtPosition(position);
        }
        return null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
