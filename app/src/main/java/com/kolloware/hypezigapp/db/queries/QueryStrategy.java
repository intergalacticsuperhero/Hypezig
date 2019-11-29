package com.kolloware.hypezigapp.db.queries;

import android.content.Context;

import com.kolloware.hypezigapp.models.Event;

import java.util.List;

public interface QueryStrategy {

    List<Event> getSortedData(Context context);
}
