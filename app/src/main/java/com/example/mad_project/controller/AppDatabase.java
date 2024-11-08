package com.example.mad_project.controller;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.mad_project.data.BusDriver;
import com.example.mad_project.data.BusDriverDao;
import com.example.mad_project.data.BusOwner;
import com.example.mad_project.data.BusOwnerDao;
import com.example.mad_project.data.Customer;
import com.example.mad_project.data.CustomerDao;
import com.example.mad_project.data.User;
import com.example.mad_project.data.UserDao;

@Database(entities = {User.class, Customer.class, BusOwner.class, BusDriver.class}, version = 1, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();
    public abstract CustomerDao customerDao();
    public abstract BusOwnerDao busOwnerDao();
    public abstract BusDriverDao busDriverDao();
}