package com.example.javaapp.models;

import android.util.Log;

import java.util.List;

import static com.example.javaapp.BaseApplication.LOG_NET;

public class ScrapingResult {

    List<Event> events;

    public ScrapingResult(List<Event> events) {
        Log.d(LOG_NET, getClass().getName() + " constructed");

        this.events = events;
    }

    public List<Event> getEvents() { return events; }
}
