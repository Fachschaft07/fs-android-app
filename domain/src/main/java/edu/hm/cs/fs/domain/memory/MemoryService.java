package edu.hm.cs.fs.domain.memory;

import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import edu.hm.cs.fs.common.constant.Day;
import edu.hm.cs.fs.common.constant.Time;
import edu.hm.cs.fs.common.model.Exam;
import edu.hm.cs.fs.common.model.Group;
import edu.hm.cs.fs.common.model.LessonGroup;
import edu.hm.cs.fs.common.model.Module;
import edu.hm.cs.fs.common.model.PublicTransport;
import edu.hm.cs.fs.common.model.simple.SimpleRoom;
import edu.hm.cs.fs.domain.AbstractCacheService;
import rx.Observable;

public class MemoryService extends AbstractCacheService {
    private final Map<String, List<Object>> mCache = new HashMap<>();

    @Inject
    public MemoryService() {
    }

    @Override
    public <T> Observable<T> addCache(@NonNull final T item) {
        if (!mCache.containsKey(item.getClass().getName())) {
            mCache.put(item.getClass().getName(), new ArrayList<>());
        }
        mCache.get(item.getClass().getName()).add(item);

        return Observable.just(item);
    }

    @Override
    public <T> Observable<Void> cleanCache(@NonNull final Class<T> classType) {
        if (mCache.containsKey(classType.getName())) {
            mCache.remove(classType.getName());
        }
        return Observable.never();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> List<T> getCache(@NonNull final Class<T> classType) {
        if (mCache.containsKey(classType.getName())) {
            return (List<T>) mCache.get(classType.getName());
        }
        return new ArrayList<>();
    }

    @Override
    public Observable<Exam> examsOfUser() {
        throw new UnsupportedOperationException("Method is not supported by the memory service");
    }

    @Override
    public Observable<Boolean> isExamPined(@NonNull Exam exam) {
        throw new UnsupportedOperationException("Method is not supported by the memory service");
    }

    @Override
    public Observable<Boolean> pinExam(@NonNull Exam exam) {
        throw new UnsupportedOperationException("Method is not supported by the memory service");
    }

    @Override
    public Observable<Module> moduleById(@NonNull String id) {
        throw new UnsupportedOperationException("Method is not supported by the memory service");
    }

    @Override
    public Observable<PublicTransport> publicTransportPasing() {
        throw new UnsupportedOperationException("Method is not supported by the memory service");
    }

    @Override
    public Observable<PublicTransport> publicTransportLothstrasse() {
        throw new UnsupportedOperationException("Method is not supported by the memory service");
    }

    @Override
    public Observable<SimpleRoom> freeRooms(@NonNull Day day, @NonNull Time time) {
        throw new UnsupportedOperationException("Method is not supported by the memory service");
    }

    @Override
    public Observable<LessonGroup> lessonsByGroup(@NonNull Group group) {
        throw new UnsupportedOperationException("Method is not supported by the memory service");
    }

    @Override
    public Observable<Void> save(@NonNull LessonGroup lessonGroup, @NonNull Boolean selected) {
        throw new UnsupportedOperationException("Method is not supported by the memory service");
    }

    @Override
    public Observable<Void> save(@NonNull LessonGroup lessonGroup, @NonNull Integer pk, @NonNull Boolean selected) {
        throw new UnsupportedOperationException("Method is not supported by the memory service");
    }

    @Override
    public Observable<Boolean> isPkSelected(@NonNull LessonGroup lessonGroup, @NonNull Integer pk) {
        throw new UnsupportedOperationException("Method is not supported by the memory service");
    }

    @Override
    public Observable<Boolean> isModuleSelected(@NonNull LessonGroup lessonGroup) {
        throw new UnsupportedOperationException("Method is not supported by the memory service");
    }
}
