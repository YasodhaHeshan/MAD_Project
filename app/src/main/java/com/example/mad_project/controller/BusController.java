package com.example.mad_project.controller;

import android.content.Context;
import com.example.mad_project.data.AppDatabase;
import com.example.mad_project.data.Bus;
import com.example.mad_project.data.BusDao;
import androidx.room.Room;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.ArrayList;
import java.util.LinkedHashSet;

public class BusController {

    private final BusDao busDao;
    private final ExecutorService executorService;

    public BusController(Context context) {
        AppDatabase db = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "mad_project_db")
                .fallbackToDestructiveMigration()
                .build();
        busDao = db.busDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    public void getAllBuses(Consumer<List<Bus>> callback) {
        executorService.execute(() -> {
            List<Bus> busList = busDao.getAllBuses();
            callback.accept(busList);
        });
    }

    public void getBusesByRoute(String from, String to, Consumer<List<Bus>> callback) {
        executorService.execute(() -> {
            List<Bus> busList = busDao.getBusesByRoute(from, to);
            callback.accept(busList);
        });
    }

    public void createBus(Bus bus, Consumer<Boolean> callback) {
        executorService.execute(() -> {
            try {
                busDao.insert(bus);
                callback.accept(true);
            } catch (Exception e) {
                callback.accept(false);
            }
        });
    }

    public void getRouteSuggestions(String query, Consumer<List<String>> callback) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            List<String> suggestions = new ArrayList<>();
            
            // Get unique 'from' locations
            List<String> fromLocations = busDao.getFromLocations("%" + query + "%");
            // Get unique 'to' locations
            List<String> toLocations = busDao.getToLocations("%" + query + "%");
            
            // Combine and remove duplicates
            suggestions.addAll(fromLocations);
            suggestions.addAll(toLocations);
            suggestions = new ArrayList<>(new LinkedHashSet<>(suggestions));
            
            callback.accept(suggestions);
        });
        executor.shutdown();
    }
}