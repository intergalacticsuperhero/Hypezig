package com.example.javaapp.models;

import java.util.List;

public class ScrapingResult {

    List<Event> events;
    List<Location> locations;

    public ScrapingResult(List<Event> events, List<Location> locations) {
        this.events = events;
        this.locations = locations;
    }

    public List<Event> getEvents() { return events; }
    public List<Location> getLocations() { return locations; }
}
