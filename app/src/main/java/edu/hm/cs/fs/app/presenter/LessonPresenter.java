package edu.hm.cs.fs.app.presenter;

import android.support.annotation.NonNull;

import java.util.Calendar;

import edu.hm.cs.fs.app.database.ICallback;
import edu.hm.cs.fs.app.database.error.IError;
import edu.hm.cs.fs.app.database.model.TimetableModel;
import edu.hm.cs.fs.app.view.ILessonView;
import edu.hm.cs.fs.common.model.Module;
import edu.hm.cs.fs.common.model.Person;

/**
 * Created by FHellman on 17.08.2015.
 */
public class LessonPresenter extends BasePresenter<ILessonView, TimetableModel> {
    /**
     * @param view
     */
    public LessonPresenter(ILessonView view) {
        super(view, TimetableModel.getInstance());
    }

    /**
     * Needed for testing!
     *
     * @param view
     * @param model
     */
    public LessonPresenter(ILessonView view, TimetableModel model) {
        super(view, model);
    }

    public void loadLesson(@NonNull final String moduleId, @NonNull final String teacherName) {
        getView().showLoading();
        getModel().getModule(moduleId, teacherName, new ICallback<Module>() {
            @Override
            public void onSuccess(Module data) {
                getView().showModuleName(data.getName());
                getView().showModuleEffort(data.getExpenditure());
                getView().showModulePrerequisites(data.getRequirements());
                getView().showModuleAims(data.getGoals());
                getView().showModuleContent(data.getContent());
                getView().showModuleLiterature(data.getLiterature());

                if(!data.getTeachers().isEmpty()) {
                    final Person person = data.getTeachers().get(0);

                    getView().showTeacherName(person.getTitle() + " " + person.getLastName() + " "
                            + person.getFirstName());
                    getView().showTeacherWebsite(person.getWebsite());
                    getView().showTeacherPhone(person.getPhone());
                    getView().showTeacherFocus(person.getFocus());

                    final Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.DAY_OF_WEEK,
                            person.getOfficeHourWeekday().getCalendarId());
                    getView().showTeacherOfficeHourWeekday(String.format("%1$tA", calendar));
                    getView().showTeacherOfficeHourTime(person.getOfficeHourTime());
                    getView().showTeacherOfficeHourRoom(person.getOfficeHourRoom());
                    getView().showTeacherOfficeHourComment(person.getOfficeHourComment());
                }
                getView().hideLoading();
            }

            @Override
            public void onError(IError error) {
                getView().showError(error);
                getView().hideLoading();
            }
        });
    }
}
