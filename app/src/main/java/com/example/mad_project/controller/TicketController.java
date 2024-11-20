package com.example.mad_project.controller;

import android.content.Context;
import android.util.Log;
import com.example.mad_project.data.AppDatabase;
import com.example.mad_project.data.Ticket;
import com.example.mad_project.data.TicketDao;
import androidx.room.Room;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class TicketController {

    private final AppDatabase db;
    private final TicketDao ticketDao;
    private final ExecutorService executorService;

    public TicketController(Context context) {
        db = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "mad_project_db").build();
        ticketDao = db.ticketDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    public void createTicket(Ticket ticket, Consumer<Boolean> callback) {
        executorService.execute(() -> {
            try {
                ticketDao.insert(ticket);
                callback.accept(true);
            } catch (Exception e) {
                Log.e("TicketController", "Error creating ticket", e);
                callback.accept(false);
            }
        });
    }

    public void getAllTickets(Consumer<List<Ticket>> callback) {
        executorService.execute(() -> {
            try {
                List<Ticket> tickets = ticketDao.getAllActiveTickets();
                callback.accept(tickets);
            } catch (Exception e) {
                Log.e("TicketController", "Error getting tickets", e);
                callback.accept(Collections.emptyList());
            }
        });
    }

    public void getUpcomingTickets(Consumer<List<Ticket>> callback) {
        executorService.execute(() -> {
            try {
                long currentTime = System.currentTimeMillis();
                List<Ticket> tickets = ticketDao.getUpcomingTickets(currentTime);
                callback.accept(tickets);
            } catch (Exception e) {
                Log.e("TicketController", "Error getting upcoming tickets", e);
                callback.accept(Collections.emptyList());
            }
        });
    }

    public void updateTicketStatus(int ticketId, String status, Consumer<Boolean> callback) {
        executorService.execute(() -> {
            try {
                ticketDao.updateTicketStatus(ticketId, status, System.currentTimeMillis());
                callback.accept(true);
            } catch (Exception e) {
                Log.e("TicketController", "Error updating ticket status", e);
                callback.accept(false);
            }
        });
    }
}