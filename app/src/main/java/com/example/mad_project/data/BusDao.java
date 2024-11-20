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
}