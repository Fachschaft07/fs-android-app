package edu.hm.cs.fs.domain.disk;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import edu.hm.cs.fs.common.constant.Day;
import edu.hm.cs.fs.common.constant.Time;
import edu.hm.cs.fs.common.model.BlackboardEntry;
import edu.hm.cs.fs.common.model.Exam;
import edu.hm.cs.fs.common.model.Group;
import edu.hm.cs.fs.common.model.Holiday;
import edu.hm.cs.fs.common.model.Lesson;
import edu.hm.cs.fs.common.model.LessonGroup;
import edu.hm.cs.fs.common.model.LostFound;
import edu.hm.cs.fs.common.model.Meal;
import edu.hm.cs.fs.common.model.Module;
import edu.hm.cs.fs.common.model.News;
import edu.hm.cs.fs.common.model.Presence;
import edu.hm.cs.fs.common.model.PublicTransport;
import edu.hm.cs.fs.common.model.simple.SimpleJob;
import edu.hm.cs.fs.common.model.simple.SimpleRoom;
import edu.hm.cs.fs.domain.ICachedDataService;
import edu.hm.cs.fs.domain.helper.GroupTypeAdapter;
import edu.hm.cs.fs.domain.helper.LessonGroupSaver;
import edu.hm.cs.fs.restclient.typeadapter.DateTypeAdapter;
import rx.Observable;

/**
 * @author Fabio
 */
public class DiskService implements ICachedDataService {
    private final Map<String, Type> mTypeMapping = new HashMap<>();
    @NonNull
    private final SharedPreferences mPrefs;
    private final Gson mGson;

    @Inject
    public DiskService(@NonNull final SharedPreferences prefs) {
        mPrefs = prefs;
        mGson = new GsonBuilder()
                .registerTypeAdapter(Group.class, new GroupTypeAdapter())
                .registerTypeAdapter(Date.class, new DateTypeAdapter())
                .create();

        mTypeMapping.put(BlackboardEntry.class.getName(), new TypeToken<List<BlackboardEntry>>() {
        }.getType());
        mTypeMapping.put(Exam.class.getName(), new TypeToken<List<Exam>>() {
        }.getType());
        mTypeMapping.put(LessonGroup.class.getName(), new TypeToken<List<LessonGroup>>() {
        }.getType());
        mTypeMapping.put(LessonGroupSaver.class.getName(), new TypeToken<List<LessonGroupSaver>>() {
        }.getType());
        mTypeMapping.put(Lesson.class.getName(), new TypeToken<List<Lesson>>() {
        }.getType());
        mTypeMapping.put(Holiday.class.getName(), new TypeToken<List<Holiday>>() {
        }.getType());
        mTypeMapping.put(Presence.class.getName(), new TypeToken<List<Presence>>() {
        }.getType());
        mTypeMapping.put(News.class.getName(), new TypeToken<List<News>>() {
        }.getType());
        mTypeMapping.put(SimpleJob.class.getName(), new TypeToken<List<SimpleJob>>() {
        }.getType());
        mTypeMapping.put(LostFound.class.getName(), new TypeToken<List<LostFound>>() {
        }.getType());
        mTypeMapping.put(Meal.class.getName(), new TypeToken<List<Meal>>() {
        }.getType());
        mTypeMapping.put(PinedExam.class.getName(), new TypeToken<List<PinedExam>>() {
        }.getType());
    }

    @Override
    public <T> Observable<T> addToCache(@NonNull final T item) {
        final String key = item.getClass().getSimpleName() + "_cache";

        final List<T> cache;
        if (mPrefs.contains(key)) {
            cache = mGson.fromJson(mPrefs.getString(key, ""), mTypeMapping.get(item.getClass().getName()));
        } else {
            cache = new ArrayList<>();
        }

        cache.add(item);

        mPrefs.edit().putString(key, mGson.toJson(cache)).apply();

        return Observable.just(item);
    }

    public <T> Observable<T> setCache(@NonNull final List<T> items) {
        final String key = items.get(0).getClass().getSimpleName() + "_cache";

        mPrefs.edit().putString(key, mGson.toJson(items)).apply();

        return Observable.from(items);
    }

    @Override
    public <T> Observable<Void> cleanCache(@NonNull Class<T> classType) {
        final String key = classType.getSimpleName() + "_cache";
        mPrefs.edit().remove(key).apply();
        return Observable.never();
    }

    @Override
    public <T> List<T> getFromCache(@NonNull final Class<T> classType) {
        final String key = classType.getSimpleName() + "_cache";

        if (!mPrefs.contains(key)) {
            return new ArrayList<>();
        }

        return mGson.fromJson(mPrefs.getString(key, ""), mTypeMapping.get(classType.getName()));
    }

    @Override
    public Observable<Exam> examsOfUser() {
        return Observable.from(getFromCache(PinedExam.class))
                .map(pined -> pined.mExam)
                .toSortedList((exam, exam2) -> exam.getDate().compareTo(exam2.getDate()))
                .flatMap(Observable::from);
    }

