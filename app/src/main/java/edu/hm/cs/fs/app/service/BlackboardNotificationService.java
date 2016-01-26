package edu.hm.cs.fs.app.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import com.fk07.R;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import edu.hm.cs.fs.app.App;
import edu.hm.cs.fs.app.presenter.BasicSubscriber;
import edu.hm.cs.fs.app.ui.MainActivity;
import edu.hm.cs.fs.common.model.BlackboardEntry;
import edu.hm.cs.fs.domain.DataService;
import rx.Notification;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Action2;
import rx.functions.Func0;
import rx.functions.Func1;

public class BlackboardNotificationService extends Service implements Runnable {
    private static final int NOTIFICATION_ID = 97234;
    private final Handler mHandler = new Handler();
    private long mLastExecutionTime = System.currentTimeMillis();

    @Inject
    DataService mDataService;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mHandler.post(this);

        App.getAppComponent(this).inject(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void run() {
        mDataService.blackboardEntriesSince(true, mLastExecutionTime)
                .collect(new Func0<ArrayList<BlackboardEntry>>() {
                    @Override
                    public ArrayList<BlackboardEntry> call() {
                        return new ArrayList<>();
                    }
                }, new Action2<ArrayList<BlackboardEntry>, BlackboardEntry>() {
                    @Override
                    public void call(ArrayList<BlackboardEntry> blackboardEntries, BlackboardEntry blackboardEntry) {
                        blackboardEntries.add(blackboardEntry);
                    }
                })
                .doOnEach(new Action1<Notification<? super ArrayList<BlackboardEntry>>>() {
                    @Override
                    public void call(Notification<? super ArrayList<BlackboardEntry>> notification) {
                        mLastExecutionTime = System.currentTimeMillis();
                    }
                })
                .filter(new Func1<ArrayList<BlackboardEntry>, Boolean>() {
                    @Override
                    public Boolean call(ArrayList<BlackboardEntry> blackboardEntries) {
                        return !blackboardEntries.isEmpty();
                    }
                })
                .subscribe(new Subscriber<ArrayList<BlackboardEntry>>() {
                    @Override
                    public void onCompleted() {
                        // Will be ignored
                    }

                    @Override
                    public void onError(Throwable e) {
                        // Will be ignored
                    }

                    @Override
                    public void onNext(ArrayList<BlackboardEntry> data) {
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
                });
        mHandler.postDelayed(this, TimeUnit.MILLISECONDS.convert(1, TimeUnit.HOURS));
    }
}
