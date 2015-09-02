package edu.hm.cs.fs.app.view;

import android.support.annotation.NonNull;

import java.util.List;

import edu.hm.cs.fs.common.model.PublicTransport;

/**
 * @author Fabio
 */
public interface IPublicTransportView extends IView {

    /**
     * Displays the public transport entries on the view.
     *
     * @param content to display.
     */
    void showContent(@NonNull final List<PublicTransport> content);
}
