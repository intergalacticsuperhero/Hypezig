package com.example.javaapp.models.filters;

import android.text.format.DateUtils;

import com.example.javaapp.models.Event;

import java.util.List;


public class TodayFilter implements FilterStrategy {


    @Override
    public void applyFilter(List<Event> input, List<Event> output) {
        output.clear();

        for (Event e : input) {
            if (DateUtils.isToday(e.date.getTime())) {
                output.add(e);
            }
        }
    }
}
