package com.example.mad_project.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.example.mad_project.data.AppDatabase;
import com.example.mad_project.data.Bus;
import com.example.mad_project.data.Payment;
import com.example.mad_project.data.Ticket;
import com.example.mad_project.data.User;
import com.example.mad_project.utils.FareCalculator;
import com.example.mad_project.utils.DialogManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class BookingManager {
    private final AppDatabase db;
    private final Executor executor;
    private final Context context;
    private final Handler mainHandler;

    public BookingManager(Context context) {
        this.context = context;
        this.db = AppDatabase.getDatabase(context);
        this.executor = Executors.newSingleThreadExecutor();
        this.mainHandler = new Handler(Looper.getMainLooper());
    }

    public void processBooking(int userId, Bus selectedBus, List<String> selectedSeats,
                               BookingCallback callback) {
        executor.execute(() -> {
            try {
                User user = db.userDao().getUserById(userId);
                if (user == null) {
                    postToMainThread(() -> callback.onBookingFailure("User not found"));
                    return;
                }

                int totalPoints = selectedSeats.stream()
                    .mapToInt(seat -> FareCalculator.calculatePoints(selectedBus).totalPoints)
                    .sum();

                postToMainThread(() -> {
                    DialogManager.showBookingConfirmation(context, totalPoints, () -> {
                        startBookingTransaction(user, selectedBus, selectedSeats, totalPoints, callback);
                    });
                });
            } catch (Exception e) {
                postToMainThread(() -> callback.onBookingFailure(e.getMessage()));
            }
        });
    }

    private void postToMainThread(Runnable runnable) {
        mainHandler.post(runnable);
    }

    private void startBookingTransaction(User user, Bus bus, List<String> seats,
                                         int totalPoints, BookingCallback callback) {
        executor.execute(() -> {
            try {
                final List<Long> ticketIds = new ArrayList<>();
                final PaymentResult paymentResult = new PaymentResult();
                
                db.runInTransaction(() -> {
                    // Create tickets first
                    ticketIds.addAll(createTickets(user.getId(), bus, seats));
                    
                    // Create payment and store result
                    long pId = createPayment(user.getId(), ticketIds.get(0).intValue(), totalPoints);
                    paymentResult.paymentId = pId;
                    
                    // Update tickets with payment ID
                    updateTicketsWithPayment(ticketIds, (int)pId);
                    
                    // Deduct points
                    deductUserPoints(user.getId(), totalPoints);
                });

                // Send confirmation emails after successful transaction
                for (Long ticketId : ticketIds) {
                    Ticket ticket = db.ticketDao().getTicketById(ticketId.intValue());
                    sendConfirmationEmail(user, ticket, bus);
                }

                callback.onBookingSuccess(totalPoints);
            } catch (Exception e) {
                Log.e("BookingManager", "Booking failed", e);
                callback.onBookingFailure(e.getMessage());
            }
        });
    }

    private List<Long> createTickets(int userId, Bus bus, List<String> seats) {
        List<Long> ticketIds = new ArrayList<>();
        for (String seatNumber : seats) {
            Ticket ticket = new Ticket(userId, bus.getId(), seatNumber,
                bus.getDepartureTime(), bus.getRouteFrom(), bus.getRouteTo(), "booked");
            long ticketId = db.ticketDao().insert(ticket);
            ticketIds.add(ticketId);
        }
        return ticketIds;
    }

    private long createPayment(int userId, int ticketId, int points) {
        Payment payment = new Payment(0, userId, ticketId, points);
        return db.paymentDao().insert(payment);
    }

    private void updateTicketsWithPayment(List<Long> ticketIds, int paymentId) {
        for (Long ticketId : ticketIds) {
            db.ticketDao().updateTicketPaymentId(ticketId.intValue(), paymentId);
        }
    }

    private void deductUserPoints(int userId, int points) {
        db.userDao().deductPoints(userId, points);
    }

    private void sendConfirmationEmail(User user, Ticket ticket, Bus bus) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM d, yyyy", Locale.getDefault());
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        
        String date = dateFormat.format(new Date(ticket.getJourneyDate()));
        String time = timeFormat.format(new Date(bus.getDepartureTime()));

        EmailSender.sendTicketConfirmation(
            user.getEmail(),
            user.getName(),
            ticket.getId(),
            bus.getRegistrationNumber(),
            ticket.getSource(),
            ticket.getDestination(),
            date,
            time,
            ticket.getSeatNumber()
        );
    }

    public interface BookingCallback {
        void onBookingSuccess(int pointsDeducted);
        void onBookingFailure(String error);
    }

    // Helper class to store payment result
    private static class PaymentResult {
        long paymentId;
    }
} 