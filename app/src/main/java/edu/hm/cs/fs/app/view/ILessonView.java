package edu.hm.cs.fs.app.view;

import android.support.annotation.NonNull;

import edu.hm.cs.fs.app.presenter.LessonPresenter;

/**
 * @author Fabio
 */
public interface ILessonView extends IDetailsView<LessonPresenter> {
    void showModuleName(@NonNull final String name);

    void showModuleEffort(@NonNull final String effort);

    void showModulePrerequisites(@NonNull final String prerequisites);

    void showModuleAims(@NonNull final String aims);

    void showModuleContent(@NonNull final String content);

    void showModuleLiterature(@NonNull final String literature);

    void showTeacherName(@NonNull final String name);

    void showTeacherWebsite(@NonNull final String website);

    void showTeacherPhone(@NonNull final String phone);

    void showTeacherFocus(@NonNull final String focus);

    void showTeacherOfficeHourWeekday(@NonNull final String day);

    void showTeacherOfficeHourTime(@NonNull final String time);

    void showTeacherOfficeHourRoom(@NonNull final String room);

    void showTeacherOfficeHourComment(@NonNull final String comment);
}
