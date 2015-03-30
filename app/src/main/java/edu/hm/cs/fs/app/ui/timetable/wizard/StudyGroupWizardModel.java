package edu.hm.cs.fs.app.ui.timetable.wizard;

import android.content.Context;

import com.fk07.R;
import com.tech.freak.wizardpager.model.AbstractWizardModel;
import com.tech.freak.wizardpager.model.BranchPage;
import com.tech.freak.wizardpager.model.Page;
import com.tech.freak.wizardpager.model.PageList;

import edu.hm.cs.fs.app.datastore.model.constants.Faculty;

/**
 * Created by Fabio on 30.03.2015.
 */
public class StudyGroupWizardModel extends AbstractWizardModel {
    private static final int[] SEMESTER_TEXT_ID = {
            R.string.semester_1,
            R.string.semester_2,
            R.string.semester_3,
            R.string.semester_4,
            R.string.semester_5,
            R.string.semester_6,
            R.string.semester_7
    };
    private static final int[] GROUP_TEXT_ID = {
            R.string.group_a,
            R.string.group_b,
            R.string.group_c
    };

    public StudyGroupWizardModel(Context context) {
        super(context);
    }

    @Override
    protected PageList onNewRootPageList() {
        return new PageList(getFaculty());
    }

    private Page getFaculty() {
        return new BranchPage(this, getString(R.string.wizard_faculty))
                .addBranch(
                        getString(R.string.faculty_07),
                        getStudies(Faculty._07)
                )
                        // Add new Faculties here
                .setRequired(true);
    }

    private Page getStudies(Faculty faculty) {
        BranchPage page = new BranchPage(this, getString(R.string.wizard_study));
        page.setRequired(true);
        switch (faculty) {
            // Add new Faculties here AND
            // add the Study Tag to edu.hm.cs.fs.app.datastore.model.constants.Study
            case _07:
                page.addBranch(
                        getString(R.string.study_go),
                        getSemester(1, 1, 1, 1, 1, 1, 1)
                ).addBranch(
                        getString(R.string.study_ib),
                        getSemester(3, 3, 3, 3, 3, 3, 1)
                ).addBranch(
                        getString(R.string.study_ic),
                        getSemester(1, 1, 1, 1, 1, 1, 1)
                ).addBranch(
                        getString(R.string.study_if),
                        getSemester(3, 3, 2, 2, 1, 1, 1)
                ).addBranch(
                        getString(R.string.study_ig),
                        getSemester(1, 1, 1)
                ).addBranch(
                        getString(R.string.study_in),
                        getSemester(1, 1, 1)
                ).addBranch(
                        getString(R.string.study_is),
                        getSemester(1, 1, 1)
                );
        }
        return page;
    }

    private Page getSemester(int... semesterGroupCounts) {
        BranchPage page = new BranchPage(this, getString(R.string.wizard_semesters));
        page.setRequired(true);
        for (int index = 0; index < semesterGroupCounts.length; index++) {
            page.addBranch(
                    getString(SEMESTER_TEXT_ID[index]),
                    getGroups(semesterGroupCounts[index])
            );
        }
        return page;
    }

    private Page getGroups(int count) {
        // FIXME: Bei 07 -> IF -> 3 oder 4 -> A, B, C (Hier darf nur A und B erscheinen)
        if(count > 1) {
            BranchPage page = new BranchPage(this, getString(R.string.wizard_groups));
            page.setRequired(true);
            for (int index = 0; index < count; index++) {
                page.addBranch(
                        getString(GROUP_TEXT_ID[index]),
                        new LessonPage(this, getString(R.string.wizard_lessons)).setRequired(true)
                );
            }
            return page;
        } else {
            return new LessonPage(this, getString(R.string.wizard_lessons)).setRequired(true);
        }
    }

    private String getString(int stringId) {
        return mContext.getString(stringId);
    }
}
