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
}