package com.example.javaapp;

import android.app.AlarmManager;
import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import java.util.Date;

public class BaseApplication extends Application {

    public static final String CHANNEL_FAVORITES_ID = "favorites_reminder";


    @Override
    public void onCreate() {
        super.onCreate();

        createNotificationChannels();
        initAlarmReceiver();
    }


    private void createNotificationChannels() {
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
        AlarmManager manager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, DailyAlarmReveiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
        manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, (new Date()).getTime() + 1000 * 60, AlarmManager.INTERVAL_DAY, pendingIntent);
    }
}
