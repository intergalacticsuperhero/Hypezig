package com.example.javaapp.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.javaapp.KreuzerScraper;
import com.example.javaapp.RecyclerViewAdapter;
import com.example.javaapp.db.AppDatabase;
import com.example.javaapp.models.Event;
import com.example.javaapp.models.ScrapingResult;

import static com.example.javaapp.BaseApplication.LOG_APP;
import static com.example.javaapp.BaseApplication.LOG_NET;

public class ReloadEventsFromInternet extends AsyncTask<Void, Void, Void> {

    private Context context;
    private SwipeRefreshLayout layout;
    private RecyclerViewAdapter adapter;

    public ReloadEventsFromInternet(Context context, SwipeRefreshLayout layout,
                                    RecyclerViewAdapter adapter) {
        Log.d(LOG_APP, getClass().getName() + " constructed");
        this.context = context;
        this.layout = layout;
        this.adapter = adapter;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        Log.d(LOG_NET, getClass().getName() + ".doInBackground() called with: voids = ["
                + voids + "]");

        try {
            // Scrape information from website
            ScrapingResult localResult = KreuzerScraper.fetchEvents();

            // Put results into database
            AppDatabase db = AppDatabase.getInstance(context);
            Event[] newEvents = localResult.getEvents().toArray(new Event[0]);

            for (Event forEvent : newEvents) {
                Event oldEvent = db.eventDao().getByProviderNameAndId(forEvent.providerName,
                        forEvent.providerId);

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
            Log.e(LOG_NET, getClass().getName() + ".doInBackground: ", e);
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        Log.d(LOG_NET, getClass().getName() + ".onPostExecute() called with: aVoid = ["
                + aVoid + "]");

        super.onPostExecute(aVoid);

        layout.setRefreshing(false);
        (new ReadEventsFromDatabase(context, adapter)).execute();
    }
}
