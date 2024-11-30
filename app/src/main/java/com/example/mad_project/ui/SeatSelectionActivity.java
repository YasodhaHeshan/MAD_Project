package com.example.mad_project.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.Date;


import com.example.mad_project.MainActivity;
import com.example.mad_project.R;
import com.example.mad_project.controller.NotificationController;
import com.example.mad_project.controller.PaymentController;
import com.example.mad_project.controller.TicketController;
import com.example.mad_project.data.Bus;
import com.example.mad_project.data.Ticket;
import com.example.mad_project.utils.BookingManager;
import com.example.mad_project.utils.DialogManager;
import com.example.mad_project.utils.FareCalculator;
import com.example.mad_project.utils.NotificationHandler;
import com.google.android.material.button.MaterialButton;
import com.example.mad_project.data.Payment;
import com.example.mad_project.data.User;
import com.example.mad_project.utils.EmailSender;
import com.example.mad_project.utils.SessionManager;


import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import com.example.mad_project.data.AppDatabase;
import android.content.res.ColorStateList;
import androidx.core.content.ContextCompat;
import android.content.Intent;

public class SeatSelectionActivity extends MainActivity {
    private GridLayout leftSeatGrid;
    private GridLayout rightSeatGrid;
    private TextView selectedSeatText;
    private TextView fareText;
    private Button confirmButton;
    private Bus selectedBus;
    private String selectedSeat;
    private final List<String> bookedSeats = new ArrayList<>();
    private final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("en", "LK"));
    private AppDatabase db;
    private final Executor executor = Executors.newSingleThreadExecutor();
    private List<MaterialButton> selectedButtons = new ArrayList<>();
    private List<String> selectedSeats = new ArrayList<>();
    private boolean isSwapRequest = false;
    private String currentSeat = null;
    private int ticketId = -1;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_seat_selection, contentFrame);
        setupNavigation(true, false, "Select Seats");

        // Initialize database
        db = AppDatabase.getDatabase(this);

        // Get bus ID from intent
        int busId = getIntent().getIntExtra("bus_id", -1);
        if (busId == -1) {
            Toast.makeText(this, "Invalid bus selection", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        isSwapRequest = getIntent().getBooleanExtra("is_swap", false);
        if (isSwapRequest) {
            currentSeat = getIntent().getStringExtra("current_seat");
            ticketId = getIntent().getIntExtra("ticket_id", -1);
        }

        initializeViews();
        loadBusDetails(busId);
    }

    private void initializeViews() {
        leftSeatGrid = findViewById(R.id.leftSeatGrid);
        rightSeatGrid = findViewById(R.id.rightSeatGrid);
        selectedSeatText = findViewById(R.id.selectedSeatText);
        fareText = findViewById(R.id.fareText);
        confirmButton = findViewById(R.id.confirmButton);

        // Set appropriate button visibility based on mode
        if (isSwapRequest) {
            confirmButton.setVisibility(View.GONE); // Hide confirm button in swap mode
            fareText.setVisibility(View.GONE); // Hide fare for swap requests
        } else {
            confirmButton.setText("Confirm Booking");
            confirmButton.setOnClickListener(v -> {
                if (!selectedSeats.isEmpty()) {
                    proceedToPayment();
                } else {
                    Toast.makeText(this, "Please select a seat", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void loadBusDetails(int busId) {
        executor.execute(() -> {
            // Get bus from database
            Bus bus = db.busDao().getBusById(busId);
            
            // Get booked seats for this bus
            List<Ticket> tickets = db.ticketDao().getTicketsByBusId(busId);
            bookedSeats.clear(); // Clear existing booked seats
            for (Ticket ticket : tickets) {
                if (ticket.getStatus().equalsIgnoreCase("booked")) {
                    bookedSeats.add(ticket.getSeatNumber());
                }
            }
            
            // Update UI on main thread
            runOnUiThread(() -> {
                if (bus != null) {
                    selectedBus = bus;
                    
                    // Update bus info in UI
                    TextView busInfoText = findViewById(R.id.busInfoText);
                    busInfoText.setText(String.format("%s (%s)\n%s to %s", 
                        bus.getRegistrationNumber(),
                        bus.getModel(),
                        bus.getRouteFrom(),
                        bus.getRouteTo()
                    ));
                    
                    // Now setup the seat grid after bus data is loaded
                    setupSeatGrid();
                } else {
                    Toast.makeText(this, "Bus not found", Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
        });
    }

    private void setupSeatGrid() {
        if (selectedBus == null) return;
        
        leftSeatGrid.removeAllViews();
        rightSeatGrid.removeAllViews();
        
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        int buttonSize = (screenWidth - 32 - 48) / 6; // Account for padding and aisle
        
        final MaterialButton[] selectedButtonHolder = new MaterialButton[1];
        
        for (int i = 0; i < selectedBus.getCapacity(); i++) {
            MaterialButton seatButton = createSeatButton(i, buttonSize, selectedButtonHolder);
            
            // Add to appropriate grid based on position
            if ((i % 4) < 2) {
                leftSeatGrid.addView(seatButton);
            } else {
                rightSeatGrid.addView(seatButton);
            }
        }
    }

    private MaterialButton createSeatButton(int position, int size, MaterialButton[] selectedButtonHolder) {
        MaterialButton button = new MaterialButton(this);
        String seatNumber = String.format("%c%d", (char)('A' + position / 4), (position % 4) + 1);
        
        // Set layout parameters with fixed size
        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
        params.width = 90;
        params.height = 90;
        params.setMargins(8, 8, 8, 8);
        button.setLayoutParams(params);
        
        // Set button appearance
        button.setText(seatNumber);
        button.setTextSize(16);
        button.setTextColor(Color.WHITE);
        button.setBackgroundResource(R.drawable.seat_color);
        button.setPadding(0, 0, 0, 0);
        button.setInsetTop(0);
        button.setInsetBottom(0);
        
        if (bookedSeats.contains(seatNumber)) {
            button.setBackgroundTintList(ColorStateList.valueOf(
                ContextCompat.getColor(this, R.color.red)));
            if (isSwapRequest) {
                button.setEnabled(true);
                button.setOnClickListener(v -> handleBookedSeatClick(seatNumber));
            } else {
                button.setEnabled(false);
            }
        } else {
            button.setBackgroundTintList(ColorStateList.valueOf(
                ContextCompat.getColor(this, R.color.green_light)));
            button.setOnClickListener(v -> handleSeatSelection(button, seatNumber));
        }
        
        if (isSwapRequest && seatNumber.equals(currentSeat)) {
            button.setBackgroundTintList(ColorStateList.valueOf(
                ContextCompat.getColor(this, R.color.accent_blue)));
        }
        
        return button;
    }

    private void updateSelectionInfo(List<String> seatNumbers) {
        if (seatNumbers.isEmpty()) {
            selectedSeatText.setText("No seats selected");
            fareText.setText("");
            return;
        }
        
        selectedSeatText.setText(String.format("Selected Seats: %s", 
            String.join(", ", seatNumbers)));
        
        FareCalculator.PointsBreakdown breakdown = FareCalculator.calculatePoints(selectedBus);
        int totalPoints = seatNumbers.size() * breakdown.totalPoints;
        
        fareText.animate()
            .alpha(0f)
            .setDuration(150)
            .withEndAction(() -> {
                fareText.setText(String.format("Total Fare: %s", 
                    NumberFormat.getCurrencyInstance(new Locale("en", "LK")).format(totalPoints)));
                fareText.animate()
                    .alpha(1f)
                    .setDuration(150)
                    .start();
            });
    }

    private String getSeatNumber(int index) {
        int row = index / 4;
        int col = index % 4;
        
        // Adjust column number for aisle gap
        if (col >= 2) {
            col += 1; // Add gap after second seat
        }
        
        // Convert row number to letter (A, B, C, etc.)
        char rowLetter = (char) ('A' + row);
        
        return String.format("%c%d", rowLetter, col + 1);
    }

    private void proceedToPayment() {
        SessionManager sessionManager = new SessionManager(this);
        int userId = sessionManager.getUserId();
        
        if (userId == -1) {
            Toast.makeText(this, "Please login first", Toast.LENGTH_SHORT).show();
            return;
        }

        BookingManager bookingManager = new BookingManager(this);
        bookingManager.processBooking(userId, selectedBus, selectedSeats, 
            new BookingManager.BookingCallback() {
                @Override
                public void onBookingSuccess(int pointsDeducted) {
                    runOnUiThread(() -> {
                        clearSelections();
                        
                        // Show success dialog with points info
                        DialogManager.showBookingSuccess(SeatSelectionActivity.this,
                            pointsDeducted, () -> {
                                // Navigate to tickets activity
                                Intent intent = new Intent(SeatSelectionActivity.this, 
                                    TicketsActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | 
                                    Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            });
                    });
                }

                @Override
                public void onBookingFailure(String error) {
                    runOnUiThread(() -> {
                        Toast.makeText(SeatSelectionActivity.this, 
                            "Booking failed: " + error, Toast.LENGTH_LONG).show();
                    });
                }
            });
    }

    private void clearSelections() {
        // Clear selections after successful booking
        for (MaterialButton button : selectedButtons) {
            button.setBackgroundResource(R.drawable.seat_color);
            button.setBackgroundTintList(ColorStateList.valueOf(
                ContextCompat.getColor(this, R.color.colorSecondaryBackground)));
            button.setSelected(false);
        }
        selectedButtons.clear();
        selectedSeats.clear();
        updateSelectionInfo(selectedSeats);
    }

    private void handleSeatSelection(MaterialButton seatButton, String seatNumber) {
        if (bookedSeats.contains(seatNumber)) {
            if (isSwapRequest) {
                handleBookedSeatClick(seatNumber);
            } else {
                Toast.makeText(this, "This seat is already booked", Toast.LENGTH_SHORT).show();
            }
            return;
        }

        // Handle normal seat selection
        if (!isSwapRequest) {
            if (selectedButtons.contains(seatButton)) {
                // Deselect seat
                selectedButtons.remove(seatButton);
                selectedSeats.remove(seatNumber);
                seatButton.setBackgroundTintList(ColorStateList.valueOf(
                    ContextCompat.getColor(this, R.color.green_light)));
            } else {
                // Select seat
                selectedButtons.add(seatButton);
                selectedSeats.add(seatNumber);
                seatButton.setBackgroundTintList(ColorStateList.valueOf(
                    ContextCompat.getColor(this, R.color.accent_blue)));
            }
            updateSelectionInfo(selectedSeats);
        }
    }

    private void handleBookedSeatClick(String seatNumber) {
        executor.execute(() -> {
            try {
                SessionManager sessionManager = new SessionManager(this);
                AppDatabase db = AppDatabase.getDatabase(this);
                
                User currentUser = db.userDao().getUserById(sessionManager.getUserId());
                Ticket currentTicket = db.ticketDao().getTicketById(ticketId);
                Ticket targetTicket = db.ticketDao().getTicketBySeatAndBus(seatNumber, selectedBus.getId());
                
                if (targetTicket != null && currentUser != null && currentTicket != null) {
                    runOnUiThread(() -> {
                        String title = "Confirm Seat Swap";
                        String message = String.format("Do you want to request to swap your seat %s with seat %s?", 
                            currentTicket.getSeatNumber(), seatNumber);
                        
                        DialogManager.showSwapConfirmationDialog(this, title, message, () -> {
                            NotificationHandler notificationHandler = new NotificationHandler(this);
                            notificationHandler.handleSeatSwapRequest(currentUser, currentTicket, targetTicket);
                            runOnUiThread(() -> {
                                Toast.makeText(this, "Seat swap request sent", Toast.LENGTH_SHORT).show();
                                finish();
                            });
                        });
                    });
                }
            } catch (Exception e) {
                Log.e("SeatSelection", "Error handling seat swap", e);
            }
        });
    }
} 