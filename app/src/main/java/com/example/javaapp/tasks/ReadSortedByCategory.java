package com.example.javaapp.tasks;

import android.content.Context;

import com.example.javaapp.RecyclerViewAdapter;
import com.example.javaapp.db.AppDatabase;
import com.example.javaapp.models.Event;

import java.util.List;

public class ReadSortedByCategory extends ReadEventsFromDatabase {
    public ReadSortedByCategory(Context context, List<Event> events, RecyclerViewAdapter adapter) {
        super(context, events, adapter);
    }

    @Override
    protected List<Event> getSortedData() {
        return AppDatabase.getInstance(context).eventDao().getAllOrderedByCategory();
    }
}
