package edu.hm.cs.fs.app.datastore.web;

import android.content.Context;

import javax.xml.xpath.XPathConstants;

import edu.hm.cs.fs.app.datastore.model.impl.RoomImpl;
import edu.hm.cs.fs.app.datastore.web.fetcher.AbstractXmlFetcher;

/**
 * Created by Fabio on 27.04.2015.
 */
public class RoomFetcher extends AbstractXmlFetcher<RoomFetcher, RoomImpl> {
    private static final String URL = "http://fi.cs.hm.edu/fi/rest/public/timetable/room.xml";
    private static final String ROOT_NODE = "/list/timetable";

    public RoomFetcher(Context context) {
        super(context, URL, ROOT_NODE);
    }

    @Override
    protected RoomImpl onCreateItem(String rootPath) throws Exception {
        String name;

        // Parse Elements...
        name = findByXPath(rootPath + "/value/text()",
                XPathConstants.STRING);

        RoomImpl room = new RoomImpl();
        room.setName(name);

        return room;
    }
}
