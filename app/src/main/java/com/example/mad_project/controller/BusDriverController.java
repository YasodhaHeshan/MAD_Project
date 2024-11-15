package com.example.mad_project.controller;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.room.Room;

import com.example.mad_project.data.AppDatabase;
import com.example.mad_project.data.UserDao;
import com.example.mad_project.data.BusDriver;
import com.example.mad_project.data.BusDriverDao;
import com.example.mad_project.ui.RegisterBusDriverActivity;


public class BusDriverController {
    private final AppDatabase db;
    private final BusDriverDao busDriverDao;
    private final UserDao userDao;

    public BusDriverController(Context context) {
        db = Room.databaseBuilder(context, AppDatabase.class, "mad_project_db").build();
        busDriverDao = db.busDriverDao();
        userDao = db.userDao();
    }

    public void registerBusDriver(String licenseNumber, String nic, Context context) {
        if (licenseNumber.isEmpty() || nic.isEmpty()) {
            Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show();
        } else {
            new Thread(() -> {
                try {
                    BusDriver newBusDriver = new BusDriver(userDao.getUserId().get(0), licenseNumber, nic);
                    busDriverDao.insert(newBusDriver);
                    ((RegisterBusDriverActivity) context).runOnUiThread(() -> Toast.makeText(context, "Bus driver registered successfully", Toast.LENGTH_SHORT).show());
                } catch (Exception e) {
                    Log.e("BusDriverController", "Failed to register bus driver", e);
                    ((RegisterBusDriverActivity) context).runOnUiThread(() -> Toast.makeText(context, "Failed to register bus driver: " + e.getMessage(), Toast.LENGTH_LONG).show());
                } finally {
                    db.close();
                }
            }).start();
        }
    }
}
