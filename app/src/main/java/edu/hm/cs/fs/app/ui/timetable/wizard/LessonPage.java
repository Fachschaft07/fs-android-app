package edu.hm.cs.fs.app.ui.timetable.wizard;

import android.support.v4.app.Fragment;

import com.tech.freak.wizardpager.model.ModelCallbacks;
import com.tech.freak.wizardpager.model.MultipleFixedChoicePage;

/**
 * Created by Fabio on 30.03.2015.
 */
public class LessonPage extends MultipleFixedChoicePage {
    public LessonPage(final ModelCallbacks callbacks, final String title) {
        super(callbacks, title);
    }

    @Override
    public Fragment createFragment() {
        return LessonFragment.create(getKey());
    }
}
