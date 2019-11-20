package com.example.javaapp.models.filters;

import com.example.javaapp.models.Event;

import java.util.List;

public interface FilterStrategy {

    void applyFilter(List<Event> input, List<Event> output);
}
