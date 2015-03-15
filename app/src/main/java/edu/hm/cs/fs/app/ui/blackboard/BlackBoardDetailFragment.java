package edu.hm.cs.fs.app.ui.blackboard;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fk07.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import edu.hm.cs.fs.app.datastore.model.News;
import edu.hm.cs.fs.app.util.multipane.OnMultiPaneDetailSegment;

/**
 * Created by Fabio on 08.03.2015.
 */
public class BlackBoardDetailFragment extends Fragment implements OnMultiPaneDetailSegment<News> {
    @InjectView(R.id.textSubject) TextView subject;
    @InjectView(R.id.textGroups) TextView groups;
    @InjectView(R.id.textDescription) TextView description;
    private News mNews;

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
    public void onListItemClicked(final News item) {
        mNews = item;
        initViewContent();
    }

    private void initViewContent() {
        if(subject == null || mNews == null) {
            return;
        }

        subject.setText(mNews.getSubject());
        groups.setText(mNews.getGroups().toString());
        description.setText(mNews.getText());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
