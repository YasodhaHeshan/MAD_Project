// SelectBusActivity.java
package com.example.mad_project.ui;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mad_project.R;
import com.example.mad_project.controller.BusController;
import com.example.mad_project.controller.TicketController;

public class SelectBusActivity extends AppCompatActivity {

    private RecyclerView busRecyclerView;
    private BusAdapter busAdapter;
    private BusController busController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_bus);

        // Set up back button
        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());

        // Initialize RecyclerView
        busRecyclerView = findViewById(R.id.fragment_container);
        busRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        busRecyclerView.setHasFixedSize(true);

        // Initialize BusController
        busController = new BusController(this);

        // Load all buses initially
        loadAllBuses();

        // Set up search button
        Button searchButton = findViewById(R.id.btnSearch);
        searchButton.setOnClickListener(v -> filterBuses());
    }

    private void loadAllBuses() {
        try {
            busController.getAllBuses(busList -> {
                if (busList != null && !isFinishing()) {
                    runOnUiThread(() -> {
                        try {
                            TicketController ticketController = new TicketController(this);
                            busAdapter = new BusAdapter(busList, ticketController);
                            busRecyclerView.setAdapter(busAdapter);
                        } catch (Exception e) {
                            e.printStackTrace();
                            // Show error message to user
                            Toast.makeText(this, "Error loading buses: " + e.getMessage(),
                                Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void filterBuses() {
        EditText fromField = findViewById(R.id.fromField);
        EditText toField = findViewById(R.id.toField);
        EditText dateField = findViewById(R.id.dateField);

        String from = fromField.getText().toString();
        String to = toField.getText().toString();
        String date = dateField.getText().toString();

        try {
            busController.getBusesByRoute(from, to, busList -> {
                if (busList != null && !isFinishing()) {
                    runOnUiThread(() -> {
                        try {
                            TicketController ticketController = new TicketController(this);
                            busAdapter = new BusAdapter(busList, ticketController);
                            busRecyclerView.setAdapter(busAdapter);
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(this, "Error filtering buses: " + e.getMessage(), 
                                Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}