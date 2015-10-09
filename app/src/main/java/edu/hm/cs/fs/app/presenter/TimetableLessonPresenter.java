package edu.hm.cs.fs.app.presenter;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.Locale;

import edu.hm.cs.fs.app.database.ICallback;
import edu.hm.cs.fs.app.database.error.IError;
import edu.hm.cs.fs.app.database.model.ModelFactory;
import edu.hm.cs.fs.app.database.model.TimetableModel;
import edu.hm.cs.fs.app.util.MarkdownUtil;
import edu.hm.cs.fs.app.view.ITimetableLessonView;
import edu.hm.cs.fs.common.model.Module;

/**
 * @author Fabio
 */
public class TimetableLessonPresenter extends BasePresenter<ITimetableLessonView, TimetableModel> {
    /**
     *
     * @param context
     * @param view
     */
    public TimetableLessonPresenter(@NonNull final Context context,
                                    @NonNull final ITimetableLessonView view) {
        super(view, ModelFactory.getTimetable(context));
    }

    /**
     * Needed for testing!
     *
     * @param view
     * @param model
     */
    public TimetableLessonPresenter(@NonNull final ITimetableLessonView view,
                                    @NonNull final TimetableModel model) {
        super(view, model);
    }

    public void loadModule(@NonNull final String moduleId) {
        getView().showLoading();
        ModelFactory.getModule().getModuleById(moduleId, new ICallback<Module>() {
            @Override
            public void onSuccess(Module data) {
                getView().showModuleTitle(data.getName());
                getView().showModuleCredits(data.getCredits());
                StringBuilder languages = new StringBuilder();
                for(int index = 0; index < data.getLanguages().size(); index++) {
                    if(index > 0) {
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

            private Locale getLocaleByTag(@NonNull final String tag) {
                for (Locale locale : Locale.getAvailableLocales()) {
                    if(locale.getLanguage().equals(tag)) {
                        return locale;
                    }
                }
                return Locale.ENGLISH;
            }

            @Override
            public void onError(@NonNull IError error) {
                getView().showError(error);
                getView().hideLoading();
            }
        });
    }
}
