package com.example.javaapp.tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.example.javaapp.RecyclerViewAdapter;
import com.example.javaapp.models.Event;

import java.util.List;

public abstract class ReadEventsFromDatabase extends AsyncTask<Void, Void, Void> {

    Context context;
    List<Event> events;
    RecyclerViewAdapter adapter;

    public ReadEventsFromDatabase(Context context, List<Event> events, RecyclerViewAdapter adapter) {
        this.context = context;
        this.events = events;
        this.adapter = adapter;
    }

    protected abstract List<Event> getSortedData();

    @Override
    protected Void doInBackground(Void... voids) {

        try {
            List<Event> eventsFromDatabase = getSortedData();
            events.clear();
            events.addAll(eventsFromDatabase);
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
