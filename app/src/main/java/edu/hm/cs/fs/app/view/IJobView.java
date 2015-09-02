package edu.hm.cs.fs.app.view;

import android.support.annotation.NonNull;

import java.util.List;

import edu.hm.cs.fs.common.model.Job;

/**
 * @author Fabio
 */
public interface IJobView extends IView {

    /**
     * Displays the job entries on the view.
     *
     * @param content to display.
     */
    void showContent(@NonNull final List<Job> content);
}
