package edu.hm.cs.fs.app.util;

import android.app.ActivityManager;
import android.content.Context;

/**
 * Created by FHellman on 10.12.2015.
 */
public final class ServiceUtils {
    private ServiceUtils() {
    }

    public static boolean isServiceRunning(Context context, Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
