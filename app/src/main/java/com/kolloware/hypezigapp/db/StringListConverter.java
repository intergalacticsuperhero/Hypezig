package com.kolloware.hypezigapp.db;

import androidx.room.TypeConverter;

import java.util.ArrayList;
import java.util.List;

public class StringListConverter {

    @TypeConverter
    public String fromArray(List<String> strings) {
        String string = "";
        for(String s : strings) string += (s + ",");

        return string;
    }

    @TypeConverter
    public List<String> toArray(String concatenatedStrings) {
        ArrayList<String> myStrings = new ArrayList<>();

        for (String s : concatenatedStrings.split(",")) {
            myStrings.add(s);
        }

        return myStrings;
    }
}
