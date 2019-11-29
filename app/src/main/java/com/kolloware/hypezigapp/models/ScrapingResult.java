package com.kolloware.hypezigapp.models;

import android.util.Log;

import java.util.List;

import static com.kolloware.hypezigapp.BaseApplication.LOG_NET;

public class ScrapingResult {

    List<Event> events;

    public ScrapingResult(List<Event> events) {
        Log.d(LOG_NET, getClass().getName() + " constructed");

        this.events = events;
    }

    public List<Event> getEvents() { return events; }
}
