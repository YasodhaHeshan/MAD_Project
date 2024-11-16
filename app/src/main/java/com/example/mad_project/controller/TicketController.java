package com.example.mad_project.controller;

import android.content.Context;
import com.example.mad_project.data.AppDatabase;
import com.example.mad_project.data.TicketDao;
import androidx.room.Room;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TicketController {

    private final TicketDao ticketDao;
    private final ExecutorService executorService;

    public TicketController(Context context) {
        AppDatabase db = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "mad_project_db").build();
        ticketDao = db.ticketDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    public void getTicketPriceByBusId(int busId, TicketPriceCallback callback) {
        executorService.execute(() -> {
            double ticketPrice = ticketDao.getTicketPriceByBusId(busId);
            callback.onTicketPriceLoaded(ticketPrice);
        });
    }

    public interface TicketPriceCallback {
        void onTicketPriceLoaded(double ticketPrice);
    }
}