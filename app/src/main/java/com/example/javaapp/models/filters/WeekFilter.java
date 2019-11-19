package com.example.javaapp.models.filters;

import com.example.javaapp.models.Event;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class WeekFilter implements FilterStrategy {

    private final List<Integer> WEEKEND_DAYS = Arrays.asList(Calendar.SATURDAY, Calendar.SUNDAY);

    @Override
    public void applyFilter(List<Event> input, List<Event> output) {
        Calendar nextWeek = Calendar.getInstance();
        nextWeek.add(Calendar.DAY_OF_MONTH, 7);

        Calendar c = Calendar.getInstance();

        output.clear();

        for (Event e : input) {
            c.setTime(e.date);
            if (!c.after(nextWeek)) output.add(e);
        }
    }
}
