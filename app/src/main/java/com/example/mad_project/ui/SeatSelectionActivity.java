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
    private GridLayout seatGrid;
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
        setupActionBar("Select Seat", true, false, false);

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
        seatGrid = findViewById(R.id.seatGrid);
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
        
        // Clear existing views
        seatGrid.removeAllViews();
        
        // Calculate button size (4 seats + aisle)
        int buttonSize = 80; // Fixed size for better visibility
        
        // Calculate rows needed (4 seats per row)
        int totalSeats = selectedBus.getCapacity();
        int rows = (totalSeats + 3) / 4; // Round up division
        
        // Set grid properties
        seatGrid.setColumnCount(5); // 2 seats + aisle + 2 seats
        seatGrid.setRowCount(rows);
        
        MaterialButton[] previouslySelected = new MaterialButton[1];
        
        // Create seat layout
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < 5; col++) {
                if (col == 2) { // Middle column (aisle)
                    Space space = new Space(this);
                    GridLayout.LayoutParams spaceParams = new GridLayout.LayoutParams();
                    spaceParams.width = buttonSize/2; // Half the button size for aisle
                    spaceParams.height = buttonSize;
                    space.setLayoutParams(spaceParams);
                    seatGrid.addView(space);
                    continue;
                }
                
                int seatIndex = row * 4 + (col > 2 ? col - 1 : col);
                if (seatIndex >= totalSeats) continue;
                
                MaterialButton seatButton = new MaterialButton(this);
                String seatNumber = getSeatNumber(seatIndex);
                
                // Set button size and margins
                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.width = buttonSize;
                params.height = buttonSize;
                params.setMargins(4, 4, 4, 4);
                seatButton.setLayoutParams(params);
                
                // Style the button
                seatButton.setBackground(ContextCompat.getDrawable(this, R.drawable.seat_button_background));
                seatButton.setText(seatNumber);
                seatButton.setTextSize(12);
                seatButton.setTextColor(Color.WHITE);
                seatButton.setInsetTop(0);
                seatButton.setInsetBottom(0);
                seatButton.setPadding(0, 0, 0, 0);
                
                if (bookedSeats.contains(seatNumber)) {
                    seatButton.setEnabled(false);
                }
                
                if (isPremiumSeat(seatNumber)) {
                    seatButton.setStrokeColor(ColorStateList.valueOf(
                        ContextCompat.getColor(this, R.color.gold)));
                    seatButton.setStrokeWidth(2);
                }
                
                seatButton.setOnClickListener(v -> {
                    // Deselect previous button if exists
                    if (previouslySelected[0] != null) {
                        previouslySelected[0].setSelected(false);
                        previouslySelected[0].animate()
                            .scaleX(1.0f)
                            .scaleY(1.0f)
                            .setDuration(150)
                            .start();
                    }
                    
                    // Select current button
                    seatButton.setSelected(true);
                    previouslySelected[0] = seatButton;
                    
                    // Animate selection
                    seatButton.animate()
                        .scaleX(1.15f)
                        .scaleY(1.15f)
                        .setDuration(150)
                        .withEndAction(() -> {
                            // Subtle bounce effect
                            seatButton.animate()
                                .scaleX(1.1f)
                                .scaleY(1.1f)
                                .setDuration(100)
                                .start();
                        })
                        .start();
                    
                    // Update selection info
                    updateSelectionInfo(seatNumber, isPremiumSeat(seatNumber));
                    
                    // Highlight selected seat in info panel
                    selectedSeatText.setTextColor(ContextCompat.getColor(
                        SeatSelectionActivity.this, 
                        R.color.green_light
                    ));
                });
                
                seatGrid.addView(seatButton);
            }
        }
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
        // Convert index to seat number (e.g., A1, A2, B1, B2)
        char row = (char) ('A' + (index / 4));
        int number = (index % 4) + 1;
        return String.format("%c%d", row, number);
    }

    private void proceedToPayment() {
        // Implement payment flow
    }
} 