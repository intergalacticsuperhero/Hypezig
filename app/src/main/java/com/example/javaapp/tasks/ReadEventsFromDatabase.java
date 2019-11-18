package com.example.javaapp.tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.example.javaapp.KreuzerScraper;
import com.example.javaapp.db.AppDatabase;
import com.example.javaapp.models.ScrapingResult;

public class ReadEventsFromDatabase extends AsyncTask<Void, Void, Void> {

    Context context;


    public ReadEventsFromDatabase(Context context) {
        this.context = context;
    }


    @Override
    protected Void doInBackground(Void... voids) {

        try {
            AppDatabase db = AppDatabase.getInstance(context);
            System.out.println("Reading from database...");
            System.out.println(db.eventDao().getAll());
        }
        catch(Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
