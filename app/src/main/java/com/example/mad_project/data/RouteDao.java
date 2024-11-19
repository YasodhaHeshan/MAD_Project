package com.example.mad_project.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface RouteDao {
    @Insert
    void insert(Route route);

    @Query("SELECT * FROM routes")
    List<Route> getAllRoutes();

    @Query("SELECT * FROM routes WHERE id = :id")
    Route getRouteById(int id);

    @Query("SELECT * FROM routes WHERE start_location = :startLocation")
    List<Route> getRoutesByStartLocation(String startLocation);

    @Query("SELECT * FROM routes WHERE end_location = :endLocation")
    List<Route> getRoutesByEndLocation(String endLocation);

    @Query("SELECT * FROM routes WHERE start_location = :startLocation AND end_location = :endLocation")
    List<Route> getRoutesByStartAndEndLocation(String startLocation, String endLocation);

    @Query("SELECT * FROM routes WHERE departure_time = :departureTime")
    List<Route> getRoutesByDepartureTime(String departureTime);

    @Query("SELECT * FROM routes WHERE arrival_time = :arrivalTime")
    List<Route> getRoutesByArrivalTime(String arrivalTime);
}