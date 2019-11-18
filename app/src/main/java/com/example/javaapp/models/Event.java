package com.example.javaapp.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import com.example.javaapp.db.DateConverter;
import com.example.javaapp.db.StringListConverter;

import java.util.Date;
import java.util.List;

@Entity
@TypeConverters({DateConverter.class, StringListConverter.class})
public class Event {

    @PrimaryKey(autoGenerate = true) public int eventId;

    public String title;
    public String subtitle;
    public String details;
    public Date date;
    public String locationName;
    public List<String> tags;
    public String imageURL;

    public Event(String title, String subtitle, String details, Date date, String locationName, List<String> tags, String imageURL) {
        this.title = title;
        this.subtitle = subtitle;
        this.details = details;
        this.date = date;
        this.locationName = locationName;
        this.tags = tags;
        this.imageURL = imageURL;
    }

    @NonNull
    @Override
    public String toString() {
        return tags + " " + title + " (" + subtitle + ") @ " + locationName + " (" + date + ") /" + imageURL+ "/ ... " + details;
    }
}