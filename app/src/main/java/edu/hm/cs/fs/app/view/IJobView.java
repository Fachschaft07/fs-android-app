package edu.hm.cs.fs.app.view;

import android.support.annotation.NonNull;

import java.util.List;

import edu.hm.cs.fs.app.presenter.JobPresenter;
import edu.hm.cs.fs.common.model.Job;

/**
 * @author Fabio
 */
public interface IJobView extends IView<JobPresenter> {
    void showContent(@NonNull final List<Job> content);
}
