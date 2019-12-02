package com.kolloware.hypezigapp.db.queries;

import android.content.Context;
import android.util.Log;

import com.kolloware.hypezigapp.db.AppDatabase;
import com.kolloware.hypezigapp.models.Event;

import java.util.Date;
import java.util.List;

import static com.kolloware.hypezigapp.BaseApplication.LOG_DATA;

public class SortByDate implements QueryStrategy {

    @Override
    public List<Event> getSortedData(Context context) {
        Log.d(LOG_DATA, getClass().getSimpleName() + ".getSortedData() called with: context = [" + context + "]");
        return AppDatabase.getInstance(context).eventDao().getCurrentEvents((new Date()).getTime() - 2 * 60 * 60 * 1000);
    }
}
