package edu.hm.cs.fs.app.ui.blackboard;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fk07.R;

import edu.hm.cs.fs.app.datastore.model.News;
import edu.hm.cs.fs.app.util.multipane.MultiPaneDetailFragment;

/**
 * Created by Fabio on 08.03.2015.
 */
public class BlackBoardDetailFragment extends MultiPaneDetailFragment<News> {
    private TextView subject;
    private TextView groups;
    private TextView description;

    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_blackboard_detail, container, false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        subject = (TextView) view.findViewById(R.id.textSubject);
        groups = (TextView) view.findViewById(R.id.textGroups);
        description = (TextView) view.findViewById(R.id.textDescription);

        notifyDataSetChanged();
    }

    @Override
    public void onDisplayData(final News item, final int position) {
        subject.setText(item.getSubject());
        groups.setText(item.getGroups().toString());
        description.setText(item.getText());
    }
}
