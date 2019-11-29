package com.example.javaapp.db.queries;

import android.content.Context;
import android.util.Log;

import com.example.javaapp.db.AppDatabase;
import com.example.javaapp.models.Event;

import java.util.Date;
import java.util.List;

import static com.example.javaapp.BaseApplication.LOG_DATA;

public class SortByCategory implements QueryStrategy {

    @Override
    public List<Event> getSortedData(Context context) {
        Log.d(LOG_DATA, getClass().getName() + ".getSortedData() called with: context = [" + context + "]");
        return AppDatabase.getInstance(context).eventDao().getCurrentEventsOrderedByCategory((new Date()).getTime() - 2 * 60 * 60 * 1000);
    }
}
