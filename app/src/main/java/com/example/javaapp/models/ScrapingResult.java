package com.example.javaapp.models;

import android.util.Log;

import java.util.List;

import static com.example.javaapp.BaseApplication.LOG_NET;

public class ScrapingResult {

    List<Event> events;
    List<Location> locations;

    public ScrapingResult(List<Event> events, List<Location> locations) {
        Log.d(LOG_NET, getClass().getName() + " constructed");

        this.events = events;
        this.locations = locations;
    }

    public List<Event> getEvents() { return events; }
    public List<Location> getLocations() { return locations; }
}
