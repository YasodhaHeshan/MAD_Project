package com.example.busbook.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ScheduleDao {

    @Insert
    void insert(Schedule schedule);

    @Update
    void update(Schedule schedule);

    @Delete
    void delete(Schedule schedule);

    @Query("SELECT * FROM Schedule WHERE id = :id")
    Schedule getScheduleById(int id);

    @Query("SELECT * FROM Schedule")
    List<Schedule> getAllSchedules();
}