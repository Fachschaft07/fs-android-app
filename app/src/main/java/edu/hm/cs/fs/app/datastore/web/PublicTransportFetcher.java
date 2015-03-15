package edu.hm.cs.fs.app.datastore.web;

import android.content.Context;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import edu.hm.cs.fs.app.datastore.model.PublicTransport;
import edu.hm.cs.fs.app.datastore.model.impl.PublicTransportImpl;
import edu.hm.cs.fs.app.datastore.web.fetcher.AbstractHtmlFetcher;

/**
 * The mvv with every information.<br>
 * (Url (Lothstr.): <a href="http://www.mvg-live.de/ims/dfiStaticAnzeige.svc?haltestelle=Hochschule+M%fcnchen+%28Lothstra%dfe%29&tram=checked"
 * >http://www.mvg-live.de/ims/dfiStaticAnzeige.svc?haltestelle=Hochschule+M%fcnchen+%28Lothstra%dfe%29&tram=checked</a><br>
 * Url (Pasing): <a href="http://www.mvg-live.de/ims/dfiStaticAnzeige.svc?haltestelle=Avenariusplatz&bus=checked">http://www.mvg-live.de/ims/dfiStaticAnzeige.svc?haltestelle=Avenariusplatz&bus=checked</a>)
 *
 * @author Fabio
 *
 */
public class PublicTransportFetcher extends AbstractHtmlFetcher<PublicTransportFetcher, PublicTransportImpl> {
	private static final String MVV_LOTHSTR = "http://www.mvg-live.de/ims/dfiStaticAnzeige.svc?haltestelle=Hochschule+M%fcnchen+%28Lothstra%dfe%29&tram=checked";
	private static final String MVV_PASING = "http://www.mvg-live.de/ims/dfiStaticAnzeige.svc?haltestelle=Avenariusplatz&bus=checked";

	public PublicTransportFetcher(final Context context, PublicTransport.Location location) {
		super(context, location == PublicTransport.Location.PASING ? MVV_PASING : MVV_LOTHSTR);
	}

    @Override
    protected List<PublicTransportImpl> readFromDoc(final Document document) {
        List<PublicTransportImpl> result = new ArrayList<>();

        final Elements rows = document.getElementsByTag("tr");
        for (int indexRow = 0; indexRow < rows.size(); indexRow++) {
            final Element row = rows.get(indexRow);
            if(row.hasClass("rowOdd") || row.hasClass("rowEven")) {
                String line = row.getElementsByClass("lineColumn").get(0).text();
                String station = row.getElementsByClass("stationColumn").get(0).text();

                int inMin = Integer.parseInt(row.getElementsByClass("inMinColumn").get(0).text());
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.MINUTE, -inMin);
                cal.set(Calendar.SECOND, 0);
                cal.set(Calendar.MILLISECOND, 0);
                Date departure =  cal.getTime();

                PublicTransportImpl publicTransport = new PublicTransportImpl();
                publicTransport.setId(line+Long.toString(cal.getTimeInMillis()));
                publicTransport.setLine(line);
                publicTransport.setDestination(station);
                publicTransport.setDeparture(departure);
                result.add(publicTransport);
            }
        }

        return result;
    }
}
