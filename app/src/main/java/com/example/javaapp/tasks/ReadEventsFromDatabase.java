package com.example.javaapp.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.javaapp.RecyclerViewAdapter;
import com.example.javaapp.models.Event;
import com.example.javaapp.models.Model;
import com.example.javaapp.models.queries.SelectFavorites;

import java.util.List;

import static com.example.javaapp.BaseApplication.LOG_APP;
import static com.example.javaapp.BaseApplication.LOG_DATA;

public class ReadEventsFromDatabase extends AsyncTask<Void, Void, Void> {

    Context context;
    RecyclerViewAdapter adapter;


    public ReadEventsFromDatabase(Context context, RecyclerViewAdapter adapter) {
        Log.d(LOG_APP, getClass().getName() + " constructed");
        this.context = context;
        this.adapter = adapter;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        Log.d(LOG_DATA, getClass().getName() + ".doInBackground() called with: voids = [" + voids + "]");

        // Read events from database
        List<Event> eventsFromDatabase = Model.getInstance().getQueryStrategy().getSortedData(context);
        List<Event> newFavorites = (new SelectFavorites()).getSortedData(context);

        Log.i(LOG_DATA, getClass().getName() + " " + eventsFromDatabase.size()
                + " events were read from database");
        Log.i(LOG_DATA, getClass().getName() + " " + newFavorites.size()
                + " favorite events were read from database");

        // Update events in model
        List<Event> orderedEvents = Model.getInstance().getOrderedEvents();
        orderedEvents.clear();
        orderedEvents.addAll(eventsFromDatabase);
        Model.getInstance().applyFilter();

        // Update favorites in model
        List<Event> favoriteEvents = Model.getInstance().getFavorites();
        favoriteEvents.clear();
        favoriteEvents.addAll(newFavorites);

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        Log.d(LOG_DATA, getClass().getName() + ".onPostExecute() called with: aVoid = [" + aVoid + "]");

        adapter.updateEventsToDisplay(null);
    }
}
