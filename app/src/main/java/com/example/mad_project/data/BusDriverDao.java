package com.example.mad_project.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface BusDriverDao {
    @Insert
    void insert(BusDriver busDriver);

    @Query("SELECT * FROM bus_drivers")
    List<BusDriver> getAllBusDrivers();
}