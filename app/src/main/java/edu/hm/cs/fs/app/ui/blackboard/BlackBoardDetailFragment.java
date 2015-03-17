package edu.hm.cs.fs.app.ui.blackboard;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
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
    @InjectView(R.id.textGroups) TextView groups;
    @InjectView(R.id.textDescription) TextView description;
    @InjectView(R.id.textUrl) TextView url;
    @InjectView(R.id.textAuthor) TextView author;
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

    @Override
    public String getTitle() {
        return mNews.getSubject();
    }

    private void initViewContent() {
        if(groups == null || mNews == null) {
            return;
        }

        if(mNews.getGroups().isEmpty()) {
            groups.setVisibility(View.GONE);
        } else {
            groups.setText(mNews.getGroups().toString());
        }
        description.setText(mNews.getText());
        if(TextUtils.isEmpty(mNews.getUrl())) {
            url.setVisibility(View.GONE);
        } else {
            url.setText(mNews.getUrl());
        }
        author.setText(mNews.getAuthor().getLastName() + " " + mNews.getAuthor().getFirstName());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
