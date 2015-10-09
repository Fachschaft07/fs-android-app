package edu.hm.cs.fs.app.view;

import android.support.annotation.NonNull;
import android.text.Spanned;

/**
 * @author Fabio
 */
public interface ITimetableLessonView extends IDetailsView {
    void showModuleTitle(@NonNull final String title);

    void showModuleCredits(final int credits);

    void showModuleLanguage(@NonNull final String language);

    void showModuleEffort(@NonNull final Spanned effort);

    void showModulePrerequirements(@NonNull final Spanned prerequirements);

    void showModuleAim(@NonNull final Spanned aim);

    void showModuleContent(@NonNull final Spanned content);

    void showModuleMediaForm(@NonNull final Spanned media);

    void showModuleLiterature(@NonNull final Spanned literature);
}
