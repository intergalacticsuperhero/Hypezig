package com.example.javaapp.models.filters;

import android.text.format.DateUtils;
import android.util.Log;

import com.example.javaapp.models.Event;

import java.util.List;

import static com.example.javaapp.BaseApplication.LOG_APP;


public class TodayFilter implements FilterStrategy {


    @Override
    public void applyFilter(List<Event> input, List<Event> output) {
        Log.d(LOG_APP, getClass().getName() + ".applyFilter() called with: input = ["
                + input.size() + "], output = [" + output.size() + "]");

        output.clear();

        for (Event e : input) {
            if (DateUtils.isToday(e.date.getTime())) {
                output.add(e);
            }
        }
    }
}
