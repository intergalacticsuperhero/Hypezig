package com.kolloware.hypezigapp.models.filters;

import com.kolloware.hypezigapp.models.Event;

import java.util.List;

public interface FilterStrategy {

    void applyFilter(List<Event> input, List<Event> output);
}
