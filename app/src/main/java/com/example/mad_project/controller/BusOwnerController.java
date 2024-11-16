package com.example.mad_project.controller;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.room.Room;

import com.example.mad_project.data.AppDatabase;
import com.example.mad_project.data.UserDao;
import com.example.mad_project.data.BusOwner;
import com.example.mad_project.data.BusOwnerDao;
import com.example.mad_project.ui.RegisterOwnerActivity;

public class BusOwnerController {
    private final AppDatabase db;
    private final UserDao userDao;
    private final BusOwnerDao busOwnerDao;

    public BusOwnerController(Context context) {
        db = Room.databaseBuilder(context, AppDatabase.class, "mad_project_db").build();
        busOwnerDao = db.busOwnerDao();
        userDao = db.userDao();
    }

    public void registerBusOwner(String companyName, String licenseNumber, String nic, Context context) {
        if (companyName.isEmpty() || licenseNumber.isEmpty() || nic.isEmpty()) {
            Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show();
        } else {
            new Thread(() -> {
                try {
                    BusOwner newBusOwner = new BusOwner(userDao.getUserId().get(0), companyName, licenseNumber, nic);
                    busOwnerDao.insert(newBusOwner);
                    ((RegisterOwnerActivity) context).runOnUiThread(() -> Toast.makeText(context, "Bus owner registered successfully", Toast.LENGTH_SHORT).show());
                } catch (Exception e) {
                    Log.e("BusOwnerController", "Failed to register bus owner", e);
                    ((RegisterOwnerActivity) context).runOnUiThread(() -> Toast.makeText(context, "Failed to register bus owner: " + e.getMessage(), Toast.LENGTH_LONG).show());
                } finally {
                    db.close();
                }
            }).start();
        }
    }
}