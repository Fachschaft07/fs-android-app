package edu.hm.cs.fs.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import edu.hm.cs.fs.app.database.cloud.RestApiService;
import edu.hm.cs.fs.app.database.disk.DiskService;
import edu.hm.cs.fs.app.database.memory.MemoryService;
import edu.hm.cs.fs.app.domain.DataMigration;
import edu.hm.cs.fs.app.domain.DataService;
import edu.hm.cs.fs.app.domain.SchedulerProvider;

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
    public RestApiService provideCloudCacheService() {
        return new RestApiService();
    }

    @Provides
    public DiskService provideDiskCacheService() {
        return new DiskService(mApp);
    }

    @Provides
    public MemoryService provideMemoryCacheService(SharedPreferences prefs) {
        return new MemoryService(prefs);
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
