package com.example.javaapp.models.filters;

import com.example.javaapp.models.Event;
import com.example.javaapp.models.filters.FilterStrategy;

import java.util.List;

public class PassthroughFilter implements FilterStrategy {
    @Override
    public void applyFilter(List<Event> input, List<Event> output) {
        output.clear();
        output.addAll(input);
    }
}
