package com.example.javaapp.tasks;

import android.content.Context;
import android.os.AsyncTask;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.javaapp.KreuzerScraper;
import com.example.javaapp.RecyclerViewAdapter;
import com.example.javaapp.db.AppDatabase;
import com.example.javaapp.models.Event;
import com.example.javaapp.models.ScrapingResult;

public class ReloadEventsFromInternet extends AsyncTask<Void, Void, Void> {

    Context context;
    SwipeRefreshLayout layout;
    RecyclerViewAdapter adapter;

    public ReloadEventsFromInternet(Context context, SwipeRefreshLayout layout, RecyclerViewAdapter adapter) {
        this.context = context;
        this.layout = layout;
        this.adapter = adapter;
    }

    @Override
    protected Void doInBackground(Void... voids) {

        try {
            ScrapingResult localResult = KreuzerScraper.fetchEvents();

            AppDatabase db = AppDatabase.getInstance(context);
            Event[] newEvents = localResult.getEvents().toArray(new Event[0]);

            for (Event forEvent : newEvents) {
                Event oldEvent = db.eventDao().getByProviderNameAndId(forEvent.providerName, forEvent.providerId);

                if (oldEvent == null) {
                    db.eventDao().insertAll(forEvent);
                }
                else {
                    oldEvent.title = forEvent.title;
                    oldEvent.subtitle = forEvent.subtitle;
                    oldEvent.details = forEvent.details;
                    oldEvent.tags = forEvent.tags;
                    oldEvent.imageURL = forEvent.imageURL;
                    db.eventDao().update(oldEvent);
                }
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        layout.setRefreshing(false);
        (new ReadEventsFromDatabase(context, adapter)).execute();
    }
}
