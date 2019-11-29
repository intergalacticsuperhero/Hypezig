package com.kolloware.hypezigapp.models.filters;

import android.util.Log;

import com.kolloware.hypezigapp.models.Event;

import java.util.Date;
import java.util.List;

import static com.kolloware.hypezigapp.BaseApplication.LOG_APP;

public class WeekFilter implements FilterStrategy {

    @Override
    public void applyFilter(List<Event> input, List<Event> output) {
        Log.d(LOG_APP, getClass().getName() + ".applyFilter() called with: input = ["
                + input.size() + "], output = [" + output.size() + "]");

        long nextWeekDate = (new Date()).getTime() + 7 * 24 * 60 * 60 * 1000;

        output.clear();

        for (Event e : input) {
            if (e.date.getTime() < nextWeekDate) output.add(e);
        }
    }
}
