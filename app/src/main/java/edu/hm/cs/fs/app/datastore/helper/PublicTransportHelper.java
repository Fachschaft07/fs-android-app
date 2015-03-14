package edu.hm.cs.fs.app.datastore.helper;

import android.content.Context;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import edu.hm.cs.fs.app.datastore.model.PublicTransport;
import edu.hm.cs.fs.app.datastore.model.impl.PublicTransportImpl;
import edu.hm.cs.fs.app.datastore.web.PublicTransportFetcher;
import io.realm.Realm;

/**
 * Created by Fabio on 14.03.2015.
 */
public class PublicTransportHelper extends BaseHelper implements PublicTransport {
    private final int line;
    private final String destination;
    private final Date departure;

    private PublicTransportHelper(final Context context, PublicTransportImpl publicTransport) {
        super(context);
        line = publicTransport.getLine();
        destination = publicTransport.getDestination();
        departure = publicTransport.getDeparture();
    }

    @Override
    public int getLine() {
        return line;
    }

    @Override
    public String getDestination() {
        return destination;
    }

    @Override
    public long getDepartureIn(final TimeUnit timeUnit) {
        long currentTime = System.currentTimeMillis();
        long departureTime = getDeparture().getTime();
        long timeIn = currentTime - departureTime;
        return timeUnit.convert(timeIn, TimeUnit.MILLISECONDS);
    }

    @Override
    public Date getDeparture() {
        return departure;
    }

    public static void listAll(Context context, PublicTransport.Location location, Callback<List<PublicTransport>> callback) {
        listAll(context, new PublicTransportFetcher(context, location), PublicTransportImpl.class, callback, new OnHelperCallback<PublicTransport, PublicTransportImpl>() {
            @Override
            public PublicTransport createHelper(final Context context, final PublicTransportImpl publicTransport) {
                return new PublicTransportHelper(context, publicTransport);
            }

            @Override
            public void copyToRealmOrUpdate(final Realm realm, final PublicTransportImpl publicTransport) {
            }
        });
    }
}