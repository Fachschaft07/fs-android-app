package edu.hm.cs.fs.app.ui.timetable.wizard;

import android.content.Context;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import edu.hm.cs.fs.app.datastore.helper.Callback;
import edu.hm.cs.fs.app.datastore.helper.LessonHelper;
import edu.hm.cs.fs.app.datastore.model.Group;
import edu.hm.cs.fs.app.datastore.model.Lesson;
import edu.hm.cs.fs.app.datastore.model.Module;
import edu.hm.cs.fs.app.datastore.model.constants.Faculty;

/**
 * Created by Fabio on 29.03.2015.
 */
public class WizardContainer {
    private final Context mContext;
    private List<Group> mAllGroups = new ArrayList<>();
    private List<Group> mGroups = new ArrayList<>();
    private List<Module> mModules = new ArrayList<>();
    private List<Lesson> mLessons = new ArrayList<>();

    public WizardContainer(Context context) {
        mContext = context;

        LessonHelper.getGroups(context, Faculty._07, new Callback<List<Group>>() {
            @Override
            public void onResult(final List<Group> result) {
                Collections.sort(result, new Comparator<Group>() {
                    @Override
                    public int compare(final Group lhs, final Group rhs) {
                        return lhs.toString().compareTo(rhs.toString());
                    }
                });
                mAllGroups.addAll(result);
            }
        });
    }

    public List<Group> getGroups() {
        return null;
    }

    public void setGroups(List<Group> groups) {
        mGroups.addAll(groups);
    }

    public List<Module> getModulesByGroups() {
        return null;
    }

    public void setModules(List<Module> modules) {
        mModules.addAll(modules);
    }

    public List<Lesson> getLessonsByModules() {
        return null;
    }

    public void setLessons(List<Lesson> lessons) {
        mLessons.addAll(lessons);
    }

    public List<Lesson> getResult() {
        return mLessons;
    }
}
