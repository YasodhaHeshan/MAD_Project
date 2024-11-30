package com.example.mad_project.utils;

import android.content.Context;
import android.util.Log;

import com.example.mad_project.controller.TicketController;
import com.example.mad_project.data.*;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class NotificationHandler {
    private final Context context;
    private final AppDatabase db;
    private final Executor executor;
    private final SessionManager sessionManager;

    public NotificationHandler(Context context) {
        this.context = context;
        this.db = AppDatabase.getDatabase(context);
        this.executor = Executors.newSingleThreadExecutor();
        this.sessionManager = new SessionManager(context);
    }

    public void handleSeatSwapRequest(User requestingUser, Ticket currentTicket, Ticket targetTicket) {
        executor.execute(() -> {
            try {
                // Create notification for target user
                Notification targetNotification = new Notification(
                    targetTicket.getUserId(),
                    NotificationType.SEAT_SWAP,
                    "Seat Swap Request",
                    String.format("%s wants to swap their seat %s with your seat %s",
                        requestingUser.getName(),
                        currentTicket.getSeatNumber(),
                        targetTicket.getSeatNumber())
                );
                targetNotification.setAdditionalData(String.format("%d:%d", currentTicket.getId(), targetTicket.getId()));
                targetNotification.setStatus("PENDING");
                db.notificationDao().insert(targetNotification);

                // Create notification for requester
                Notification requesterNotification = new Notification(
                    requestingUser.getId(),
                    NotificationType.SEAT_SWAP,
                    "Seat Swap Request Sent",
                    String.format("You requested to swap your seat %s with seat %s",
                        currentTicket.getSeatNumber(),
                        targetTicket.getSeatNumber())
                );
                requesterNotification.setAdditionalData(String.format("%d:%d", currentTicket.getId(), targetTicket.getId()));
                requesterNotification.setStatus("PENDING");
                db.notificationDao().insert(requesterNotification);

                Log.d("NotificationHandler", "Created seat swap notifications successfully");
            } catch (Exception e) {
                Log.e("NotificationHandler", "Error creating seat swap request", e);
            }
        });
    }

    public void handleSeatSwapResponse(Notification notification, boolean accepted, TicketController.SwapCallback callback) {
        executor.execute(() -> {
            try {
                String[] ticketIds = notification.getAdditionalData().split(":");
                int ticket1Id = Integer.parseInt(ticketIds[0]);
                int ticket2Id = Integer.parseInt(ticketIds[1]);

                // Update notification status
                notification.setStatus(accepted ? "ACCEPTED" : "DECLINED");
                db.notificationDao().update(notification);

                // Create response notification for requester
                Ticket ticket1 = db.ticketDao().getTicketById(ticket1Id);
                if (ticket1 != null) {
                    Notification responseNotification = new Notification(
                        ticket1.getUserId(),
                        "SEAT_SWAP",
                        accepted ? "Seat Swap Accepted" : "Seat Swap Declined",
                        String.format("Your seat swap request has been %s", 
                            accepted ? "accepted" : "declined")
                    );
                    responseNotification.setAdditionalData(notification.getAdditionalData());
                    responseNotification.setStatus("UNREAD");
                    db.notificationDao().insert(responseNotification);
                }

                if (accepted) {
                    // Perform the seat swap
                    TicketController ticketController = new TicketController(context);
                    ticketController.swapSeats(ticket1Id, ticket2Id, callback);
                }
            } catch (Exception e) {
                Log.e("NotificationHandler", "Error handling seat swap response", e);
                if (callback != null) {
                    callback.onError(e.getMessage());
                }
            }
        });
    }

    public void handleBusAssignment(Notification notification, boolean accepted) {
        db.runInTransaction(() -> {
            Bus bus = db.busDao().getBusByRegistration(notification.getAdditionalData());
            if (bus != null) {
                handleBusDriverResponse(bus, notification, accepted);
                notifyBusOwner(bus, notification, accepted);
            }
        });
    }

    private void handleBusDriverResponse(Bus bus, Notification notification, boolean accepted) {
        if (accepted) {
            BusDriver driver = db.busDriverDao().getBusDriverByUserId(notification.getUserId());
            if (driver != null) {
                bus.setDriverId(driver.getId());
                bus.setActive(true);
            }
        } else {
            bus.setActive(false);
        }
        bus.setUpdatedAt(System.currentTimeMillis());
        db.busDao().update(bus);
    }

    private void notifyBusOwner(Bus bus, Notification driverNotification, boolean accepted) {
        BusOwner owner = db.busOwnerDao().getBusOwnerById(bus.getOwnerId());
        if (owner != null) {
            Notification ownerNotification = new Notification(
                owner.getUserId(),
                "BUS_ASSIGNMENT_RESPONSE",
                accepted ? "Driver Accepted" : "Driver Declined",
                String.format("Driver has %s the bus assignment for %s",
                    accepted ? "accepted" : "declined",
                    bus.getRegistrationNumber())
            );
            ownerNotification.setStatus("UNREAD");
            db.notificationDao().insert(ownerNotification);
        }
    }

    public void handleBusAssignmentCancellation(Notification notification) {
        Bus bus = db.busDao().getBusByRegistration(notification.getAdditionalData());
        if (bus != null) {
            bus.setActive(false);
            bus.setUpdatedAt(System.currentTimeMillis());
            db.busDao().update(bus);
        }
        notification.setStatus("CANCELLED");
        db.notificationDao().update(notification);
    }

    public void handleBusDriverReassignment(Notification notification, BusDriver newDriver) {
        Bus bus = db.busDao().getBusByRegistration(notification.getAdditionalData());
        if (bus != null) {
            bus.setDriverId(newDriver.getId());
            bus.setUpdatedAt(System.currentTimeMillis());
            db.busDao().update(bus);
            
            createBusAssignmentNotification(newDriver, bus, 
                db.busOwnerDao().getBusOwnerById(bus.getOwnerId()));
        }
    }

    public void createBusAssignmentNotification(BusDriver driver, Bus bus, BusOwner owner) {
        Notification notification = new Notification(
            driver.getUserId(),
            "BUS_ASSIGNMENT",
            "Bus Assignment Request",
            String.format("Owner %s wants to assign you to bus %s on route %s to %s", 
                owner.getCompanyName(),
                bus.getRegistrationNumber(),
                bus.getRouteFrom(),
                bus.getRouteTo())
        );
        notification.setAdditionalData(bus.getRegistrationNumber());
        notification.setStatus("PENDING");
        db.notificationDao().insert(notification);
    }
}