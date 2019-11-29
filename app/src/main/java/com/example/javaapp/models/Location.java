package com.example.javaapp.models;

import android.util.Log;

import androidx.annotation.NonNull;

import java.util.HashMap;

import static com.example.javaapp.BaseApplication.LOG_APP;

public class Location {

    String name, url;

    public Location(String name, String url) {
        Log.d(LOG_APP, getClass().getName() + " constructed");

        this.name = name;
        this.url = url;
    }

    @NonNull
    @Override
    public String toString() {
        return name + ": " + url;
    }
}
