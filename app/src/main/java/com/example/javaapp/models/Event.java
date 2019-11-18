package com.example.javaapp.models;

import androidx.annotation.NonNull;

import java.util.Date;
import java.util.List;

public class Event {
    String title, subtitle, details;
    Date date;
    Location location;
    List<String> tags;
    String imageURL;


    public Event(String title, String subtitle, String details, Date date, Location location, List<String> tags, String imageURL) {
        this.title = title;
        this.subtitle = subtitle;
        this.details = details;
        this.date = date;
        this.location = location;
        this.tags = tags;
        this.imageURL = imageURL;
    }

    @NonNull
    @Override
    public String toString() {
        return tags + " " + title + " (" + subtitle + ") @ " + location + " (" + date + ") /" + imageURL+ "/ ... " + details;
    }
}