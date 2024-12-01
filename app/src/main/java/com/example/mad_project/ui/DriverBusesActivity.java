package com.example.mad_project.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mad_project.MainActivity;
import com.example.mad_project.R;
import com.example.mad_project.adapter.BusAdapter;
import com.example.mad_project.data.AppDatabase;
import com.example.mad_project.data.Bus;
import com.example.mad_project.utils.SessionManager;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class DriverBusesActivity extends MainActivity {
    private RecyclerView busRecyclerView;
    private TextView emptyView;
    private AppDatabase db;
    private Executor executor = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_driver_buses, contentFrame);
        setupNavigation(true, false, "My Assigned Buses");

        initializeViews();
        loadAssignedBuses();

        db = AppDatabase.getDatabase(this);
    }

    private void initializeViews() {
        busRecyclerView = findViewById(R.id.busRecyclerView);
        emptyView = findViewById(R.id.emptyView);
        busRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void loadAssignedBuses() {
        SessionManager sessionManager = new SessionManager(this);
        int driverId = sessionManager.getUserId();

        executor.execute(() -> {
            List<Bus> assignedBuses = db.busDao().getBusesByDriverId(driverId);
            
            runOnUiThread(() -> {
                if (assignedBuses.isEmpty()) {
                    emptyView.setVisibility(View.VISIBLE);
                    busRecyclerView.setVisibility(View.GONE);
                } else {
                    emptyView.setVisibility(View.GONE);
                    busRecyclerView.setVisibility(View.VISIBLE);
                    
                    BusAdapter adapter = new BusAdapter(assignedBuses, 
                        new BusAdapter.OnBusClickListener() {
                            @Override
                            public void onBusClick(Bus bus) {
                                showBusDetails(bus);
                            }

                            @Override
                            public void onBookClick(Bus bus) {
                                // Not needed for driver view
                            }
                        }, false);  // false for non-owner view
                    busRecyclerView.setAdapter(adapter);
                }
            });
        });
    }

    private void showBusDetails(Bus bus) {
        // Show bus details in a bottom sheet or new activity
        // You can reuse existing detail view logic
    }
} 