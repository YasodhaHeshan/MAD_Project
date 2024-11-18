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

    @Query("SELECT * FROM buses")
    List<Bus> getAllBuses();

    @Insert
    void insertAll(Bus[] buses);

    @Query("SELECT * FROM buses WHERE start_location LIKE :from AND end_location LIKE :to")
    List<Bus> getBusesByRoute(String from, String to);
}