package com.example.javaapp.models;

import androidx.annotation.NonNull;

import java.util.HashMap;

public class Location {

    String name, url;

    public Location(String name, String url) {
        this.name = name;
        this.url = url;
    }

    @NonNull
    @Override
    public String toString() {
        return name + ": " + url;
    }
}
