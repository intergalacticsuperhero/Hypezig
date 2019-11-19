package com.example.javaapp.models;

import com.example.javaapp.models.filters.FilterStrategy;
import com.example.javaapp.models.filters.PassthroughFilter;

import java.util.ArrayList;
import java.util.List;

public class Model {

    private static Model instance = null;

    private ArrayList<Event> orderedEvents = new ArrayList<>();
    private ArrayList<Event> filteredEvents = new ArrayList<>();

    private FilterStrategy filterStrategy = new PassthroughFilter();

    private Model() {
    }

    public static Model getInstance() {
        if (instance == null) instance = new Model();
        return instance;
    }

    public void applyFilter() {
        filterStrategy.applyFilter(orderedEvents, filteredEvents);
    }

    public List<Event> getOrderedEvents() {
        return orderedEvents;
    }

    public List<Event> getFilteredEvents() {
        return filteredEvents;
    }

    public void setFilterStrategy(FilterStrategy filterStrategy) {
        this.filterStrategy = filterStrategy;
        applyFilter();
    }
}
