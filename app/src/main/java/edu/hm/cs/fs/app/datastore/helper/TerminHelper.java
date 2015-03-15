package edu.hm.cs.fs.app.datastore.helper;

import android.content.Context;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import edu.hm.cs.fs.app.datastore.model.Holiday;
import edu.hm.cs.fs.app.datastore.model.Termin;
import edu.hm.cs.fs.app.datastore.model.impl.TerminImpl;
import edu.hm.cs.fs.app.datastore.web.TerminFetcher;
import edu.hm.cs.fs.app.util.PrefUtils;
import io.realm.Realm;

/**
 * Created by Fabio on 03.03.2015.
 */
public class TerminHelper extends BaseHelper implements Termin {
    private String subject;
    private String scope;
    private Date date;

    TerminHelper(Context context, TerminImpl termin) {
        super(context);
        subject = termin.getSubject();
        scope = termin.getScope();
        date = termin.getDate();
    }

    @Override
    public String getSubject() {
        return subject;
    }

    @Override
    public String getScope() {
        return scope;
    }

    @Override
    public Date getDate() {
        return date;
    }

    public static void listAllEvents(final Context context, final Callback<List<Termin>> callback) {
        listAll(context, new Callback<List<Termin>>() {
            @Override
            public void onResult(List<Termin> result) {
                List<Termin> eventList = new ArrayList<>();
                for (Termin termin : result) {
                    if (!termin.getSubject().endsWith("Tag")) {
                        eventList.add(termin);
                    }
                }

                callback.onResult(eventList);
            }
        });
    }

    public static void listAllHolidays(final Context context, final Callback<List<Holiday>> callback) {
        listAll(context, new Callback<List<Termin>>() {
            @Override
            public void onResult(List<Termin> result) {
                List<Holiday> holidayList = new ArrayList<>();

                String tmpName = null;
                Date tmpStart = null;
                for (Termin termin : result) {
                    if (termin.getSubject().endsWith("Tag")) {
                        if (tmpName == null) {
                            tmpName = termin.getSubject().substring(0, termin.getSubject().indexOf(","));
                            tmpStart = termin.getDate();
                        } else {
                            final String name = tmpName;
                            final Date start = tmpStart;
                            final Date end = termin.getDate();

                            holidayList.add(new Holiday() {
                                @Override
                                public Date getStart() {
                                    return start;
                                }

                                @Override
                                public String getName() {
                                    return name;
                                }

                                @Override
                                public Date getEnd() {
                                    return end;
                                }
                            });

                            tmpName = null;
                            tmpStart = null;
                        }
                    }
                }

                callback.onResult(holidayList);
            }
        });
    }

    public static void listAll(final Context context, final Callback<List<Termin>> callback) {
        PrefUtils.setUpdateInterval(context, TerminFetcher.class, TimeUnit.MILLISECONDS.convert(30l, TimeUnit.DAYS));

        listAll(context, new TerminFetcher(context), TerminImpl.class, callback, new OnHelperCallback<Termin, TerminImpl>() {
            @Override
            public Termin createHelper(Context context, TerminImpl impl) {
                return new TerminHelper(context, impl);
            }

            @Override
            public void copyToRealmOrUpdate(Realm realm, TerminImpl impl) {
                realm.copyToRealmOrUpdate(impl);
            }
        });
    }

    static Termin findById(final Context context, final String id) {
        return new RealmExecutor<Termin>(context) {
            @Override
            public Termin run(final Realm realm) {
                TerminImpl termin = realm.where(TerminImpl.class).equalTo("id", id).findFirst();
                if (termin == null) {
                    List<TerminImpl> terminList = fetchOnlineData(new TerminFetcher(context), realm, new OnHelperCallback<Termin, TerminImpl>() {
                        @Override
                        public Termin createHelper(final Context context, final TerminImpl termin) {
                            return new TerminHelper(context, termin);
                        }

                        @Override
                        public void copyToRealmOrUpdate(final Realm realm, final TerminImpl termin) {
                            realm.copyToRealmOrUpdate(termin);
                        }
                    });
                    for (TerminImpl terminImpl : terminList) {
                        if (terminImpl.getId().equals(id)) {
                            termin = terminImpl;
                            break;
                        }
                    }
                }
                return new TerminHelper(context, termin);
            }
        }.execute();
    }
}
