package com.example.mad_project.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface BusDao {
    @Insert
    void insert(Bus bus);

    @Query("SELECT * FROM buses WHERE departure_location = :origin AND arrival_location = :destination")
    List<Bus> getBuses(String origin, String destination);
}