    @Override
    public Observable<Boolean> pinExam(@NonNull Exam exam) {
        final List<PinedExam> cache = getFromCache(PinedExam.class);
        boolean found = false;
        int index = 0;
        for (PinedExam pined : cache) {
            if (pined.mExam.getCode().equals(exam.getCode()) &&
                    pined.mExam.getStudy() == exam.getStudy()) {
                found = true;
                break;
            }
            index++;
        }

        if (found) {
            cache.remove(index);
            setCache(cache);
        } else {
            addToCache(new PinedExam(exam));
        }

        return Observable.just(found);
    }

    @Override
    public Observable<Module> moduleById(@NonNull String id) {
        throw new UnsupportedOperationException("Method is not supported by the disk service");
    }

    @Override
    public Observable<PublicTransport> publicTransportPasing() {
        throw new UnsupportedOperationException("Method is not supported by the disk service");
    }

    @Override
    public Observable<PublicTransport> publicTransportLothstrasse() {
        throw new UnsupportedOperationException("Method is not supported by the disk service");
    }

    @Override
    public Observable<SimpleRoom> freeRooms(@NonNull Day day, @NonNull Time time) {
        throw new UnsupportedOperationException("Method is not supported by the disk service");
    }

    @Override
    public Observable<LessonGroup> lessonsByGroup(@NonNull Group group) {
        throw new UnsupportedOperationException("Method is not supported by the disk service");
    }

    @Override
    public Observable<Void> save(@NonNull LessonGroup lessonGroup, @NonNull Integer pk, @NonNull Boolean selected) {
        final List<LessonGroupSaver> lessonGroupSavers = getFromCache(LessonGroupSaver.class);

        boolean changed = false;
        if (selected) {
            final LessonGroupSaver saver = getToUpdate(lessonGroupSavers, lessonGroup);
            if (saver == null) {
                lessonGroupSavers.add(new LessonGroupSaver(lessonGroup, pk));
                changed = true;
            } else if (pk != -1) {
                saver.setSelectedPk(pk);
                changed = true;
            }
        } else {
            LessonGroupSaver saverToDelete = null;
            for (LessonGroupSaver saver : lessonGroupSavers) {
                if (getLessonGroupId(saver.getLessonGroup()).equals(getLessonGroupId(lessonGroup))) {
                    saverToDelete = saver;
                    break;
                }
            }
            if (saverToDelete != null) {
                lessonGroupSavers.remove(saverToDelete);
                changed = true;
            }
        }

        if (changed) { // Only update, if something changed
            setCache(lessonGroupSavers);
        }
        return Observable.never();
    }

    private LessonGroupSaver getToUpdate(final List<LessonGroupSaver> lessonGroupSavers,
                                         final LessonGroup lessonGroup) {
        for (LessonGroupSaver saver : lessonGroupSavers) {
            if (getLessonGroupId(saver.getLessonGroup()).equals(getLessonGroupId(lessonGroup))) {
                return saver;
            }
        }
        return null;
    }

    private String getLessonGroupId(@NonNull final LessonGroup lessonGroup) {
        StringBuilder lessonGroupId = new StringBuilder();
        if (lessonGroup.getGroup() != null) {
            lessonGroupId.append(lessonGroup.getGroup().toString());
        }
        if (lessonGroup.getModule() != null) {
            lessonGroupId.append(lessonGroup.getModule().getId());
        }
        if (lessonGroup.getTeacher() != null) {
            lessonGroupId.append(lessonGroup.getTeacher().getId());
        }
        return lessonGroupId.toString();
    }

    @Override
    public Observable<Boolean> isPkSelected(@NonNull LessonGroup lessonGroup, @NonNull Integer pk) {
        final List<LessonGroupSaver> lessonGroupSavers = getFromCache(LessonGroupSaver.class);
        for (LessonGroupSaver saver : lessonGroupSavers) {
            if (getLessonGroupId(saver.getLessonGroup()).equals(getLessonGroupId(lessonGroup))
                    && saver.getSelectedPk() == pk) {
                return Observable.just(true);
            }
        }
        return Observable.just(false);
    }

    @Override
    public Observable<Boolean> isModuleSelected(@NonNull LessonGroup lessonGroup) {
        final List<LessonGroupSaver> lessonGroupSavers = getFromCache(LessonGroupSaver.class);
        for (LessonGroupSaver saver : lessonGroupSavers) {
            if (getLessonGroupId(saver.getLessonGroup()).equals(getLessonGroupId(lessonGroup))) {
                return Observable.just(true);
            }
        }
        return Observable.just(false);
    }

    @Override
    public Observable<Void> resetTimetable() {
        cleanCache(LessonGroupSaver.class);
        cleanCache(Lesson.class);
        return Observable.never();
    }

    private static class PinedExam {
        private final Exam mExam;

        public PinedExam(Exam exam) {
            mExam = exam;
        }
    }
}
