package com.example.javaapp.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.javaapp.models.Event;

import java.util.List;

@Dao
public interface EventDao {

    @Query("SELECT * from event")
    List<Event> getAll();

    @Insert
    void insertAll(Event... events);

    @Delete
    void delete(Event event);
}
