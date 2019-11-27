package com.example.javaapp.models.filters;

import com.example.javaapp.models.Event;
import com.example.javaapp.models.filters.FilterStrategy;

import java.util.Date;
import java.util.List;

public class NextWeekFilter implements FilterStrategy {
    @Override
    public void applyFilter(List<Event> input, List<Event> output) {

        long nextWeekDate = (new Date()).getTime() + 7 * 24 * 60 * 60 * 1000;

        output.clear();

        for (Event e : input) {
            if (e.date.getTime() >= nextWeekDate) output.add(e);
        }
    }
}
