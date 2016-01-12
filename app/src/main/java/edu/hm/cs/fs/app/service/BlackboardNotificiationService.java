package edu.hm.cs.fs.app.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.util.concurrent.TimeUnit;

/**
 * Created by FHellman on 10.12.2015.
 */
public class BlackboardNotificiationService extends Service implements Runnable {
    private static final int NOTIFICATION_ID = 97234;
    private final Handler mHandler = new Handler();
    private long mLastExecutionTime = System.currentTimeMillis();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mHandler.post(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void run() {
        /*
        ModelFactory.getBlackboard().getAllSince(mLastExecutionTime, new ICallback<List<BlackboardEntry>>() {
            @Override
            public void onSuccess(List<BlackboardEntry> data) {
                if (!data.isEmpty()) {
                    NotificationManager notificationManager =
                            (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                    // create Notification
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext())
                            .setSmallIcon(R.drawable.ic_launcher)
                            .setContentTitle(getString(R.string.app_name))
                            .setContentText(getResources().getQuantityString(R.plurals.blackboard_news, data.size(), data.size()))
                            .setAutoCancel(true);

                    // Add blackboard items
                    NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
                    for (BlackboardEntry entry : data) {
                        inboxStyle.addLine(entry.getSubject());
                    }

                    // Set this as style to the Notification
                    builder.setStyle(inboxStyle);

                    // Define what to do, when the Notification is clicked
                    Intent resultIntent = new Intent(getApplicationContext(), MainActivity.class);
                    final TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
                    stackBuilder.addParentStack(MainActivity.class);
                    stackBuilder.addNextIntent(resultIntent);
                    PendingIntent resultPendingIntent = stackBuilder
                            .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
                    builder.setContentIntent(resultPendingIntent);

                    // Notify the Manager
                    notificationManager.notify(NOTIFICATION_ID, builder.build());
                }

                // update last execution time
                mLastExecutionTime = System.currentTimeMillis();
            }

            @Override
            public void onError(@NonNull IError error) {
                // Do nothing...
                mLastExecutionTime = System.currentTimeMillis();
            }
        });
        */
        mHandler.postDelayed(this, TimeUnit.MILLISECONDS.convert(1, TimeUnit.HOURS));
    }
}
