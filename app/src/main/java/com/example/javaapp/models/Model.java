package com.example.javaapp.models;

import android.content.Context;

import com.example.javaapp.models.filters.FilterStrategy;
import com.example.javaapp.models.filters.PassthroughFilter;
import com.example.javaapp.models.queries.QueryStrategy;
import com.example.javaapp.models.queries.SortByDate;
import com.example.javaapp.tasks.ReadEventsFromDatabase;

import java.util.ArrayList;
import java.util.List;

public class Model {

    private static Model instance = null;

    private ArrayList<Event> orderedEvents = new ArrayList<>();
    private ArrayList<Event> filteredEvents = new ArrayList<>();

    private QueryStrategy queryStrategy = new SortByDate();
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

    public QueryStrategy getQueryStrategy() {
        return queryStrategy;
    }

    public void setQueryStrategy(QueryStrategy queryStrategy) {
        this.queryStrategy = queryStrategy;
    }

    public void setFilterStrategy(FilterStrategy filterStrategy) {
        this.filterStrategy = filterStrategy;
        applyFilter();
    }
}
