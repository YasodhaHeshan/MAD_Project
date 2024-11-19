package com.example.mad_project.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Update;

@Dao
public interface BusDriverDao {
    @Insert
    void insert(BusDriver busDriver);

    @Update
    void update(BusDriver busDriver);

    @Insert
    void insertAll(BusDriver[] drivers);
}