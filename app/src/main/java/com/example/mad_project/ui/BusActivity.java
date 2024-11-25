package com.example.mad_project.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mad_project.MainActivity;
import com.example.mad_project.R;
import com.example.mad_project.adapter.BusAdapter;
import com.example.mad_project.controller.BusController;
import com.example.mad_project.data.Bus;
import com.example.mad_project.utils.FareCalculator;
import com.example.mad_project.utils.DirectionsHandler;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import android.widget.Button;
import android.widget.TextView;
import androidx.core.widget.NestedScrollView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;

import java.util.List;
import java.text.NumberFormat;
import java.util.Locale;

public class BusActivity extends MainActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private RecyclerView busRecyclerView;
    private BusController busController;
    private BottomSheetBehavior<NestedScrollView> bottomSheetBehavior;
    private String fromLocation;
    private String toLocation;
    private MaterialButton clearFiltersButton;
    private DirectionsHandler directionsHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_bus, contentFrame);
        setupNavigation(true, true, "Bus Details");

        // Get route filter if coming from search
        fromLocation = getIntent().getStringExtra("from");
        toLocation = getIntent().getStringExtra("to");

        initializeViews();
        setupMap();
        setupBusList();
    }

    private void initializeViews() {
        busRecyclerView = findViewById(R.id.busRecyclerView);
        busRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        
        NestedScrollView bottomSheet = findViewById(R.id.bottomSheet);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        
        // Configure bottom sheet behavior
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        bottomSheetBehavior.setHideable(false);
        bottomSheetBehavior.setPeekHeight(300); // Adjust this value as needed
        
        // Add callback to handle state changes
        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                // Optional: Handle state changes
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                // Optional: Handle sliding
            }
        });
        
        TextView titleText = findViewById(R.id.availableBusesTitle);
        if (fromLocation != null && toLocation != null) {
            titleText.setText(String.format("Buses from %s to %s", fromLocation, toLocation));
        }
        
        clearFiltersButton = findViewById(R.id.clearFiltersButton);
        
        // Show FAB only if filters are applied
        clearFiltersButton.setVisibility(
            fromLocation != null && toLocation != null ? View.VISIBLE : View.GONE
        );
        
        // Setup click listener
        clearFiltersButton.setOnClickListener(v -> clearFilters());
    }

    private void setupMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    private void setupBusList() {
        busController = new BusController(this);
        
        if (fromLocation != null && toLocation != null) {
            busController.getBusesByRoute(fromLocation, toLocation, this::updateBusList);
        } else {
            busController.getAllBuses(this::updateBusList);
        }
    }

    private void updateBusList(List<Bus> buses) {
        runOnUiThread(() -> {
            // Update bus count
            TextView busCountText = findViewById(R.id.busCountText);
            String countText = fromLocation != null && toLocation != null ?
                String.format("%d buses found for this route", buses.size()) :
                String.format("Total %d buses available", buses.size());
            busCountText.setText(countText);

            // Update bus list
            BusAdapter adapter = new BusAdapter(buses, new BusAdapter.OnBusClickListener() {
                @Override
                public void onBusClick(Bus bus) {
                    showBusDetails(bus);
                }

                @Override
                public void onBookClick(Bus bus) {
                    startBookingProcess(bus);
                }
            });
            busRecyclerView.setAdapter(adapter);
            
            // Update map markers
            if (mMap != null) {
                updateMapMarkers(buses);
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Initial map setup
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        
        // Initialize DirectionsHandler
        directionsHandler = new DirectionsHandler(mMap, this, getString(R.string.MAPS_API_KEY));
        
        // Load buses again to add markers
        setupBusList();
    }

    private void updateMapMarkers(List<Bus> buses) {
        mMap.clear();
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        
        // Add bus markers
        for (Bus bus : buses) {
            LatLng busLocation = new LatLng(bus.getLatitude(), bus.getLongitude());
            builder.include(busLocation);
            
            float markerColor;
            switch (bus.getOwnerId()) {
                case 1:
                    markerColor = BitmapDescriptorFactory.HUE_RED;
                    break;
                case 2:
                    markerColor = BitmapDescriptorFactory.HUE_BLUE;
                    break;
                case 3:
                    markerColor = BitmapDescriptorFactory.HUE_GREEN;
                    break;
                default:
                    markerColor = BitmapDescriptorFactory.HUE_YELLOW;
            }
            
            mMap.addMarker(new MarkerOptions()
                    .position(busLocation)
                    .title(bus.getRegistrationNumber())
                    .snippet(String.format("%s to %s", bus.getRouteFrom(), bus.getRouteTo()))
                    .icon(BitmapDescriptorFactory.defaultMarker(markerColor)));
        }

        // Draw route if filters are applied
        if (fromLocation != null && toLocation != null && !buses.isEmpty()) {
            directionsHandler.displayRoute(fromLocation, toLocation);
        }

        // Zoom to show all markers
        if (!buses.isEmpty()) {
            LatLngBounds bounds = builder.build();
            int padding = 100;
            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
            mMap.animateCamera(cu);
        }
    }

    private void showBusDetails(Bus bus) {
        BottomSheetDialog bottomSheet = new BottomSheetDialog(this);
        View bottomSheetView = getLayoutInflater().inflate(R.layout.fare_breakdown_card, null);
        
        // Calculate fares
        FareCalculator.FareBreakdown standardFare = FareCalculator.calculateFare(bus);
        
        // Format currency
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("en", "LK"));
        
        // Set fare details
        TextView baseFareText = bottomSheetView.findViewById(R.id.baseFareText);
        TextView totalFareText = bottomSheetView.findViewById(R.id.totalFareText);
        
        baseFareText.setText(currencyFormat.format(standardFare.baseFare));
        totalFareText.setText(String.format("%s",
            currencyFormat.format(standardFare.totalFare)));
        
        // Add booking button handler
        Button bookNowButton = bottomSheetView.findViewById(R.id.bookNowButton);
        bookNowButton.setOnClickListener(v -> {
            startBookingProcess(bus);
            bottomSheet.dismiss();
        });
        
        bottomSheet.setContentView(bottomSheetView);
        bottomSheet.show();
    }

    private void startBookingProcess(Bus bus) {
        Intent intent = new Intent(this, SeatSelectionActivity.class);
        intent.putExtra("bus_id", bus.getId());
        startActivity(intent);
    }

    private void clearFilters() {
        fromLocation = null;
        toLocation = null;
        
        // Update title
        TextView titleText = findViewById(R.id.availableBusesTitle);
        titleText.setText("Available Buses");
        
        // Hide FAB
        clearFiltersButton.setVisibility(View.GONE);
        
        // Reload buses without filters
        setupBusList();
    }
}