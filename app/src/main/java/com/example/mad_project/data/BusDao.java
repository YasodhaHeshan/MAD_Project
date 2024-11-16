package com.example.mad_project.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface BusDao {
    @Insert
    void insert(Bus bus);

    @Query("SELECT * FROM buses")
    List<Bus> getAllBuses();

    @Query("SELECT * FROM buses WHERE id = :id")
    Bus getBusById(int id);

    @Query("SELECT * FROM buses WHERE bus_number = :busNumber")
    Bus getBusByBusNumber(String busNumber);

    @Query("SELECT * FROM buses WHERE bus_owner_id = :ownerId")
    List<Bus> getBusesByOwnerId(int ownerId);

    @Query("SELECT * FROM buses WHERE bus_driver_id = :driverId")
    List<Bus> getBusesByDriverId(int driverId);

    @Query("SELECT * FROM buses WHERE departure_location = :origin AND arrival_location = :destination")
    List<Bus> getBuses(String origin, String destination);
}