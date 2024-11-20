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

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;


public class BusDriverController {
    private final AppDatabase db;
    private final BusDriverDao busDriverDao;
    private final ExecutorService executorService;

    public BusDriverController(Context context) {
        db = Room.databaseBuilder(context.getApplicationContext(), 
            AppDatabase.class, "mad_project_db").build();
        busDriverDao = db.busDriverDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    public void registerBusDriver(int userId, String licenseNumber, long licenseExpiry, 
                                int experience, Consumer<Boolean> callback) {
        executorService.execute(() -> {
            try {
                if (busDriverDao.isLicenseExists(licenseNumber)) {
                    callback.accept(false);
                    return;
                }

                BusDriver driver = new BusDriver(userId, licenseNumber, licenseExpiry, experience);
                busDriverDao.insert(driver);
                callback.accept(true);
            } catch (Exception e) {
                Log.e("BusDriverController", "Error registering driver", e);
                callback.accept(false);
            }
        });
    }

    public void getAllDrivers(Consumer<List<BusDriver>> callback) {
        executorService.execute(() -> {
            try {
                List<BusDriver> drivers = busDriverDao.getAllActiveDrivers();
                callback.accept(drivers);
            } catch (Exception e) {
                Log.e("BusDriverController", "Error getting drivers", e);
                callback.accept(Collections.emptyList());
            }
        });
    }

    public void updateDriver(BusDriver driver, Consumer<Boolean> callback) {
        executorService.execute(() -> {
            try {
                driver.setUpdatedAt(System.currentTimeMillis());
                busDriverDao.update(driver);
                callback.accept(true);
            } catch (Exception e) {
                Log.e("BusDriverController", "Error updating driver", e);
                callback.accept(false);
            }
        });
    }

    public void deactivateDriver(int driverId, Consumer<Boolean> callback) {
        executorService.execute(() -> {
            try {
                busDriverDao.deactivateDriver(driverId);
                callback.accept(true);
            } catch (Exception e) {
                Log.e("BusDriverController", "Error deactivating driver", e);
                callback.accept(false);
            }
        });
    }
}
