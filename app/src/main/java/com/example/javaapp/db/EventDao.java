package com.example.javaapp.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.javaapp.models.Event;

import java.util.List;

@Dao
public interface EventDao {

    @Query("SELECT * from event ORDER by date, locationName, category")
    List<Event> getAll();

    @Query("SELECT * from event ORDER by category, date, locationName")
    List<Event> getAllOrderedByCategory();

    @Query("SELECT * from event ORDER by locationName, date, category")
    List<Event> getAllOrderedByLocation();

    @Insert
    void insertAll(Event... events);

    @Delete
    void delete(Event event);

    @Query("DELETE from event")
    void deleteAll();
}
