package com.example.javaapp.models;

import com.example.javaapp.models.filters.CategoryFilter;
import com.example.javaapp.models.filters.FilterStrategy;
import com.example.javaapp.models.filters.PassthroughFilter;
import com.example.javaapp.models.queries.QueryStrategy;
import com.example.javaapp.models.queries.SortByDate;

import java.util.ArrayList;
import java.util.List;

public class Model {

    private static Model instance = null;

    private ArrayList<Event> orderedEvents = new ArrayList<>();
    private ArrayList<Event> filteredEvents = new ArrayList<>();
    private ArrayList<Event> favorites = new ArrayList<>();

    private QueryStrategy queryStrategy = new SortByDate();
    private FilterStrategy filterStrategy = new PassthroughFilter();
    private CategoryFilter categoryFilter = new CategoryFilter();

    private Model() {
    }

    public static Model getInstance() {
        if (instance == null) instance = new Model();
        return instance;
    }

    public void applyFilter() {
        List<Event> localResult = new ArrayList<>();
        filterStrategy.applyFilter(orderedEvents, localResult);
        categoryFilter.applyFilter(localResult, filteredEvents);
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

    public List<Event> getFavorites() { return favorites; }

    public void setQueryStrategy(QueryStrategy queryStrategy) {
        this.queryStrategy = queryStrategy;
    }

    public void setFilterStrategy(FilterStrategy filterStrategy) {
        this.filterStrategy = filterStrategy;
        applyFilter();
    }

    public CategoryFilter getCategoryFilter() {
        return categoryFilter;
    }
}
