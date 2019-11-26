package com.example.javaapp;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateUtils;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.javaapp.models.Event;
import com.example.javaapp.models.Model;

import java.util.Date;
import java.util.List;

import static com.example.javaapp.BaseApplication.CHANNEL_FAVORITES_ID;

public class DailyAlarmReveiver extends BroadcastReceiver {

    public static final int TYPE_TODAY = 1;
    public static final int TYPE_NEXT_WEEK = 2;


    @Override
    public void onReceive(Context context, Intent intent) {
        System.out.println("You know'd I'm saying!");
        System.out.println(new Date());

        List<Event> favorites = Model.getInstance().getFavorites();

        for (Event forEvent : favorites) {
            if (DateUtils.isToday(forEvent.date.getTime() - 1000 * 60 * 60 * 24)) {
                notifyFavorite(context, forEvent, TYPE_TODAY);
            }

            if (DateUtils.isToday(forEvent.date.getTime() - 1000 * 60 * 60 * 24 * 7)) {
                notifyFavorite(context, forEvent, TYPE_NEXT_WEEK);
            }
        }
    }


    private void notifyFavorite(Context context, Event event, int notificationType) {

        Intent intent = new Intent(context, EventDetailsActivity.class);
        Bundle b = new Bundle();
        b.putInt("eventId", event.eventId);
        intent.putExtras(b);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, event.eventId, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        String label;
        int priority;

        switch(notificationType) {
            case TYPE_TODAY:
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

        NotificationManagerCompat notificiationManager = NotificationManagerCompat.from(context);

        notificiationManager.notify(event.eventId, notification);
    }
}