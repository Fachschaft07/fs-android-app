package edu.hm.cs.fs.app.ui.timetable.wizard;

import android.support.v4.app.Fragment;

import com.tech.freak.wizardpager.model.ModelCallbacks;
import com.tech.freak.wizardpager.model.Page;
import com.tech.freak.wizardpager.model.ReviewItem;

import java.util.ArrayList;

/**
 * Created by Fabio on 30.03.2015.
 */
public class LessonPage extends Page {
    public LessonPage(final ModelCallbacks callbacks, final String title) {
        super(callbacks, title);
    }

    @Override
    public Fragment createFragment() {
        return LessonFragment.create(getKey());
    }

    @Override
    public void getReviewItems(final ArrayList<ReviewItem> reviewItems) {
    }
}
