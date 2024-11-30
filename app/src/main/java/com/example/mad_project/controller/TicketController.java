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

    public void swapSeats(int ticket1Id, int ticket2Id, SwapCallback callback) {
        executorService.execute(() -> {
            try {
                db.runInTransaction(() -> {
                    // Get tickets
                    Ticket ticket1 = db.ticketDao().getTicketById(ticket1Id);
                    Ticket ticket2 = db.ticketDao().getTicketById(ticket2Id);
                    
                    // Validate tickets
                    if (ticket1 == null || ticket2 == null) {
                        throw new IllegalStateException("One or both tickets not found");
                    }
                    if (!ticket1.getStatus().equals("booked") || !ticket2.getStatus().equals("booked")) {
                        throw new IllegalStateException("One or both tickets are not in valid state for swapping");
                    }
                    
                    // Store original seat numbers
                    int seat1 = ticket1.getSeatNumber();
                    int seat2 = ticket2.getSeatNumber();
                    
                    // Update seat numbers
                    ticket1.setSeatNumber(seat2);
                    ticket2.setSeatNumber(seat1);
                    
                    // Update timestamps
                    long currentTime = System.currentTimeMillis();
                    ticket1.setUpdatedAt(currentTime);
                    ticket2.setUpdatedAt(currentTime);
                    
                    // Update both tickets in the database
                    db.ticketDao().update(ticket1);
                    db.ticketDao().update(ticket2);
                    
                    // Log the swap for debugging
                    Log.d("TicketController", String.format(
                        "Swapped seats - Ticket %d: %d → %d, Ticket %d: %d → %d",
                        ticket1.getId(), seat1, seat2,
                        ticket2.getId(), seat2, seat1
                    ));
                });
                
                callback.onSuccess();
            } catch (Exception e) {
                Log.e("TicketController", "Error swapping seats", e);
                callback.onError(e.getMessage());
            }
        });
    }

    public interface SwapCallback {
        void onSuccess();
        void onError(String message);
    }
}