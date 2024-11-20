package com.example.mad_project.ui;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mad_project.R;
import com.example.mad_project.data.AppDatabase;
import com.example.mad_project.data.Bus;
import com.example.mad_project.data.BusDao;

import com.example.mad_project.adapter.BusAdapter;

import java.util.Calendar;
import java.util.List;

public class SelectBusActivity extends AppCompatActivity {
    private EditText fromField, toField, dateField;
    private Button searchButton, passengerButton;
    private ImageButton backButton, swapButton;
    private RecyclerView busListView;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_bus);

        initializeViews();
        setupDatabase();
        setupListeners();
        
        // Get intent data if coming from dashboard
        String from = getIntent().getStringExtra("from");
        String to = getIntent().getStringExtra("to");
        if (from != null && to != null) {
            fromField.setText(from);
            toField.setText(to);
            searchBuses();
        }
    }

    private void initializeViews() {
        fromField = findViewById(R.id.fromField);
        toField = findViewById(R.id.toField);
        dateField = findViewById(R.id.dateField);
        searchButton = findViewById(R.id.btnSearch);
        passengerButton = findViewById(R.id.passengerButton);
        backButton = findViewById(R.id.backButton);
        swapButton = findViewById(R.id.swapButton);
        busListView = findViewById(R.id.fragment_container);
        busListView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setupDatabase() {
        db = AppDatabase.getDatabase(getApplicationContext());
    }

    private void setupListeners() {
        searchButton.setOnClickListener(v -> searchBuses());
        backButton.setOnClickListener(v -> finish());
        swapButton.setOnClickListener(v -> swapLocations());
        dateField.setOnClickListener(v -> showDatePicker());
    }

    private void searchBuses() {
        String from = fromField.getText().toString();
        String to = toField.getText().toString();

        if (from.isEmpty() || to.isEmpty()) {
            Toast.makeText(this, "Please enter both locations", Toast.LENGTH_SHORT).show();
            return;
        }

        new Thread(() -> {
            try {
                BusDao busDao = db.busDao();
                List<Bus> buses = busDao.getBusesByRoute(from, to);
                
                runOnUiThread(() -> {
                    if (buses.isEmpty()) {
                        Toast.makeText(this, "No buses found for this route", 
                            Toast.LENGTH_SHORT).show();
                    } else {
                            BusAdapter adapter = new BusAdapter(buses, bus -> {
                            Intent intent = new Intent(SelectBusActivity.this, BookSeat.class);
                            intent.putExtra("busId", bus.getId());
                            startActivity(intent);
                        });
                        busListView.setAdapter(adapter);
                    }
                });
            } catch (Exception e) {
                runOnUiThread(() -> Toast.makeText(this, 
                    "Error searching buses: " + e.getMessage(), 
                    Toast.LENGTH_LONG).show());
            }
        }).start();
    }

    private void swapLocations() {
        String temp = fromField.getText().toString();
        fromField.setText(toField.getText().toString());
        toField.setText(temp);
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(
            this,
            (view, year, month, day) -> {
                String date = String.format("%d/%02d/%02d", year, month + 1, day);
                dateField.setText(date);
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }
}
