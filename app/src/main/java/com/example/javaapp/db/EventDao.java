package com.example.javaapp.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.javaapp.models.Event;

import java.util.Date;
import java.util.List;

@Dao
public interface EventDao {

    @Query("SELECT * from event WHERE eventId = :eventId")
    Event getByEventId(int eventId);

    @Query("SELECT * from event WHERE providerName = :providerName AND providerId = :providerId")
    Event getByProviderNameAndId(String providerName, String providerId);

    @Query("SELECT * from event WHERE date >= :date ORDER by date, locationName, category")
    List<Event> getCurrentEvents(long date);

    @Query("SELECT * from event WHERE date >= :date ORDER by category, date, locationName")
    List<Event> getCurrentEventsOrderedByCategory(long date);

    @Query("SELECT * from event WHERE date >= :date ORDER by locationName, date, category")
    List<Event> getCurrentEventsOrderedByLocation(long date);

    @Query("SELECT * from event WHERE date >= :date ORDER by title, date, locationName")
    List<Event> getCurrentEventsOrderedByTitle(long date);

    @Query("SELECT * FROM event WHERE favorite = 1 AND date >= :date ORDER by date")
    List<Event> getFavorites(long date);

    @Query("SELECT DISTINCT category FROM event")
    List<String> getDistinctCategories();

    @Update
    void update(Event event);

    @Insert
    void insertAll(Event... events);

    @Delete
    void delete(Event event);

    @Query("DELETE from event")
    void deleteAll();
}
