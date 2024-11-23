package com.example.mad_project.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mad_project.R;
import com.example.mad_project.data.Bus;
import com.example.mad_project.data.Ticket;
import com.example.mad_project.service.bus.FareCalculator;
import com.google.android.material.button.MaterialButton;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import com.example.mad_project.data.AppDatabase;
import android.content.res.ColorStateList;
import androidx.core.content.ContextCompat;

public class SeatSelectionActivity extends BaseActivity {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seat_selection);
        setupActionBar("Select Your Seat", true, false, false);

        // Initialize database
        db = AppDatabase.getDatabase(this);

        // Get bus ID from intent
        int busId = getIntent().getIntExtra("bus_id", -1);
        if (busId == -1) {
            Toast.makeText(this, "Invalid bus selection", Toast.LENGTH_SHORT).show();
            finish();
            return;
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

        confirmButton.setOnClickListener(v -> {
            if (selectedSeat != null) {
                proceedToPayment();
            } else {
                Toast.makeText(this, "Please select a seat", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadBusDetails(int busId) {
        executor.execute(() -> {
            // Get bus from database
            Bus bus = db.busDao().getBusById(busId);
            
            // Get booked seats for this bus
            List<Ticket> tickets = db.ticketDao().getTicketsByBusId(busId);
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

    private MaterialButton createSeatButton(int index, int buttonSize, final MaterialButton[] selectedButtonHolder) {
        MaterialButton seatButton = new MaterialButton(this);
        String seatNumber = getSeatNumber(index);
        
        // Set button size and margins
        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
        params.width = buttonSize;
        params.height = buttonSize;
        params.setMargins(4, 4, 4, 4);
        seatButton.setLayoutParams(params);
        
        // Style the button
        seatButton.setBackgroundResource(R.drawable.seat_button_background);
        seatButton.setText(seatNumber);
        seatButton.setTextSize(12);
        seatButton.setTextColor(Color.WHITE);
        seatButton.setInsetTop(0);
        seatButton.setInsetBottom(0);
        seatButton.setPadding(0, 0, 0, 0);
        
        // Handle booked seats
        if (bookedSeats.contains(seatNumber)) {
            seatButton.setEnabled(false);
            seatButton.setBackgroundTintList(ColorStateList.valueOf(
                ContextCompat.getColor(this, R.color.red)));
        }
        
        // Handle premium seats
        if (isPremiumSeat(seatNumber)) {
            seatButton.setStrokeColor(ColorStateList.valueOf(
                ContextCompat.getColor(this, R.color.gold)));
            seatButton.setStrokeWidth(2);
        }
        
        seatButton.setOnClickListener(v -> {
            if (selectedButtonHolder[0] != null) {
                selectedButtonHolder[0].setBackgroundResource(R.drawable.seat_button_background);
                selectedButtonHolder[0].setSelected(false);
            }
            
            seatButton.setBackgroundResource(R.drawable.seat_button_selected_background);
            seatButton.setSelected(true);
            selectedButtonHolder[0] = seatButton;
            selectedSeat = seatNumber;
            
            updateSelectionInfo(seatNumber, isPremiumSeat(seatNumber));
        });
        
        return seatButton;
    }

    private void updateSelectionInfo(String seatNumber, boolean isPremium) {
        String seatType = isPremium ? "Premium Seat" : "Standard Seat";
        selectedSeatText.setText(String.format("%s: %s", seatType, seatNumber));
        
        // Calculate and display fare with animation
        FareCalculator.FareBreakdown fare = FareCalculator.calculateFare(
            selectedBus, 
            isPremium ? "PREMIUM" : "STANDARD"
        );
        
        fareText.animate()
            .alpha(0f)
            .setDuration(150)
            .withEndAction(() -> {
                fareText.setText("Total Fare: " + currencyFormat.format(fare.totalFare));
                fareText.animate()
                    .alpha(1f)
                    .setDuration(150)
                    .start();
            })
            .start();
    }

    private boolean isPremiumSeat(String seatNumber) {
        // Implement premium seat logic
        return seatNumber.startsWith("A") || seatNumber.startsWith("B");
    }

    private String getSeatNumber(int index) {
        // Organize seats from front to back
        // First two rows (A, B) are premium
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
        // Implement payment flow
    }
} 