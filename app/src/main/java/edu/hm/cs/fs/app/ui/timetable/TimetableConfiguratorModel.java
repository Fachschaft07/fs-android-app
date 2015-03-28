package edu.hm.cs.fs.app.ui.timetable;

import android.content.Context;

import com.tech.freak.wizardpager.model.AbstractWizardModel;
import com.tech.freak.wizardpager.model.PageList;

/**
 * Created by Fabio on 28.03.2015.
 */
public class TimetableConfiguratorModel extends AbstractWizardModel {
    public TimetableConfiguratorModel(final Context context) {
        super(context);
    }

    @Override
    protected PageList onNewRootPageList() {
        return new PageList(

        );
    }
}
