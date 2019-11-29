package com.example.javaapp.db.queries;

import android.content.Context;

import com.example.javaapp.models.Event;

import java.util.List;

public interface QueryStrategy {

    List<Event> getSortedData(Context context);
}
