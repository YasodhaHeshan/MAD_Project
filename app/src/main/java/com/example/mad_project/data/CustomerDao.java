package com.example.mad_project.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface CustomerDao {
    @Insert
    void insert(Customer customer);

    @Query("SELECT * FROM customers")
    List<Customer> getAllCustomers();
}