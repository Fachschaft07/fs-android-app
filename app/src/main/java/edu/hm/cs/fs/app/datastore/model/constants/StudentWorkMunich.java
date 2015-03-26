package edu.hm.cs.fs.app.datastore.model.constants;

/**
 * Created by Fabio on 26.03.2015.
 */
public enum StudentWorkMunich {
    MENSA_LEOPOLDSTRASSE("http://www.studentenwerk-muenchen.de/mensa/speiseplan/speiseplan_411_-de.html"),
    MENSA_MARTINSRIED("http://www.studentenwerk-muenchen.de/mensa/speiseplan/speiseplan_412_-de.html"),
    MENSA_GROSSHADERN("http://www.studentenwerk-muenchen.de/mensa/speiseplan/speiseplan_414_-de.html"),
    MENSA_ARCISSTRASSE("http://www.studentenwerk-muenchen.de/mensa/speiseplan/speiseplan_421_-de.html"),
    MENSA_GARCHING("http://www.studentenwerk-muenchen.de/mensa/speiseplan/speiseplan_422_-de.html"),
    MENSA_WEIHENSTEPHAN("http://www.studentenwerk-muenchen.de/mensa/speiseplan/speiseplan_423_-de.html"),
    MENSA_LOTHSTRASSE("http://www.studentenwerk-muenchen.de/mensa/speiseplan/speiseplan_431_-de.html"),
    MENSA_PASING("http://www.studentenwerk-muenchen.de/mensa/speiseplan/speiseplan_432_-de.html"),
    STUBISTRO_MENSA_ROSENHEIM("http://www.studentenwerk-muenchen.de/mensa/speiseplan/speiseplan_441_-de.html"),
    STUCAFE_ADALBERSTRASSE("http://www.studentenwerk-muenchen.de/mensa/speiseplan/speiseplan_512_-de.html"),
    STUCAFE_MENSA_GARCHING("http://www.studentenwerk-muenchen.de/mensa/speiseplan/speiseplan_524_-de.html"),
    STUCAFE_MENSA_WST("http://www.studentenwerk-muenchen.de/mensa/speiseplan/speiseplan_525_-de.html"),
    STUCAFE_AKADEMIE("http://www.studentenwerk-muenchen.de/mensa/speiseplan/speiseplan_526_-de.html"),
    STUCAFE_BOLTZMANNSTRASSE("http://www.studentenwerk-muenchen.de/mensa/speiseplan/speiseplan_527_-de.html"),
    STUCAFE_KARLSTRASSE("http://www.studentenwerk-muenchen.de/mensa/speiseplan/speiseplan_532_-de.html"),
    STUCAFE_PASING("http://www.studentenwerk-muenchen.de/mensa/speiseplan/speiseplan_534_-de.html");

    private final String mUrl;

    StudentWorkMunich(final String url) {
        mUrl = url;
    }

    public String getUrl() {
        return mUrl;
    }
}
