package com.example.javaapp.models.queries;

import android.content.Context;

import com.example.javaapp.db.AppDatabase;
import com.example.javaapp.models.Event;

import java.util.Date;
import java.util.List;

public class SortByName implements QueryStrategy {

    @Override
    public List<Event> getSortedData(Context context) {
        return AppDatabase.getInstance(context).eventDao().getCurrentEventsOrderedByTitle((new Date()).getTime() - 2 * 60 * 60 * 1000);
    }
}
