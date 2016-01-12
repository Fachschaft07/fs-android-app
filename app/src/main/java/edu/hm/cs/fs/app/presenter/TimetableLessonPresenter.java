package edu.hm.cs.fs.app.presenter;

import android.support.annotation.NonNull;

import java.util.Locale;

import javax.inject.Inject;

import edu.hm.cs.fs.app.ui.PerActivity;
import edu.hm.cs.fs.app.ui.timetable.ITimetableLessonView;
import edu.hm.cs.fs.app.util.MarkdownUtil;
import edu.hm.cs.fs.common.model.Module;

@PerActivity
public class TimetableLessonPresenter extends BasePresenter<ITimetableLessonView> {
    @Inject
    public TimetableLessonPresenter() {
    }

    public void loadModule(@NonNull final String moduleId) {
        getView().showLoading();
        getModel().moduleById(moduleId).subscribe(new BasicSubscriber<Module>(getView()) {
            @Override
            public void onNext(Module data) {
                getView().showModuleTitle(data.getName());
                getView().showModuleCredits(data.getCredits());
                StringBuilder languages = new StringBuilder();
                for (int index = 0; index < data.getLanguages().size(); index++) {
                    if (index > 0) {
                        languages.append(", ");
                    }
                    languages.append(getLocaleByTag(data.getLanguages().get(index))
                            .getDisplayLanguage(Locale.getDefault()));
                }
                getView().showModuleLanguage(languages.toString());
                getView().showModuleEffort(MarkdownUtil.toHtml(data.getExpenditure()));
                getView().showModuleAim(MarkdownUtil.toHtml(data.getGoals()));
                getView().showModulePrerequirements(MarkdownUtil.toHtml(data.getRequirements()));
                getView().showModuleContent(MarkdownUtil.toHtml(data.getContent()));
                getView().showModuleMediaForm(MarkdownUtil.toHtml(data.getMedia()));
                getView().showModuleLiterature(MarkdownUtil.toHtml(data.getLiterature()));
                getView().hideLoading();
            }
        });
    }

    private Locale getLocaleByTag(@NonNull final String tag) {
        for (Locale locale : Locale.getAvailableLocales()) {
            if (locale.getLanguage().equals(tag)) {
                return locale;
            }
        }
        return Locale.ENGLISH;
    }
}
