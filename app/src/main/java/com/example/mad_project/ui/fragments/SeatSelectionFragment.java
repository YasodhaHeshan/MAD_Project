package com.example.mad_project.ui.fragments;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.mad_project.R;
import com.example.mad_project.adapter.SeatAdapter;
import com.example.mad_project.controller.SeatBookingController;
import com.example.mad_project.data.AppDatabase;
import com.example.mad_project.data.Bus;
import com.example.mad_project.data.Ticket;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class SeatSelectionFragment extends Fragment implements SeatAdapter.OnSeatClickListener {
    private GridLayout leftSeatGrid;
    private GridLayout rightSeatGrid;
    private SeatAdapter seatAdapter;
    private SeatBookingController bookingController;
    private Bus selectedBus;
    private boolean isSwapRequest;
    private String currentSeat;
    private List<String> selectedSeats = new ArrayList<>();
    private List<String> bookedSeats = new ArrayList<>();
    private List<String> myBookedSeats = new ArrayList<>();
    private Executor executor = Executors.newSingleThreadExecutor();
    private AppDatabase db;

    public static SeatSelectionFragment newInstance(int busId, boolean isSwapRequest, String currentSeat) {
        SeatSelectionFragment fragment = new SeatSelectionFragment();
        Bundle args = new Bundle();
        args.putInt("bus_id", busId);
        args.putBoolean("is_swap", isSwapRequest);
        args.putString("current_seat", currentSeat);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_seat_selection, container, false);
        initializeViews(view);
        setupController();
        loadBusDetails();
        return view;
    }

    private void initializeViews(View view) {
        leftSeatGrid = view.findViewById(R.id.leftSeatGrid);
        rightSeatGrid = view.findViewById(R.id.rightSeatGrid);
    }

    private void setupController() {
        bookingController = new SeatBookingController(requireContext());
        db = AppDatabase.getDatabase(requireContext());
    }

    private void loadBusDetails() {
        int busId = getArguments().getInt("bus_id", -1);
        isSwapRequest = getArguments().getBoolean("is_swap", false);
        currentSeat = getArguments().getString("current_seat");

        bookingController.loadBusDetails(busId, new SeatBookingController.BusLoadCallback() {
            @Override
            public void onBusLoaded(Bus bus) {
                selectedBus = bus;
                // Load booked seats
                executor.execute(() -> {
                    List<Ticket> tickets = db.ticketDao().getTicketsByBusId(busId);
                    bookedSeats.clear();
                    for (Ticket ticket : tickets) {
                        if (ticket.getStatus().equalsIgnoreCase("booked")) {
                            bookedSeats.add(ticket.getSeatNumber());
                        }
                    }
                    requireActivity().runOnUiThread(() -> setupSeatAdapter());
                });
            }

            @Override
            public void onLoadFailure(String error) {
                Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupSeatAdapter() {
        // Clear existing views
        leftSeatGrid.removeAllViews();
        rightSeatGrid.removeAllViews();
        
        // Set grid properties for vertical layout - 2 columns each side
        leftSeatGrid.setColumnCount(2);
        rightSeatGrid.setColumnCount(2);
        
        int totalSeats = selectedBus.getTotalSeats();
        
        // Create all seats
        for (int seatIndex = 0; seatIndex < totalSeats; seatIndex++) {
            String seatNumber = String.valueOf(seatIndex + 1);
            LinearLayout seatContainer = createSeatButton(seatNumber);
            
            // Even numbered seats go to left, odd to right
            if (seatIndex % 2 == 0) {
                leftSeatGrid.addView(seatContainer);
            } else {
                rightSeatGrid.addView(seatContainer);
            }
        }
    }

    private LinearLayout createSeatButton(String seatNumber) {
        LinearLayout seatContainer = new LinearLayout(requireContext());
        seatContainer.setOrientation(LinearLayout.VERTICAL);
        seatContainer.setGravity(Gravity.CENTER);
        
        // Create seat button with number inside
        MaterialButton button = new MaterialButton(requireContext());
        LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(150, 150);
        button.setLayoutParams(buttonParams);
        button.setText(seatNumber);
        button.setTextSize(14);
        button.setTextColor(Color.WHITE);
        
        // Remove all padding and insets
        button.setPadding(0, 0, 0, 0);
        button.setInsetTop(0);
        button.setInsetBottom(0);
        button.setIconPadding(0);
        button.setMinHeight(0);
        button.setMinWidth(0);
        button.setCornerRadius(16);
        
        // Prevent text wrapping
        button.setSingleLine(true);
        button.setMaxLines(1);
        button.setGravity(Gravity.CENTER);
        
        button.setBackgroundTintList(ColorStateList.valueOf(
            ContextCompat.getColor(requireContext(), R.color.green_light)));
        
        button.setOnClickListener(v -> {
            if (bookedSeats.contains(seatNumber)) {
                if (isSwapRequest) {
                    onBookedSeatClick(seatNumber);
                }
                return;
            }
            
            if (selectedSeats.contains(seatNumber)) {
                selectedSeats.remove(seatNumber);
                button.setBackgroundTintList(ColorStateList.valueOf(
                    ContextCompat.getColor(requireContext(), R.color.green_light)));
            } else {
                selectedSeats.add(seatNumber);
                button.setBackgroundTintList(ColorStateList.valueOf(
                    ContextCompat.getColor(requireContext(), R.color.accent_blue)));
            }
            
            updateSeatAppearance(button, seatNumber);
        });
        
        // Set container layout params for GridLayout
        GridLayout.LayoutParams containerParams = new GridLayout.LayoutParams();
        containerParams.width = GridLayout.LayoutParams.WRAP_CONTENT;
        containerParams.height = GridLayout.LayoutParams.WRAP_CONTENT;
        containerParams.setMargins(12, 12, 12, 12);
        seatContainer.setLayoutParams(containerParams);
        
        seatContainer.addView(button);
        
        return seatContainer;
    }

    private void updateSeatAppearance(MaterialButton button, String seatNumber) {
        Context context = requireContext();
        if (myBookedSeats.contains(seatNumber)) {
            // Seat booked by current user
            button.setBackgroundTintList(ColorStateList.valueOf(
                ContextCompat.getColor(context, R.color.yellow_light)));
        } else if (bookedSeats.contains(seatNumber)) {
            // Seat booked by others
            button.setBackgroundTintList(ColorStateList.valueOf(
                ContextCompat.getColor(context, R.color.red)));
        } else if (seatNumber.equals(currentSeat)) {
            button.setBackgroundTintList(ColorStateList.valueOf(
                ContextCompat.getColor(context, R.color.accent_blue)));
        } else if (selectedSeats.contains(seatNumber)) {
            button.setBackgroundTintList(ColorStateList.valueOf(
                ContextCompat.getColor(context, R.color.accent_blue)));
        } else {
            button.setBackgroundTintList(ColorStateList.valueOf(
                ContextCompat.getColor(context, R.color.green_light)));
        }
    }

    @Override
    public void onSeatSelected(String seatNumber) {
        if (isSwapRequest) {
            // Handle swap selection
            selectedSeats.clear();
            selectedSeats.add(seatNumber);
        } else {
            // Handle normal booking selection
            if (selectedSeats.contains(seatNumber)) {
                selectedSeats.remove(seatNumber);
            } else {
                selectedSeats.add(seatNumber);
            }
        }
        updateUI();
    }

    @Override
    public void onBookedSeatClick(String seatNumber) {
        if (isSwapRequest) {
            // Handle booked seat click for swap
            // Show confirmation dialog
        }
    }

    private void updateUI() {
        // Update selected seats UI and fare calculation
    }
} 