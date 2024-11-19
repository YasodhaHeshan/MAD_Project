package com.example.mad_project.controller;

import android.content.Context;
import com.example.mad_project.data.AppDatabase;
import com.example.mad_project.data.Bus;
import com.example.mad_project.data.BusDao;
import androidx.room.Room;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.ArrayList;
import android.util.Log;

public class BusController {

    private final BusDao busDao;
    private final ExecutorService executorService;

    public BusController(Context context) {
        AppDatabase db = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "mad_project_db").build();
        busDao = db.busDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    public void register(Bus bus) {
        executorService.execute(() -> busDao.insert(bus));
    }

    public void getAllBuses(BusCallback callback) {
        executorService.execute(() -> {
            try {
                List<Bus> busList = busDao.getAllBuses();
                Log.d("BusController", "Retrieved " + busList.size() + " buses from database");
                callback.onBusesLoaded(busList);
            } catch (Exception e) {
                Log.e("BusController", "Error getting buses", e);
                callback.onBusesLoaded(new ArrayList<>()); // Return empty list instead of null
            }
        });
    }

    public void getBusesByRoute(String from, String to, BusCallback callback) {
        executorService.execute(() -> {
            List<Bus> busList = busDao.getAllBuses();
            List<Bus> filteredList = busList.stream()
                .filter(bus -> 
                    (from.isEmpty() || bus.getStartLocation().toLowerCase().contains(from.toLowerCase())) &&
                    (to.isEmpty() || bus.getEndLocation().toLowerCase().contains(to.toLowerCase())))
                .collect(Collectors.toList());
            callback.onBusesLoaded(filteredList);
        });
    }

    public interface BusCallback {
        void onBusesLoaded(List<Bus> busList);
    }
}