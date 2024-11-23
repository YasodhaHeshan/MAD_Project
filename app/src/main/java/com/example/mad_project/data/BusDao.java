package com.example.mad_project.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface BusDao {
    @Insert
    void insert(Bus bus);

    @Update
    void update(Bus bus);

    @Query("SELECT * FROM buses WHERE is_active = 1")
    List<Bus> getAllBuses();

    @Query("SELECT * FROM buses WHERE route_from LIKE :from AND route_to LIKE :to AND is_active = 1")
    List<Bus> getBusesByRoute(String from, String to);

    @Query("SELECT DISTINCT route_from FROM buses WHERE route_from LIKE :query")
    List<String> getFromLocations(String query);

    @Query("SELECT DISTINCT route_to FROM buses WHERE route_to LIKE :query")
    List<String> getToLocations(String query);

    @Query("SELECT * FROM buses WHERE id = :busId")
    Bus getBusById(int busId);
}