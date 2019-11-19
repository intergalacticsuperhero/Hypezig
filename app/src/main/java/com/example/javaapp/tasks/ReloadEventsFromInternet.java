package com.example.javaapp.tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.example.javaapp.KreuzerScraper;
import com.example.javaapp.db.AppDatabase;
import com.example.javaapp.models.Event;
import com.example.javaapp.models.Location;
import com.example.javaapp.models.ScrapingResult;

import java.util.HashMap;
import java.util.HashSet;

public class ReloadEventsFromInternet extends AsyncTask<Void, Void, Void> {

    Context context;
    String eventLabels;

    HashMap<String, Location> locations;
    HashSet<Event> events;


    public ReloadEventsFromInternet(Context context) {
        this.context = context;
    }

    @Override
    protected Void doInBackground(Void... voids) {

        try {
            ScrapingResult localResult = KreuzerScraper.fetchEvents();

            AppDatabase db = AppDatabase.getInstance(context);
            Event[] newEvents = localResult.getEvents().toArray(new Event[0]);

            db.eventDao().deleteAll();
            db.eventDao().insertAll(newEvents);
        }
        catch(Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }
}
