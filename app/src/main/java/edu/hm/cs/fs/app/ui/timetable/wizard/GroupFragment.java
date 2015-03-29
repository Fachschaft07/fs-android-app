package edu.hm.cs.fs.app.ui.timetable.wizard;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.fk07.R;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import edu.hm.cs.fs.app.datastore.helper.Callback;
import edu.hm.cs.fs.app.datastore.helper.LessonHelper;
import edu.hm.cs.fs.app.datastore.model.Group;
import edu.hm.cs.fs.app.datastore.model.constants.Faculty;

/**
 * Created by Fabio on 29.03.2015.
 */
public class GroupFragment extends BaseWizardFragment<Group> {
    @Override
    public void onViewCreated(final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LessonHelper.getGroups(getActivity(), Faculty._07, new Callback<List<Group>>() {
            @Override
            public void onResult(final List<Group> result) {
                Collections.sort(result, new Comparator<Group>() {
                    @Override
                    public int compare(final Group lhs, final Group rhs) {
                        return lhs.toString().compareTo(rhs.toString());
                    }
                });
                addItems(result);
            }
        });
    }

    @Override
    public int getTitle() {
        return R.string.wizard_groups;
    }

    @Override
    public void onItemText(final Group item, final TextView textView) {
        textView.setText(item.toString());
    }
}
