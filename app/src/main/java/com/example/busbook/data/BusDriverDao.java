package com.example.busbook.data;

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

    @Query("SELECT * FROM bus_drivers WHERE id = :id")
    BusDriver getBusDriverById(int id);

    @Query("SELECT * FROM bus_drivers WHERE user_id = :userId")
    BusDriver getBusDriverByUserId(int userId);

    @Query("SELECT * FROM bus_drivers WHERE license_number = :licenseNumber")
    BusDriver getBusDriverByLicenseNumber(String licenseNumber);

    @Query("SELECT * FROM bus_drivers WHERE nic = :nic")
    BusDriver getBusDriverByNic(String nic);

    @Query("SELECT * FROM bus_drivers WHERE bus_number = :busNumber")
    BusDriver getBusDriverByBusNumber(String busNumber);

    @Query("SELECT * FROM bus_drivers WHERE is_active = :isActive")
    List<BusDriver> getBusDriversByIsActive(boolean isActive);

    @Query("SELECT * FROM bus_drivers WHERE route_assigned = :routeAssigned")
    List<BusDriver> getBusDriversByRouteAssigned(String routeAssigned);

    @Query("SELECT * FROM bus_drivers WHERE bus_assigned = :busAssigned")
    List<BusDriver> getBusDriversByBusAssigned(String busAssigned);

    @Query("SELECT * FROM bus_drivers WHERE rating >= :rating")
    List<BusDriver> getBusDriversByRating(double rating);

    @Query("SELECT * FROM bus_drivers WHERE experience_years >= :experienceYears")
    List<BusDriver> getBusDriversByExperienceYears(int experienceYears);
}