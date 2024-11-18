package com.example.mad_project.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Update;

@Dao
public interface PaymentDao {
    @Insert
    void insert(Payment payment);

    @Update
    void update(Payment payment);

    @Insert
    void insertAll(Payment[] payments);
}