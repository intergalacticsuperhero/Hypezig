package com.kolloware.hypezigapp.models;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.kolloware.hypezigapp.db.DateConverter;
import com.kolloware.hypezigapp.db.StringListConverter;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.kolloware.hypezigapp.BaseApplication.LOG_APP;

@Entity
@TypeConverters({DateConverter.class, StringListConverter.class})
public class Event {

    @PrimaryKey(autoGenerate = true) public int eventId;

    public String title;
    public String subtitle;
    public String details;
    public Date date;
    public String locationName;
    public String locationURL;
    public List<String> tags;
    public String imageURL;
    public String category;
    public String eventURL;

    public boolean favorite;

    public String providerName;
    public String providerId;
    public String providerCategory;

    public Event(String title, String subtitle, String details, Date date, String locationName,
                 List<String> tags, String imageURL, String category,
                 String providerName, String providerId, String providerCategory) {
        Log.v(LOG_APP, getClass().getName() + " constructed");

        this.title = title;
        this.subtitle = subtitle;
        this.details = details;
        this.date = date;
        this.locationName = locationName;
        this.tags = tags;
        this.imageURL = imageURL;
        this.category = category;
        this.favorite = false;
        this.providerName = providerName;
        this.providerId = providerId;
        this.providerCategory = providerCategory;
    }

    @NonNull
    @Override
    public String toString() {
        Log.v(LOG_APP, getClass().getName() + ".toString() called");

        Map<String, String> localValues = new HashMap<>();

        localValues.put("title", title);
        localValues.put("locationURL", locationURL);
        localValues.put("eventURL", eventURL);
        localValues.put("subtitle", subtitle);
        localValues.put("details", details);
        localValues.put("date", date.toString());
        localValues.put("locationName", locationName);
        localValues.put("tags", tags.toString());
        localValues.put("imageURL", imageURL);
        localValues.put("category", category);
        localValues.put("providerName", providerName);
        localValues.put("providerId", providerId);
        localValues.put("providerCategory", providerCategory);

        return localValues.toString();
    }
}