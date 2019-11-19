package com.example.javaapp.tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.example.javaapp.RecyclerViewAdapter;
import com.example.javaapp.models.Event;
import com.example.javaapp.models.Model;

import java.util.List;

public abstract class ReadEventsFromDatabase extends AsyncTask<Void, Void, Void> {

    Context context;
    RecyclerViewAdapter adapter;

    public ReadEventsFromDatabase(Context context, RecyclerViewAdapter adapter) {
        this.context = context;
        this.adapter = adapter;
    }

    protected abstract List<Event> getSortedData();

    @Override
    protected Void doInBackground(Void... voids) {

        try {
            List<Event> eventsFromDatabase = getSortedData();

            List<Event> orderedEvents = Model.getInstance().getOrderedEvents();
            orderedEvents.clear();
            orderedEvents.addAll(eventsFromDatabase);

            Model.getInstance().applyFilter();
        }
        catch(Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        adapter.notifyDataSetChanged();
    }
}
