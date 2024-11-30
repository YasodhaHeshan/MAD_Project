package com.example.mad_project.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mad_project.MainActivity;
import com.example.mad_project.R;
import com.example.mad_project.controller.SeatBookingController;
import com.example.mad_project.data.Bus;
import com.example.mad_project.utils.SeatLayoutManager;
import com.example.mad_project.ui.fragments.SeatSelectionFragment;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class SeatBookActivity extends MainActivity {
    private SeatBookingController bookingController;
    private SeatLayoutManager layoutManager;
    private TextView fareText;
    private Button confirm;
    private BottomSheetBehavior<View> bottomSheetBehavior;
    private boolean isSwapRequest;
    private String currentSeat;
    private int ticketId;
    private Bus selectedBus;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_seat_selection, contentFrame);
        setupNavigation(true, false, "Select Seats");
        
        setupBottomSheet();
        initializeViews();
        setupControllers();
        handleIntentData();
    }
    
    private void setupBottomSheet() {
        View bottomSheet = findViewById(R.id.bottomSheet);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }
    
    private void initializeViews() {
        fareText = findViewById(R.id.fareText);
        confirm = findViewById(R.id.confirm);
        
        confirm.setOnClickListener(v -> {
            if (isSwapRequest) {
                bookingController.handleSeatSwap(currentSeat, ticketId, selectedBus,
                    new SeatBookingController.SwapCallback() {
                        @Override
                        public void onSwapSuccess() {
                            finish();
                        }
                        
                        @Override
                        public void onSwapFailure(String error) {
                            Toast.makeText(SeatBookActivity.this, error, Toast.LENGTH_SHORT).show();
                        }
                    });
            } else {
                bookingController.proceedToPayment();
            }
        });
    }
    
    private void setupControllers() {
        bookingController = new SeatBookingController(this);
        layoutManager = new SeatLayoutManager(this);
    }
    
    private void handleIntentData() {
        int busId = getIntent().getIntExtra("bus_id", -1);
        isSwapRequest = getIntent().getBooleanExtra("is_swap", false);
        currentSeat = getIntent().getStringExtra("current_seat");
        ticketId = getIntent().getIntExtra("ticket_id", -1);
        
        if (busId != -1) {
            bookingController.loadBusDetails(busId, new SeatBookingController.BusLoadCallback() {
                @Override
                public void onBusLoaded(Bus bus) {
                    selectedBus = bus;
                    getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.seatSelectionContainer, 
                            SeatSelectionFragment.newInstance(bus.getId(), isSwapRequest, currentSeat))
                        .commit();
                }
                
                @Override
                public void onLoadFailure(String error) {
                    Toast.makeText(SeatBookActivity.this, error, Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
        }
    }
} 