package edu.hm.cs.fs.app.ui.job;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fk07.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import edu.hm.cs.fs.app.datastore.model.Job;
import edu.hm.cs.fs.app.util.DataUtils;
import edu.hm.cs.fs.app.util.multipane.OnMultiPaneDetailSegment;

/**
 * Created by Fabio on 16.04.2015.
 */
public class JobDetailFragment extends Fragment implements OnMultiPaneDetailSegment<Job> {
    @InjectView(R.id.textSubject)
    TextView subject;
    @InjectView(R.id.textGroups) TextView groups;
    @InjectView(R.id.textDescription) TextView description;
    @InjectView(R.id.textUrl) TextView url;
    @InjectView(R.id.textAuthor) TextView author;
    private Job mJob;

    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_blackboard_detail, container, false);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onViewCreated(final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViewContent();
    }

    @Override
    public void onListItemClicked(final Job item) {
        mJob = item;
        initViewContent();
    }

    private void initViewContent() {
        if (subject == null || mJob == null) {
            return;
        }

        subject.setText(mJob.getTitle());
        if (mJob.getProgram() == null) {
            groups.setVisibility(View.GONE);
        } else {
            groups.setText(mJob.getProgram().toString());
        }
        description.setText(Html.fromHtml(DataUtils.toHtml(mJob.getDescription())));
        if (TextUtils.isEmpty(mJob.getUrl())) {
            url.setVisibility(View.GONE);
        } else {
            url.setText(mJob.getUrl());
        }
        author.setText(mJob.getProvider());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
