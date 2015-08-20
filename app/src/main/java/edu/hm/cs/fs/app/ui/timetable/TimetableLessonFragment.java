package edu.hm.cs.fs.app.ui.timetable;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.fk07.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import edu.hm.cs.fs.app.presenter.LessonPresenter;
import edu.hm.cs.fs.app.util.BaseFragment;
import edu.hm.cs.fs.app.view.ILessonView;

/**
 * Created by FHellman on 17.08.2015.
 */
public class TimetableLessonFragment extends BaseFragment<LessonPresenter> implements ILessonView,
        SwipeRefreshLayout.OnRefreshListener {
    public static final String ARG_MODULE = "module";
    public static final String ARG_TEACHER = "teacher";

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.swipeContainer)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @Bind(R.id.textModuleName)
    TextView mModuleName;
    @Bind(R.id.textModuleEffort)
    TextView mModuleEffort;
    @Bind(R.id.textModulePrerequisites)
    TextView mModulePrerequisites;
    @Bind(R.id.textModuleAims)
    TextView mModuleAims;
    @Bind(R.id.textModuleContent)
    TextView mModuleContent;
    @Bind(R.id.textModuleLiterature)
    TextView mModuleLiterature;
    @Bind(R.id.textTeacherName)
    TextView mTeacherName;
    @Bind(R.id.textTeacherWebsite)
    TextView mTeacherWebsite;
    @Bind(R.id.textTeacherPhone)
    TextView mTeacherPhone;
    @Bind(R.id.textTeacherFocus)
    TextView mTeacherFocus;
    @Bind(R.id.textTeacherOfficeHourWeekday)
    TextView mTeacherOfficeHourWeekday;
    @Bind(R.id.textTeacherOfficeHourTime)
    TextView mTeacherOfficeHourTime;
    @Bind(R.id.textTeacherOfficeHourRoom)
    TextView mTeacherOfficeHourRoom;
    @Bind(R.id.textTeacherOfficeHourComment)
    TextView mTeacherOfficeHourComment;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        mToolbar.setNavigationIcon(getMainActivity().getToolbar().getNavigationIcon());
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMainActivity().openDrawer();
            }
        });

        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setEnabled(false);
        initSwipeRefreshLayout(mSwipeRefreshLayout);
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_timetable_lesson;
    }

    @Override
    public boolean hasCustomToolbar() {
        return true;
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void showModuleName(@NonNull String name) {
        mModuleName.setText(name);
    }

    @Override
    public void showModuleEffort(@NonNull String effort) {
        mModuleEffort.setText(effort);
    }

    @Override
    public void showModulePrerequisites(@NonNull String prerequisites) {
        mModulePrerequisites.setText(prerequisites);
    }

    @Override
    public void showModuleAims(@NonNull String aims) {
        mModuleAims.setText(aims);
    }

    @Override
    public void showModuleContent(@NonNull String content) {
        mModuleContent.setText(content);
    }

    @Override
    public void showModuleLiterature(@NonNull String literature) {
        mModuleLiterature.setText(literature);
    }

    @Override
    public void showTeacherName(@NonNull String name) {
        mTeacherName.setText(name);
    }

    @Override
    public void showTeacherWebsite(@NonNull String website) {
        mTeacherWebsite.setText(website);
    }

    @Override
    public void showTeacherPhone(@NonNull String phone) {
        mTeacherPhone.setText(phone);
    }

    @Override
    public void showTeacherFocus(@NonNull String focus) {
        mTeacherFocus.setText(focus);
    }

    @Override
    public void showTeacherOfficeHourWeekday(@NonNull String day) {
        mTeacherOfficeHourWeekday.setText(day);
    }

    @Override
    public void showTeacherOfficeHourTime(@NonNull String time) {
        mTeacherOfficeHourTime.setText(time);
    }

    @Override
    public void showTeacherOfficeHourRoom(@NonNull String room) {
        mTeacherOfficeHourRoom.setText(room);
    }

    @Override
    public void showTeacherOfficeHourComment(@NonNull String comment) {
        mTeacherOfficeHourComment.setText(comment);
    }

    @Override
    public void close() {
        getMainActivity().getNavigator().goOneBack();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
