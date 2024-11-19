package com.example.mad_project.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mad_project.R;
import com.example.mad_project.utils.DirectionsHandler;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;
import java.util.Locale;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap googleMap;
    private String fieldType;
    private Marker selectedMarker;
    private DirectionsHandler directionsHandler;
    private static final LatLngBounds SRI_LANKA_BOUNDS = new LatLngBounds(
            new LatLng(5.916667, 79.516667),  // SW bounds
            new LatLng(9.850000, 81.883333)   // NE bounds
    );

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        fieldType = getIntent().getStringExtra("fieldType");
        String origin = getIntent().getStringExtra("origin");
        String destination = getIntent().getStringExtra("destination");

        // Initialize map
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        setupButtons();

        // If we have both origin and destination, we're in route display mode
        if (origin != null && destination != null) {
            setupRouteDisplay(origin, destination);
        } else {
            // We're in location selection mode
            setupLocationSelection();
        }
    }

    private void setupRouteDisplay(String origin, String destination) {
        // Hide location selection UI elements
        findViewById(R.id.confirm_location).setVisibility(View.GONE);
        
        // Show route when map is ready
        if (googleMap != null && directionsHandler != null) {
            directionsHandler.displayRoute(origin, destination);
        }
    }

    private void setupLocationSelection() {
        Button confirmButton = findViewById(R.id.confirm_location);
        confirmButton.setOnClickListener(v -> {
            if (selectedMarker != null) {
                returnResult(selectedMarker.getPosition(), selectedMarker.getTitle());
            } else {
                Toast.makeText(this, "Please select a location first", Toast.LENGTH_SHORT).show();
            }
        });

        // Enable map click for marker placement
        if (googleMap != null) {
            googleMap.setOnMapClickListener(position -> addMarker(position, "Selected Location"));
        }
    }

    private void setupButtons() {
        ImageButton backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> finish());
    }

    @Override
    public void onMapReady(@NonNull GoogleMap map) {
        googleMap = map;
        
        // Initialize DirectionsHandler
        directionsHandler = new DirectionsHandler(map, this, getString(R.string.MAPS_API_KEY));
        
        // Set Sri Lanka bounds
        googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(SRI_LANKA_BOUNDS, 100));

        // Check if we need to display a route
        String origin = getIntent().getStringExtra("origin");
        String destination = getIntent().getStringExtra("destination");
        if (origin != null && destination != null) {
            directionsHandler.displayRoute(origin, destination);
        } else {
            // Enable location selection mode
            googleMap.setOnMapClickListener(position -> addMarker(position, "Selected Location"));
        }
    }

    private void addMarker(LatLng position, String title) {
        if (selectedMarker != null) {
            selectedMarker.remove();
        }
        
        // Get location name from coordinates
        String locationName = getLocationName(position);
        
        selectedMarker = googleMap.addMarker(new MarkerOptions()
                .position(position)
                .title(locationName)
                .draggable(true));

        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(position, 15f));
    }

    private String getLocationName(LatLng position) {
        try {
            android.location.Geocoder geocoder = new android.location.Geocoder(this, Locale.getDefault());
            List<android.location.Address> addresses = geocoder.getFromLocation(
                    position.latitude, 
                    position.longitude, 
                    1
            );
            if (!addresses.isEmpty()) {
                android.location.Address address = addresses.get(0);
                if (address.getLocality() != null) {
                    return address.getLocality();
                }
                if (address.getSubAdminArea() != null) {
                    return address.getSubAdminArea();
                }
            }
        } catch (Exception e) {
            Log.e("MapActivity", "Error getting location name", e);
        }
        return "Unknown Location";
    }

    private void returnResult(LatLng position, String locationName) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("location", locationName);
        resultIntent.putExtra("fieldType", fieldType);
        setResult(RESULT_OK, resultIntent);
        finish();
    }
}