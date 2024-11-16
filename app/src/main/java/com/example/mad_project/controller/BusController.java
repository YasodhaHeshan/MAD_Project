package com.example.mad_project.controller;

import android.content.Context;
import com.example.mad_project.data.AppDatabase;
import com.example.mad_project.data.Bus;
import com.example.mad_project.data.BusDao;
import androidx.room.Room;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
            List<Bus> busList = busDao.getAllBuses();
            callback.onBusesLoaded(busList);
        });
    }

    public interface BusCallback {
        void onBusesLoaded(List<Bus> busList);
    }
}