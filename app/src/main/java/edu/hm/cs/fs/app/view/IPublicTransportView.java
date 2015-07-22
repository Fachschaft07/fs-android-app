package edu.hm.cs.fs.app.view;

import java.util.List;

import edu.hm.cs.fs.common.model.PublicTransport;

/**
 * Created by Fabio on 29.06.2015.
 */
public interface IPublicTransportView extends IView {
    void showContent(List<PublicTransport> content);
}
