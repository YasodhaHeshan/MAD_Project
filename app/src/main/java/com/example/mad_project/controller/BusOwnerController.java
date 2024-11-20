package com.example.mad_project.controller;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.room.Room;

import com.example.mad_project.data.AppDatabase;
import com.example.mad_project.data.User;
import com.example.mad_project.data.UserDao;
import com.example.mad_project.data.BusOwner;
import com.example.mad_project.data.BusOwnerDao;
import com.example.mad_project.ui.RegisterOwnerActivity;

import java.util.List;
import java.util.function.Consumer;
import java.util.Collections;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BusOwnerController {
    private final AppDatabase db;
    private final BusOwnerDao busOwnerDao;
    private final ExecutorService executorService;

    public BusOwnerController(Context context) {
        db = Room.databaseBuilder(context.getApplicationContext(), 
            AppDatabase.class, "mad_project_db").build();
        busOwnerDao = db.busOwnerDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    public void registerBusOwner(int userId, String companyName, String regNumber, 
                                String taxId, Consumer<Boolean> callback) {
        executorService.execute(() -> {
            try {
                if (busOwnerDao.isRegistrationExists(regNumber)) {
                    callback.accept(false);
                    return;
                }

                BusOwner busOwner = new BusOwner(userId, companyName, regNumber, taxId);
                busOwnerDao.insert(busOwner);
                callback.accept(true);
            } catch (Exception e) {
                Log.e("BusOwnerController", "Error registering bus owner", e);
                callback.accept(false);
            }
        });
    }

    public void getAllBusOwners(Consumer<List<BusOwner>> callback) {
        executorService.execute(() -> {
            try {
                List<BusOwner> owners = busOwnerDao.getAllBusOwners();
                callback.accept(owners);
            } catch (Exception e) {
                Log.e("BusOwnerController", "Error getting bus owners", e);
                callback.accept(Collections.emptyList());
            }
        });
    }

    public void updateBusOwner(BusOwner owner, Consumer<Boolean> callback) {
        executorService.execute(() -> {
            try {
                owner.setUpdatedAt(System.currentTimeMillis());
                busOwnerDao.update(owner);
                callback.accept(true);
            } catch (Exception e) {
                Log.e("BusOwnerController", "Error updating bus owner", e);
                callback.accept(false);
            }
        });
    }

    public void deactivateBusOwner(int ownerId, Consumer<Boolean> callback) {
        executorService.execute(() -> {
            try {
                busOwnerDao.deactivateOwner(ownerId);
                callback.accept(true);
            } catch (Exception e) {
                Log.e("BusOwnerController", "Error deactivating bus owner", e);
                callback.accept(false);
            }
        });
    }
}