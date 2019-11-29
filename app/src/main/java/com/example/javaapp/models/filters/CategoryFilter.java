package com.example.javaapp.models.filters;

import android.util.Log;

import com.example.javaapp.models.Event;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.example.javaapp.BaseApplication.LOG_APP;

public class CategoryFilter implements FilterStrategy {

    private Set<String> categories = new HashSet<>();

    @Override
    public void applyFilter(List<Event> input, List<Event> output) {
        Log.d(LOG_APP, getClass().getName() + ".applyFilter() called with: input = ["
                + input.size() + "], output = [" + output.size() + "]");

        output.clear();

        if (categories.isEmpty()) {
            output.addAll(input);
        }
        else {
            for (Event forEvent : input) {
                if (categories.contains(forEvent.category)) {
                    output.add(forEvent);
                }
            }
        }
    }

    public void setCategories(Set<String> categories) {
        this.categories = categories;
    }
}
