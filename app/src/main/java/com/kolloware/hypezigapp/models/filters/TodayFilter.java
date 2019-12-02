package com.kolloware.hypezigapp.models.filters;

import android.text.format.DateUtils;
import android.util.Log;

import com.kolloware.hypezigapp.models.Event;

import java.util.List;

import static com.kolloware.hypezigapp.BaseApplication.LOG_APP;


public class TodayFilter implements FilterStrategy {


    @Override
    public void applyFilter(List<Event> input, List<Event> output) {
        Log.d(LOG_APP, getClass().getSimpleName() + ".applyFilter() called with: input = ["
                + input.size() + "], output = [" + output.size() + "]");

        output.clear();

        for (Event e : input) {
            if (DateUtils.isToday(e.date.getTime())) {
                output.add(e);
            }
        }
    }
}
