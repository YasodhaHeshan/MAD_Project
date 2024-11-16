package com.example.mad_project.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface BusOwnerDao {
    @Insert
    void insert(BusOwner busOwner);

    @Query("SELECT * FROM bus_owners")
    List<BusOwner> getAllBusOwners();

    @Query("SELECT * FROM bus_owners WHERE id = :id")
    BusOwner getBusOwnerById(int id);

    @Query("SELECT * FROM bus_owners WHERE user_id = :userId")
    BusOwner getBusOwnerByUserId(int userId);

    @Query("SELECT * FROM bus_owners WHERE license_number = :licenseNumber")
    BusOwner getBusOwnerByLicenseNumber(String licenseNumber);

    @Query("SELECT * FROM bus_owners WHERE nic = :nic")
    BusOwner getBusOwnerByNic(String nic);

    @Query("SELECT * FROM bus_owners WHERE company_name = :companyName")
    BusOwner getBusOwnerByCompanyName(String companyName);

    @Query("SELECT * FROM bus_owners WHERE rating >= :rating")
    List<BusOwner> getBusOwnersByRating(double rating);

    @Query("SELECT * FROM bus_owners WHERE years_in_business >= :yearsInBusiness")
    List<BusOwner> getBusOwnersByYearsInBusiness(int yearsInBusiness);

    @Query("SELECT * FROM bus_owners WHERE fleet_size >= :fleetSize")
    List<BusOwner> getBusOwnersByFleetSize(int fleetSize);
}