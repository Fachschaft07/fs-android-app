package edu.hm.cs.fs.app.ui.timetable.wizard.model;

import android.support.v4.app.Fragment;

import com.tech.freak.wizardpager.model.ModelCallbacks;
import com.tech.freak.wizardpager.model.SingleFixedChoicePage;

import edu.hm.cs.fs.app.ui.timetable.wizard.ui.StudyFragment;

/**
 * Created by Fabio on 02.05.2015.
 */
public class StudyPage extends SingleFixedChoicePage {
    public StudyPage(final ModelCallbacks callbacks, final String title) {
        super(callbacks, title);
    }

    @Override
    public Fragment createFragment() {
        return StudyFragment.create(getKey());
    }
}
