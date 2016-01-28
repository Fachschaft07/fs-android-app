package edu.hm.cs.fs.domain;

import android.content.Context;
import android.support.annotation.NonNull;

/**
 *
 */
public interface Migration {
    /**
     * Version 2.0.1.0
     **/
    int VERSION_CODE_2010 = 201;
    /**
     * Version 2.1.1.0
     **/
    int VERSION_CODE_2110 = 2110;

    /**
     * Migrate from an older backend version to a newer one.
     *
     * @param context to access the android device storage.
     * @param dataService to access the data storage.
     * @param oldVersion  of the backend.
     * @param newVersion  of the backend.
     */
    static void onUpdate(@NonNull final Context context,
                          @NonNull final IDataService dataService,
                          @NonNull final Long oldVersion,
                          @NonNull final Long newVersion) {
        if (oldVersion < VERSION_CODE_2010) {
            dataService.resetTimetable();
        } else if (newVersion > VERSION_CODE_2110) {
            // TODO Migrate Timetable from file to diskservice
        }
    }
}
