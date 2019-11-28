package com.example.javaapp.models.filters;

import com.example.javaapp.models.Event;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjuster;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static java.time.temporal.TemporalAdjusters.next;
import static java.util.Calendar.MONDAY;
import static java.util.Calendar.SUNDAY;

public class WeekendFilter implements FilterStrategy {

    private final List<Integer> WEEKEND_DAYS = Arrays.asList(Calendar.FRIDAY, Calendar.SATURDAY, SUNDAY);

    @Override
    public void applyFilter(List<Event> input, List<Event> output) {
        Calendar forCalendar = Calendar.getInstance();
        Calendar nextMonday = nextMonday();

        for (Event e : input) {
            forCalendar.setTime(e.date);

            if (forCalendar.before(nextMonday) && WEEKEND_DAYS.contains(forCalendar.get(Calendar.DAY_OF_WEEK))) {
                output.add(e);
            }
        }
    }

    private Calendar nextMonday() {
        Calendar c = Calendar.getInstance();

        do {
            c.add(Calendar.DAY_OF_WEEK, 1);
        } while (c.get(Calendar.DAY_OF_WEEK) != MONDAY);

        return c;
    }
}
