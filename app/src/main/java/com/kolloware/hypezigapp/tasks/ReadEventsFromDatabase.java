package com.kolloware.hypezigapp.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.kolloware.hypezigapp.ui.RecyclerViewAdapter;
import com.kolloware.hypezigapp.models.Event;
import com.kolloware.hypezigapp.models.Model;
import com.kolloware.hypezigapp.db.queries.SelectFavorites;

import java.util.List;

import static com.kolloware.hypezigapp.BaseApplication.LOG_APP;
import static com.kolloware.hypezigapp.BaseApplication.LOG_DATA;

public class ReadEventsFromDatabase extends AsyncTask<Void, Void, Void> {

    Context context;
    RecyclerViewAdapter adapter;


    public ReadEventsFromDatabase(Context context, RecyclerViewAdapter adapter) {
        Log.d(LOG_APP, getClass().getSimpleName() + " constructed");
        this.context = context;
        this.adapter = adapter;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        Log.d(LOG_DATA, getClass().getSimpleName() + ".doInBackground() called with: voids = ["
                + voids + "]");

        // Read events from database
        List<Event> eventsFromDatabase = Model.getInstance()
                .getQueryStrategy().getSortedData(context);
        List<Event> newFavorites = (new SelectFavorites()).getSortedData(context);

        Log.i(LOG_DATA, getClass().getSimpleName() + " " + eventsFromDatabase.size()
                + " events were read from database");
        Log.i(LOG_DATA, getClass().getSimpleName() + " " + newFavorites.size()
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
        Log.d(LOG_DATA, getClass().getSimpleName() + ".onPostExecute() called with: aVoid = ["
                + aVoid + "]");

        adapter.updateEventsToDisplay(null);
    }
}
