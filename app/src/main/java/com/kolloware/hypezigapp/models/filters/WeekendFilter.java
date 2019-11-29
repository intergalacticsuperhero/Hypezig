package com.kolloware.hypezigapp.models.filters;

import android.util.Log;

import com.kolloware.hypezigapp.models.Event;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import static com.kolloware.hypezigapp.BaseApplication.LOG_APP;

public class WeekendFilter implements FilterStrategy {

    private final List<Integer> WEEKEND_DAYS = Arrays.asList(Calendar.FRIDAY,
            Calendar.SATURDAY, Calendar.SUNDAY);

    @Override
    public void applyFilter(List<Event> input, List<Event> output) {
        Log.d(LOG_APP, getClass().getName() + ".applyFilter() called with: input = ["
                + input.size() + "], output = [" + output.size() + "]");

        Calendar forCalendar = Calendar.getInstance();
        Calendar nextMonday = nextMonday();

        for (Event e : input) {
            forCalendar.setTime(e.date);

            if (forCalendar.before(nextMonday) && WEEKEND_DAYS.contains(
                    forCalendar.get(Calendar.DAY_OF_WEEK))) {
                output.add(e);
            }
        }
    }

    private Calendar nextMonday() {
        Log.d(LOG_APP, getClass().getName() + ".nextMonday() called");

        Calendar c = Calendar.getInstance();

        do {
            c.add(Calendar.DAY_OF_WEEK, 1);
        } while (c.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY);

        return c;
    }
}
