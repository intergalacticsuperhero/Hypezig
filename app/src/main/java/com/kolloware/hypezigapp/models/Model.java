package com.kolloware.hypezigapp.models;

import android.util.Log;

import com.kolloware.hypezigapp.models.filters.CategoryFilter;
import com.kolloware.hypezigapp.models.filters.FilterStrategy;
import com.kolloware.hypezigapp.models.filters.NextWeekFilter;
import com.kolloware.hypezigapp.db.queries.QueryStrategy;
import com.kolloware.hypezigapp.db.queries.SortByDate;

import java.util.ArrayList;
import java.util.List;

import static com.kolloware.hypezigapp.BaseApplication.LOG_APP;

public class Model {

    private static Model instance = null;

    private ArrayList<Event> orderedEvents = new ArrayList<>();
    private ArrayList<Event> filteredEvents = new ArrayList<>();
    private ArrayList<Event> favorites = new ArrayList<>();

    private QueryStrategy queryStrategy = new SortByDate();
    private FilterStrategy filterStrategy = new NextWeekFilter();
    private CategoryFilter categoryFilter = new CategoryFilter();

    private Model() {
        Log.d(LOG_APP, getClass().getName() + " constructed");
    }

    public static Model getInstance() {
        Log.d(LOG_APP, Model.class.getName() + ".getInstance() called");

        if (instance == null) instance = new Model();
        return instance;
    }

    public void applyFilter() {
        Log.d(LOG_APP, getClass().getName() + ".applyFilter() called");

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

    public CategoryFilter getCategoryFilter() {
        return categoryFilter;
    }

    public void setQueryStrategy(QueryStrategy queryStrategy) {
        Log.d(LOG_APP, getClass().getName() + ".setQueryStrategy() called");

        this.queryStrategy = queryStrategy;
    }

    public void setFilterStrategy(FilterStrategy filterStrategy) {
        Log.d(LOG_APP, getClass().getName() + ".setFilterStrategy() called");

        this.filterStrategy = filterStrategy;
        applyFilter();
    }
}
