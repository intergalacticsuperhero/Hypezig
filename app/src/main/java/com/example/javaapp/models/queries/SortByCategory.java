package com.example.javaapp.models.queries;

import android.content.Context;

import com.example.javaapp.db.AppDatabase;
import com.example.javaapp.models.Event;

import java.util.Date;
import java.util.List;

public class SortByCategory implements QueryStrategy {

    @Override
    public List<Event> getSortedData(Context context) {
        return AppDatabase.getInstance(context).eventDao().getCurrentEventsOrderedByCategory((new Date()).getTime());
    }
}
