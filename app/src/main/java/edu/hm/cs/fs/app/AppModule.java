package edu.hm.cs.fs.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import edu.hm.cs.fs.domain.DataMigration;
import edu.hm.cs.fs.domain.DataService;
import edu.hm.cs.fs.domain.SchedulerProvider;
import edu.hm.cs.fs.domain.cloud.RestApiService;
import edu.hm.cs.fs.domain.disk.DiskService;
import edu.hm.cs.fs.domain.memory.MemoryService;

@Module
public class AppModule {
    private final App mApp;

    public AppModule(@NonNull final App app) {
        mApp = app;
    }

    @Provides
    @Singleton
    public Context provideApplicationContext() {
        return mApp;
    }

    @Provides
    @Singleton
    public SharedPreferences provideSharedPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(mApp);
    }

    @Provides
    public SchedulerProvider provideSchedulerProvider() {
        return SchedulerProvider.DEFAULT;
    }

    @Provides
    @Singleton
    public DataMigration provideVersionManager(DataService dataService) {
        return new DataMigration(mApp, dataService);
    }

    @Provides
    public RestApiService provideCloudCacheService(SharedPreferences prefs) {
        return new RestApiService(prefs);
    }

    @Provides
    public DiskService provideDiskCacheService(SharedPreferences prefs) {
        return new DiskService(prefs);
    }

    @Provides
    public MemoryService provideMemoryCacheService() {
        return new MemoryService();
    }

    @Provides
    @Singleton
    public DataService provideCacheService(@NonNull final SchedulerProvider schedulerProvider,
                                           @NonNull final MemoryService memoryCacheService,
                                           @NonNull final DiskService diskCacheService,
                                           @NonNull final RestApiService cloudCacheService) {
        return new DataService(
                schedulerProvider,
                memoryCacheService,
                diskCacheService,
                cloudCacheService
        );
    }

}
