package edu.hm.cs.fs.app.datastore.helper;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
                /**
                 Osterferien 2015, erster Tag
                 Osterferien 2015, letzter Tag
                 Pfingstferien 2015, erster Tag
                  Pfingstferien 2015, letzter Tag
                 SS2015, Pr&#252;fungszeitraum
                 SS2015, erster Vorlesungstag
                 SS2016, erster Vorlesungstag
                 WS2014, Pr&#252;fungszeitraum
                 WS2015, Pr&#252;fungszeitraum
                 WS2015, erster Vorlesungstag
                 Weihnachtsferien 2014, erster Tag
                 Weihnachtsferien 2015, erster Tag
                 Weihnachtsferien 2015, letzter Tag
                 Weihnachtsferien 2016, letzter Tag
                 */
                Map<String, Holiday> holidayMap = new HashMap<>();

                Collections.sort(result, new Comparator<Termin>() {
                    @Override
                    public int compare(Termin lhs, Termin rhs) {
                        String leftName = lhs.getSubject().replaceAll("[0-9]+", "").trim();
                        String rightName = rhs.getSubject().replaceAll("[0-9]+", "").trim();
                        return leftName.compareTo(rightName);
                    }
                });

                for (final Termin termin : result) {
                    if (termin.getSubject().endsWith("erster Tag")) {
                        final String name = extractName(termin.getSubject(), termin.getDate());
                        holidayMap.put(name, new Holiday() {
                            @Override
                            public Date getStart() {
                                return termin.getDate();
                            }

                            @Override
                            public String getName() {
                                return name;
                            }

                            @Override
                            public Date getEnd() {
                                return null;
                            }
                        });
                    } else if(termin.getSubject().endsWith("letzter Tag")) {
                        final String name = extractName(termin.getSubject(), termin.getDate());
                        final Holiday holiday = holidayMap.get(name);

                        holidayMap.put(name, new Holiday() {
                            @Override
                            public String getName() {
                                return holiday.getName();
                            }

                            @Override
                            public Date getStart() {
                                return holiday.getStart();
                            }

                            @Override
                            public Date getEnd() {
                                return termin.getDate();
                            }
                        });
                    }
                }

                Log.d("TerminHelper", "Found holidays: " + holidayMap.keySet());

                callback.onResult(new ArrayList<Holiday>(holidayMap.values()));
            }
        });
    }

    private static String extractName(String subject, Date date) {
        String tmpName = subject.substring(0, subject.indexOf(",")).trim();
        if (tmpName.startsWith("Weihnachtsferien") && subject.endsWith("erster Tag")) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            tmpName += "/" + (cal.get(Calendar.YEAR) + 1);
        } else if (tmpName.startsWith("Weihnachtsferien") && subject.endsWith("letzter Tag")) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            tmpName = tmpName.replaceAll("[0-9]+", "") + (cal.get(Calendar.YEAR) - 1) + "/" + cal.get(Calendar.YEAR);
        }
        return tmpName;
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
