package com.example.javaapp.models.filters;

import com.example.javaapp.models.Event;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CategoryFilter implements FilterStrategy {

    private Set<String> categories = new HashSet<>();

    @Override
    public void applyFilter(List<Event> input, List<Event> output) {
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
