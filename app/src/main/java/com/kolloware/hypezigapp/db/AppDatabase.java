package com.kolloware.hypezigapp.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.kolloware.hypezigapp.models.Event;

@Database(entities = {Event.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public static final String DATABASE_NAME = "hypezig-events";

    private static AppDatabase instance;

    public abstract EventDao eventDao();

    public static AppDatabase getInstance(Context context) {
        if (instance == null) instance = Room.databaseBuilder(context, AppDatabase.class,
                DATABASE_NAME).build();
        return instance;
    }
}
