package com.example.mad_project.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Update;

@Dao
public interface RouteDao {
    @Insert
    void insert(Route route);

    @Update
    void update(Route route);

    @Insert
    void insertAll(Route[] routes);
}