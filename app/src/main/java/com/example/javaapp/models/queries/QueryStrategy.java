package com.example.javaapp.models.queries;

import android.content.Context;

import com.example.javaapp.models.Event;

import java.util.List;

public interface QueryStrategy {

    List<Event> getSortedData(Context context);
}
