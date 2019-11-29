package com.kolloware.hypezigapp.util;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.kolloware.hypezigapp.R;
import com.kolloware.hypezigapp.models.Event;
import com.kolloware.hypezigapp.models.Model;
import com.kolloware.hypezigapp.ui.EventDetailsActivity;

import java.util.Date;
import java.util.List;

import static com.kolloware.hypezigapp.BaseApplication.CHANNEL_FAVORITES_ID;
import static com.kolloware.hypezigapp.BaseApplication.LOG_APP;

public class DailyAlarmReceiver extends BroadcastReceiver {

    public static final int TYPE_TOMORROW = 1;
    public static final int TYPE_NEXT_WEEK = 2;


    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(LOG_APP, getClass().getName() + ".onReceive() called with: context = ["
                + context + "], intent = [" + intent + "]");
        Log.i(LOG_APP, "onReceive: was called at " + new Date());

        List<Event> favorites = Model.getInstance().getFavorites();

        for (Event forEvent : favorites) {
            if (DateUtils.isToday(forEvent.date.getTime() - 1000 * 60 * 60 * 24)) {
                Log.i(LOG_APP, "onReceive: sending tomorrow notification for event: "
                        + forEvent);
                notifyFavorite(context, forEvent, TYPE_TOMORROW);
            }

            if (DateUtils.isToday(forEvent.date.getTime() - 1000 * 60 * 60 * 24 * 7)) {
                Log.i(LOG_APP, "onReceive: sending next week notification for event: "
                        + forEvent);
                notifyFavorite(context, forEvent, TYPE_NEXT_WEEK);
            }
        }
    }


    private void notifyFavorite(Context context, Event event, int notificationType) {
        Log.d(LOG_APP, getClass().getName() + ".DailyAlarmReceiver.notifyFavorite() called "
                + "with: context = [" + context + "], event = [" + event + "], notificationType = ["
                + notificationType + "]");

        Intent intent = new Intent(context, EventDetailsActivity.class);
        Bundle b = new Bundle();
        b.putInt("eventId", event.eventId);
        intent.putExtras(b);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, event.eventId, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        String label;
        int priority;

        switch(notificationType) {
            case TYPE_TOMORROW:
                label = "Veranstaltung morgen";
                priority = NotificationCompat.PRIORITY_HIGH;
                break;
            case TYPE_NEXT_WEEK:
                label = "Nächste Woche";
                priority = NotificationCompat.PRIORITY_DEFAULT;
                break;
            default:
                label = "This should never happen";
                priority = NotificationCompat.PRIORITY_LOW;
        }

        Notification notification = new NotificationCompat.Builder(context, CHANNEL_FAVORITES_ID)
                .setSmallIcon(R.drawable.icon_favorite)
                .setContentTitle(label)
                .setContentText(event.title)
                .setContentIntent(pendingIntent)
                .setPriority(priority)
                .setCategory(NotificationCompat.CATEGORY_EVENT)
                .build();

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        notificationManager.notify(event.eventId, notification);
    }
}