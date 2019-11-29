package com.example.javaapp;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;

import java.util.Date;

public class BaseApplication extends Application {

    public static final String CHANNEL_FAVORITES_ID = "favorites_reminder";

    /**
     * Logging tag used for common UI lifecycle events
     */
    public static final String LOG_UI = "UI";

    /**
     * Logging tag used for any kind of network I/O communication
     */
    public static final String LOG_NET = "NET";

    /**
     * Logging tag used for storage; local files, preferences and databases
     */
    public static final String LOG_DATA = "DATA";

    /**
     * Logging tag used for business logic and app related things not
     * already covered by the other log tags
     */
    public static final String LOG_APP = "APP";


    @Override
    public void onCreate() {
        Log.d(LOG_APP, "BaseApplication.onCreate() called");

        super.onCreate();

        initLogging();
        createNotificationChannels();
        initAlarmReceiver();
    }


    private void initLogging() {
        Log.i(LOG_APP, "Creating Application");

        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                Log.i(LOG_UI, activity.getClass().getSimpleName() + " created");
            }

            @Override
            public void onActivityStarted(Activity activity) {
                Log.i(LOG_UI, activity.getClass().getSimpleName() + " started");
            }

            @Override
            public void onActivityResumed(Activity activity) {
                Log.i(LOG_UI, activity.getClass().getSimpleName() + " resumed");
            }

            @Override
            public void onActivityPaused(Activity activity) {
                Log.i(LOG_UI, activity.getClass().getSimpleName() + " paused");
            }

            @Override
            public void onActivityStopped(Activity activity) {
                Log.i(LOG_UI, activity.getClass().getSimpleName() + " stopped");
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
                Log.i(LOG_UI, activity.getClass().getSimpleName() + " saved");
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                Log.i(LOG_UI, activity.getClass().getSimpleName() + " destroyed");
            }
        });

        if (BuildConfig.DEBUG) {
            StrictMode.enableDefaults();
        }
    }


    private void createNotificationChannels() {
        Log.d(LOG_APP, getClass().getName() + ".createNotificationChannels() called");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel1 = new NotificationChannel(
                    CHANNEL_FAVORITES_ID,
                    "Favoriten erinnern",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            channel1.setDescription("Erinnert an bevorstehende Events (morgen + nächste Woche)");

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel1);
        }
    }


    private void initAlarmReceiver() {
        Log.d(LOG_APP, getClass().getName() + ".initAlarmReceiver() called");

        AlarmManager manager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, DailyAlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this,
                0, intent, 0);
        manager.setInexactRepeating(AlarmManager.RTC_WAKEUP,
                (new Date()).getTime() + 1000 * 60,
                AlarmManager.INTERVAL_DAY, pendingIntent);
    }
}
