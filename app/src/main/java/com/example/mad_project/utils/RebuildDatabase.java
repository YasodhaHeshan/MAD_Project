package com.example.mad_project.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.room.Room;

import com.example.mad_project.data.AppDatabase;
import com.example.mad_project.data.User;
import com.example.mad_project.data.BusOwner;
import com.example.mad_project.data.BusDriver;
import com.example.mad_project.data.Bus;
import com.example.mad_project.data.Ticket;
import com.example.mad_project.data.Payment;
import com.example.mad_project.data.Notification;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class RebuildDatabase {
    private static final String TAG = "RebuildDatabase";
    private static final Executor executor = Executors.newSingleThreadExecutor();
    private static Handler mainHandler = null;
    private static final String DB_NAME = "mad_project_db";

    private static Handler getMainHandler() {
        if (mainHandler == null) {
            // Ensure we're on the main thread when creating the handler
            if (Looper.myLooper() == Looper.getMainLooper()) {
                mainHandler = new Handler(Looper.getMainLooper());
            } else {
                // Post handler creation to main thread
                try {
                    Looper.prepare();
                    mainHandler = new Handler(Looper.getMainLooper());
                    Looper.loop();
                } catch (Exception e) {
                    Log.e(TAG, "Error creating handler", e);
                }
            }
        }
        return mainHandler;
    }

    public interface RebuildCallback {
        void onSuccess(String message);
        void onError(String error);
    }

    public static void clearAndRebuildDatabase(Context context, boolean async, RebuildCallback callback) {
        Runnable rebuildTask = () -> {
            try {
                // Close existing database connection
                AppDatabase.closeDatabase();

                // Delete database file
                context.deleteDatabase(DB_NAME);

                // Create new database instance
                AppDatabase db = Room.databaseBuilder(context, AppDatabase.class, DB_NAME)
                    .fallbackToDestructiveMigration()
                    .build();

                // Insert sample data
                insertSampleData(db);

                // Post callback to main thread
                if (callback != null) {
                    new Handler(Looper.getMainLooper()).post(() -> 
                        callback.onSuccess("Database rebuilt successfully"));
                }

            } catch (Exception e) {
                Log.e(TAG, "Error rebuilding database", e);
                if (callback != null) {
                    new Handler(Looper.getMainLooper()).post(() -> 
                        callback.onError(e.getMessage()));
                }
            }
        };

        if (async) {
            executor.execute(rebuildTask);
        } else {
            rebuildTask.run();
        }
    }

    private static void insertSampleData(AppDatabase db) {
        db.runInTransaction(() -> {
            // 1. Create Users with different roles and initial points
            User passenger1 = new User("Adithya Ekanayaka", "adithyasean@gmail.com", "+94702967909",
                Validation.hashPassword("aaaaaa"), "user");
            passenger1.setPoints(50000);

            User passenger2 = new User("Adithya Ekanayaka", "adithyasean@outlook.com", "+94767007909",
                Validation.hashPassword("aaaaaa"), "user");
            passenger2.setPoints(30000);

            User passenger3 = new User("Yasodha Heshan", "yasodhaheshan2002@gmail.com", "+94111111111",
                Validation.hashPassword("aaaaaa"), "user");
            passenger3.setPoints(45000);

            User ownerUser1 = new User("Senith Nimsara", "senith2002n@gmail.com", "+94761234567",
                Validation.hashPassword("aaaaaa"), "owner");
            ownerUser1.setPoints(10000);

            User ownerUser2 = new User("Jalith Chamikara", "premjalith.@gmail.com", "+94762345678",
                Validation.hashPassword("aaaaaa"), "owner");
            ownerUser2.setPoints(15000);

            User driverUser1 = new User("Dave Brown", "ai@gmail.com", "+94751234567",
                Validation.hashPassword("aaaaaa"), "driver");
            driverUser1.setPoints(10000);

            User driverUser2 = new User("John Connor", "a@a.a", "+94752345678",
                Validation.hashPassword("aaaaaa"), "driver");
            driverUser2.setPoints(12000);

            // Insert all users
            long p1Id = db.userDao().insert(passenger1);
            long p2Id = db.userDao().insert(passenger2);
            long p3Id = db.userDao().insert(passenger3);
            long owner1Id = db.userDao().insert(ownerUser1);
            long owner2Id = db.userDao().insert(ownerUser2);
            long driver1Id = db.userDao().insert(driverUser1);
            long driver2Id = db.userDao().insert(driverUser2);

            // 2. Create Bus Owners
            BusOwner busOwner1 = new BusOwner((int)owner1Id, "Wilson Transport", "REG123456", "TAX789012");
            BusOwner busOwner2 = new BusOwner((int)owner2Id, "Connor Express", "REG789012", "TAX345678");
            
            long busOwner1Id = db.busOwnerDao().insert(busOwner1);
            long busOwner2Id = db.busOwnerDao().insert(busOwner2);

            // 3. Create Bus Drivers with different experience levels
            BusDriver driver1 = new BusDriver((int)driver1Id, "DL123456", 
                System.currentTimeMillis() + 31536000000L, 5);
            BusDriver driver2 = new BusDriver((int)driver2Id, "DL789012", 
                System.currentTimeMillis() + 63072000000L, 8);
            
            db.busDriverDao().insert(driver1);
            db.busDriverDao().insert(driver2);

            // 4. Create Buses with various routes and amenities
            // Owner 1's buses
            Bus bus1 = new Bus((int)busOwner1Id, "NB-1234", "Volvo 9400", 40, 
                "WiFi, AC, USB Charging", true, "Colombo", "Kandy",
                6.933631, 79.855221,
                System.currentTimeMillis() + 3600000,
                System.currentTimeMillis() + 18000000,
                2500);

            Bus bus2 = new Bus((int)busOwner1Id, "NB-5678", "Volvo 9400", 40,
                "WiFi, AC, USB Charging, Entertainment", true, "Colombo", "Galle",
                6.933631, 79.855221,
                System.currentTimeMillis() + 7200000,
                System.currentTimeMillis() + 14400000,
                2000);

            // Owner 2's buses
            Bus bus3 = new Bus((int)busOwner2Id, "NB-9012", "Mercedes-Benz O303", 45,
                "WiFi, AC, USB Charging, Refreshments", true, "Colombo", "Jaffna",
                6.933631, 79.855221,
                System.currentTimeMillis() + 10800000,
                System.currentTimeMillis() + 36000000,
                3500);

            long bus1Id = db.busDao().insert(bus1);
            long bus2Id = db.busDao().insert(bus2);
            long bus3Id = db.busDao().insert(bus3);

            // 5. Create Tickets and Payments for various scenarios
            // Completed bookings for Passenger 1
            createTicketAndPayment(db, (int)p1Id, (int)bus1Id, "A1", 2625, "booked");
            createTicketAndPayment(db, (int)p1Id, (int)bus2Id, "B3", 2100, "booked");

            // Completed bookings for Passenger 2
            createTicketAndPayment(db, (int)p2Id, (int)bus2Id, "C2", 2100, "booked");
            createTicketAndPayment(db, (int)p2Id, (int)bus3Id, "D4", 3675, "cancelled");

            // Completed bookings for Passenger 3
            createTicketAndPayment(db, (int)p3Id, (int)bus1Id, "B2", 2625, "booked");
            createTicketAndPayment(db, (int)p3Id, (int)bus3Id, "A3", 3675, "booked");

            // 6. Create Notifications for various scenarios
            createNotifications(db, (int)driver1Id, (int)driver2Id, (int)p1Id, (int)p2Id, (int)p3Id);
        });
    }

    private static void createTicketAndPayment(AppDatabase db, int userId, int busId, 
        String seatNumber, int points, String status) {
        
        Ticket ticket = new Ticket(userId, busId, seatNumber,
            System.currentTimeMillis() + 86400000, // Journey tomorrow
            "Colombo", "Kandy", status);
        long ticketId = db.ticketDao().insert(ticket);

        Payment payment = new Payment(0, userId, (int)ticketId, points);
        long paymentId = db.paymentDao().insert(payment);

        db.ticketDao().updateTicketPaymentId((int)ticketId, (int)paymentId);
        db.userDao().deductPoints(userId, points);
    }

    private static void createNotifications(AppDatabase db, int driver1Id, int driver2Id, 
        int passenger1Id, int passenger2Id, int passenger3Id) {
        
        // 1. Bus Assignment Notifications
        Bus bus1 = db.busDao().getBusByRegistration("NB-1234");
        Bus bus2 = db.busDao().getBusByRegistration("NB-5678");
        Bus bus3 = db.busDao().getBusByRegistration("NB-9012");
        BusOwner owner1 = db.busOwnerDao().getBusOwnerById(bus1.getOwnerId());
        BusOwner owner2 = db.busOwnerDao().getBusOwnerById(bus3.getOwnerId());

        // Driver 1 assignments
        createBusAssignmentNotification(db, driver1Id, bus1, owner1);
        createBusAssignmentNotification(db, driver1Id, bus2, owner1);

        // Driver 2 assignments
        createBusAssignmentNotification(db, driver2Id, bus3, owner2);

        // 2. Seat Swap Notifications
        // Get existing tickets for swaps
        List<Ticket> bus1Tickets = db.ticketDao().getTicketsByBusId(bus1.getId());
        List<Ticket> bus2Tickets = db.ticketDao().getTicketsByBusId(bus2.getId());
        List<Ticket> bus3Tickets = db.ticketDao().getTicketsByBusId(bus3.getId());

        // Create seat swap requests between passengers
        createSeatSwapNotifications(db, bus1Tickets, passenger1Id, passenger3Id);
        createSeatSwapNotifications(db, bus2Tickets, passenger1Id, passenger2Id);
        createSeatSwapNotifications(db, bus3Tickets, passenger2Id, passenger3Id);
    }

    private static void createBusAssignmentNotification(AppDatabase db, int driverId, 
        Bus bus, BusOwner owner) {
        
        Notification notification = new Notification(driverId, "BUS_ASSIGNMENT",
            "Bus Assignment Request",
            String.format("Owner %s wants to assign you to bus %s on route %s to %s", 
                owner.getCompanyName(), 
                bus.getRegistrationNumber(),
                bus.getRouteFrom(),
                bus.getRouteTo()));
        notification.setAdditionalData(bus.getRegistrationNumber());
        notification.setStatus("PENDING");
        db.notificationDao().insert(notification);
    }

    private static void createSeatSwapNotifications(AppDatabase db, List<Ticket> tickets, 
        int requesterId, int targetId) {
        
        // Find valid tickets belonging to these users
        Ticket requesterTicket = null;
        Ticket targetTicket = null;
        
        for (Ticket ticket : tickets) {
            if (ticket.getUserId() == requesterId && ticket.getStatus().equals("booked")) {
                requesterTicket = ticket;
            } else if (ticket.getUserId() == targetId && ticket.getStatus().equals("booked")) {
                targetTicket = ticket;
            }
            
            // Break if we found both tickets
            if (requesterTicket != null && targetTicket != null) {
                break;
            }
        }

        if (requesterTicket != null && targetTicket != null) {
            User requester = db.userDao().getUserById(requesterId);
            
            // Create notification for the target user
            Notification notification = new Notification(targetId, "SEAT_SWAP",
                "Seat Swap Request",
                String.format("%s would like to swap their seat %s with your seat %s", 
                    requester.getName(), 
                    requesterTicket.getSeatNumber(), 
                    targetTicket.getSeatNumber()));
            notification.setAdditionalData(requesterTicket.getId() + ":" + targetTicket.getId());
            notification.setStatus("PENDING");
            db.notificationDao().insert(notification);
            
            // Also create a notification for the requester to track their request
            Notification requesterNotification = new Notification(requesterId, "SEAT_SWAP",
                "Seat Swap Request Sent",
                String.format("You have requested to swap your seat %s with seat %s", 
                    requesterTicket.getSeatNumber(), 
                    targetTicket.getSeatNumber()));
            requesterNotification.setAdditionalData(requesterTicket.getId() + ":" + targetTicket.getId());
            requesterNotification.setStatus("PENDING");
            db.notificationDao().insert(requesterNotification);
        }
    }
}
