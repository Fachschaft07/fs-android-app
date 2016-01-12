package edu.hm.cs.fs.app.ui.timetable;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Spanned;
import android.view.View;
import android.widget.TextView;

import com.fk07.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import edu.hm.cs.fs.app.App;
import edu.hm.cs.fs.app.presenter.TimetableLessonPresenter;
import edu.hm.cs.fs.app.ui.BaseFragment;
import edu.hm.cs.fs.app.ui.MainActivity;
import edu.hm.cs.fs.app.ui.PerActivity;

@PerActivity
public class TimetableLessonFragment extends BaseFragment<TimetableLessonComponent, TimetableLessonPresenter> implements ITimetableLessonView {
    public static final String ARG_MODULE_ID = "module_id";
    public static final String ARG_TEACHER_ID = "teacher_id";

    @Bind(R.id.swipeContainer)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @Bind(R.id.textTitle)
    TextView mTitle;
    @Bind(R.id.textCredits)
    TextView mCredits;
    @Bind(R.id.textLanguage)
    TextView mLanguage;
    @Bind(R.id.labelEffort)
    TextView mLabelEffort;
    @Bind(R.id.textEffort)
    TextView mEffort;
    @Bind(R.id.labelPrerequirements)
    TextView mLabelPrerequirements;
    @Bind(R.id.textPrerequirements)
    TextView mPrerequirements;
    @Bind(R.id.labelAims)
    TextView mLabelAims;
    @Bind(R.id.textAims)
    TextView mAims;
    @Bind(R.id.labelContent)
    TextView mLabelContent;
    @Bind(R.id.textContent)
    TextView mContent;
    @Bind(R.id.labelMediaForm)
    TextView mLabelMediaForm;
    @Bind(R.id.textMediaForm)
    TextView mMediaForm;
    @Bind(R.id.labelLiterature)
    TextView mLabelLiterature;
    @Bind(R.id.textLiterature)
    TextView mLiterature;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        mSwipeRefreshLayout.setEnabled(false);
        initSwipeRefreshLayout(mSwipeRefreshLayout);

        getToolbar().setNavigationOnClickListener(v -> close());

        final Bundle arguments = getArguments();
        final String moduleId = arguments.getString(ARG_MODULE_ID);
        final String teacherId = arguments.getString(ARG_TEACHER_ID);

        if (moduleId != null) {
            getPresenter().loadModule(moduleId);
        }
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
    public void close() {
        MainActivity.getNavigator().goOneBack();
    }

    @Override
    public void showModuleTitle(@NonNull String title) {
        mTitle.setText(title);
    }

    @Override
    public void showModuleCredits(int credits) {
        mCredits.setText(getString(R.string.ects, credits));
    }

    @Override
    public void showModuleLanguage(@NonNull String language) {
        mLanguage.setText(language);
    }

    @Override
    public void showModuleEffort(@NonNull Spanned effort) {
        mLabelEffort.setVisibility(View.VISIBLE);
        mEffort.setText(effort);
    }

    @Override
    public void showModulePrerequirements(@NonNull Spanned prerequirements) {
        mLabelPrerequirements.setVisibility(View.VISIBLE);
        mPrerequirements.setText(prerequirements);
    }

    @Override
    public void showModuleAim(@NonNull Spanned aim) {
        mLabelAims.setVisibility(View.VISIBLE);
        mAims.setText(aim);
    }

    @Override
    public void showModuleContent(@NonNull Spanned content) {
        mLabelContent.setVisibility(View.VISIBLE);
        mContent.setText(content);
    }

    @Override
    public void showModuleMediaForm(@NonNull Spanned media) {
        mLabelMediaForm.setVisibility(View.VISIBLE);
        mMediaForm.setText(media);
    }

    @Override
    public void showModuleLiterature(@NonNull Spanned literature) {
        mLabelLiterature.setVisibility(View.VISIBLE);
        mLiterature.setText(literature);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    protected TimetableLessonComponent onCreateNonConfigurationComponent() {
        return DaggerTimetableLessonComponent.builder()
                .appComponent(App.getAppComponent(getMainActivity()))
                .build();
    }
}
