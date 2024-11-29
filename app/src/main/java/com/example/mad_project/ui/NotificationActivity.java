package com.example.mad_project.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mad_project.MainActivity;
import com.example.mad_project.R;
import com.example.mad_project.controller.NotificationController;
import com.example.mad_project.controller.TicketController;
import com.example.mad_project.data.AppDatabase;
import com.example.mad_project.data.Notification;
import com.example.mad_project.data.NotificationType;
import com.example.mad_project.utils.SessionManager;
import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class NotificationActivity extends MainActivity {
    private LinearLayout notificationsContainer;
    private TextView emptyNotificationsText;
    private AppDatabase db;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_notifications, contentFrame);
        setupNavigation(true, true, "Notifications");

        sessionManager = new SessionManager(this);
        db = AppDatabase.getDatabase(this);
        
        initializeViews();
        loadNotifications();
    }

    private void initializeViews() {
        notificationsContainer = findViewById(R.id.notificationsContainer);
        emptyNotificationsText = findViewById(R.id.emptyNotificationsText);
        
        if (notificationsContainer == null) {
            throw new RuntimeException("notificationsContainer not found");
        }
        if (emptyNotificationsText == null) {
            throw new RuntimeException("emptyNotificationsText not found");
        }
    }

    private void loadNotifications() {
        notificationsContainer.removeAllViews();
        int userId = sessionManager.getUserId();

        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            try {
                List<Notification> notifications = db.notificationDao().getPendingForUser(userId);
                Log.d("NotificationActivity", "Found " + notifications.size() + 
                    " notifications for user " + userId);
                
                for (Notification n : notifications) {
                    Log.d("NotificationActivity", "Notification: type=" + n.getType() + 
                        ", title=" + n.getTitle());
                }

                runOnUiThread(() -> {
                    if (notifications.isEmpty()) {
                        emptyNotificationsText.setVisibility(View.VISIBLE);
                    } else {
                        emptyNotificationsText.setVisibility(View.GONE);
                        for (Notification notification : notifications) {
                            displayNotification(notification);
                        }
                    }
                });
            } catch (Exception e) {
                Log.e("NotificationActivity", "Error loading notifications", e);
                e.printStackTrace();
            }
        });
    }

    private void displayNotification(Notification notification) {
        try {
            switch (notification.getType()) {
                case NotificationType.BUS_ASSIGNMENT:
                    addDriverBusAssignmentNotification(notification);
                    break;
                case NotificationType.SEAT_SWAP:
                    addSeatSwapNotification(notification);
                    break;
                default:
                    Log.w("NotificationActivity", "Unknown notification type: " + notification.getType());
                    break;
            }
        } catch (Exception e) {
            Log.e("NotificationActivity", "Error displaying notification", e);
        }
    }

    private void addDriverBusAssignmentNotification(Notification notification) {
        View notificationView = getLayoutInflater().inflate(R.layout.notification_card, notificationsContainer, false);
        
        TextView titleView = notificationView.findViewById(R.id.notificationTitle);
        TextView messageView = notificationView.findViewById(R.id.notificationMessage);
        MaterialButton acceptButton = notificationView.findViewById(R.id.acceptButton);
        MaterialButton declineButton = notificationView.findViewById(R.id.declineButton);
        
        titleView.setText(notification.getTitle());
        messageView.setText(notification.getMessage());
        
        acceptButton.setOnClickListener(v -> handleBusAssignmentResponse(true, notification.getAdditionalData(), notification.getId()));
        declineButton.setOnClickListener(v -> handleBusAssignmentResponse(false, notification.getAdditionalData(), notification.getId()));
        
        notificationsContainer.addView(notificationView);
    }

    private void handleBusAssignmentResponse(boolean accepted, String additionalData, int notificationId) {
        NotificationController controller = new NotificationController(this);
        controller.updateNotificationStatus(notificationId, accepted ? "ACCEPTED" : "DECLINED");
        
        String message = accepted ? 
            "You have been assigned to bus " + additionalData :
            "You declined the bus assignment";
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        
        // Refresh notifications
        notificationsContainer.removeAllViews();
        loadNotifications();
    }

    private void addSeatSwapNotification(Notification notification) {
        View notificationView = getLayoutInflater().inflate(R.layout.notification_card, notificationsContainer, false);
        
        TextView titleView = notificationView.findViewById(R.id.notificationTitle);
        TextView messageView = notificationView.findViewById(R.id.notificationMessage);
        MaterialButton acceptButton = notificationView.findViewById(R.id.acceptButton);
        MaterialButton declineButton = notificationView.findViewById(R.id.declineButton);
        MaterialButton cancelButton = notificationView.findViewById(R.id.cancelButton);
        
        titleView.setText(notification.getTitle());
        messageView.setText(notification.getMessage());
        
        // Show different buttons based on notification title instead of type
        boolean isRequester = notification.getTitle().equals("Seat Swap Request Sent");
        acceptButton.setVisibility(isRequester ? View.GONE : View.VISIBLE);
        declineButton.setVisibility(isRequester ? View.GONE : View.VISIBLE);
        cancelButton.setVisibility(isRequester ? View.VISIBLE : View.GONE);
        
        acceptButton.setOnClickListener(v -> handleSeatSwapResponse(true, notification.getAdditionalData(), notification.getId()));
        declineButton.setOnClickListener(v -> handleSeatSwapResponse(false, notification.getAdditionalData(), notification.getId()));
        cancelButton.setOnClickListener(v -> {
            NotificationController controller = new NotificationController(this);
            controller.handleSeatSwapDecision(notification.getId(), false, notification.getAdditionalData());
            notificationsContainer.removeAllViews();
            loadNotifications();
            Toast.makeText(this, "Seat swap request cancelled", Toast.LENGTH_SHORT).show();
        });
        
        notificationsContainer.addView(notificationView);
    }

    private void handleSeatSwapResponse(boolean accepted, String additionalData, int notificationId) {
        NotificationController controller = new NotificationController(this);
        TicketController ticketController = new TicketController(this);
        AppDatabase db = AppDatabase.getDatabase(this);

        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            try {
                // Get the notification to access ticket details
                Notification notification = db.notificationDao().getNotificationById(notificationId);
                if (notification == null) return;
                
                // Parse additional data (format: "ticketId1:ticketId2")
                String[] ticketIds = notification.getAdditionalData().split(":");
                int ticket1Id = Integer.parseInt(ticketIds[0]);
                int ticket2Id = Integer.parseInt(ticketIds[1]);
                
                if (accepted) {
                    // Perform the seat swap
                    ticketController.swapSeats(ticket1Id, ticket2Id, new TicketController.SwapCallback() {
                        @Override
                        public void onSuccess() {
                            runOnUiThread(() -> {
                                Toast.makeText(NotificationActivity.this, 
                                    "Seat swap completed successfully", Toast.LENGTH_SHORT).show();
                                
                                // Delete the notification after successful swap
                                executor.execute(() -> {
                                    db.notificationDao().deleteNotification(notificationId);
                                    runOnUiThread(() -> {
                                        notificationsContainer.removeAllViews();
                                        loadNotifications();
                                    });
                                });
                            });
                        }

                        @Override
                        public void onError(String message) {
                            runOnUiThread(() -> {
                                Toast.makeText(NotificationActivity.this, 
                                    "Failed to swap seats: " + message, Toast.LENGTH_SHORT).show();
                                
                                // Update notification status to failed
                                executor.execute(() -> {
                                    db.notificationDao().deleteNotification(notificationId);
                                    runOnUiThread(() -> {
                                        notificationsContainer.removeAllViews();
                                        loadNotifications();
                                    });
                                });
                            });
                        }
                    });
                } else {
                    // Delete notification for declined swap
                    db.notificationDao().deleteNotification(notificationId);
                    runOnUiThread(() -> {
                        Toast.makeText(NotificationActivity.this, 
                            "Seat swap request declined", Toast.LENGTH_SHORT).show();
                        notificationsContainer.removeAllViews();
                        loadNotifications();
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> {
                    Toast.makeText(NotificationActivity.this, 
                        "Error processing seat swap", Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    private void checkPendingNotifications() {
        AppDatabase db = AppDatabase.getDatabase(this);
        SessionManager sessionManager = new SessionManager(this);
        int userId = sessionManager.getUserId();

        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            List<Notification> pendingNotifications = db.notificationDao().getPendingForUser(userId);
            runOnUiThread(() -> {
                // Update notification badge in menu or toolbar
                invalidateOptionsMenu();
            });
        });
    }
}
