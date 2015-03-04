package edu.hm.cs.fs.app.datastore.web;

import java.util.List;

import android.content.Context;

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

	public PublicTransportFetcher(final Context context, Type type) {
		super(context, type == Type.PASING ? MVV_PASING : MVV_LOTHSTR);
	}

	@Override
	protected List<PublicTransportImpl> read(String url) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public enum Type {
		PASING, LOTHSTR
	}
}
