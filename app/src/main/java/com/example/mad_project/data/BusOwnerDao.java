package com.example.mad_project.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface BusOwnerDao {
    @Insert
    void insert(BusOwner busOwner);

    @Update
    void update(BusOwner busOwner);

    @Insert
    void insertAll(BusOwner[] owners);
}