package edu.hm.cs.fs.app.ui.timetable.wizard;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.fk07.R;

import java.util.List;

import edu.hm.cs.fs.app.datastore.helper.Callback;
import edu.hm.cs.fs.app.datastore.helper.LessonHelper;
import edu.hm.cs.fs.app.datastore.model.Group;
import edu.hm.cs.fs.app.datastore.model.Lesson;
import edu.hm.cs.fs.app.datastore.model.constants.Faculty;

/**
 * Created by Fabio on 29.03.2015.
 */
public class LessonFragment extends BaseWizardFragment<Lesson> {
    @Override
    public void onViewCreated(final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public int getTitle() {
        return R.string.wizard_lessons;
    }

    @Override
    public void onItemText(final Lesson item, final TextView textView) {
        textView.setText(item.getDay().toString() + ". " + item.getTime().toString() + " - " + item.getModule());
    }
}
