package edu.hm.cs.fs.app.ui.home;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.dexafree.materialList.card.Card;

import java.util.List;

import edu.hm.cs.fs.app.ui.IView;
import edu.hm.cs.fs.common.model.BlackboardEntry;
import edu.hm.cs.fs.common.model.Exam;
import edu.hm.cs.fs.common.model.Holiday;
import edu.hm.cs.fs.common.model.Lesson;
import edu.hm.cs.fs.common.model.Meal;
import edu.hm.cs.fs.common.model.News;

/**
 * @author Fabio
 */
public interface HomeView extends IView {

    void showCard(@NonNull final Card card);

    /**
     * Removes all cards from the home screen.
     */
    void clear();
}
