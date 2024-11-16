package com.example.mad_project.ui;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.mad_project.R;
import com.example.mad_project.data.AppDatabase;
import com.example.mad_project.data.Bus;
import com.example.mad_project.data.BusDao;

public class RegisterBusActivity extends AppCompatActivity {

    private EditText editTextBusNumber, editTextBusOwnerId, editTextBusDriverId, editTextTicketId, editTextRouteId, editTextSeats;
    private Button buttonRegisterBus;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_bus);

        initializeViews();
        setupDatabase();

        buttonRegisterBus.setOnClickListener(v -> registerBus());
    }

    private void initializeViews() {
        editTextBusNumber = findViewById(R.id.editTextBusNumber);
        editTextBusOwnerId = findViewById(R.id.editTextBusOwnerId);
        editTextBusDriverId = findViewById(R.id.editTextBusDriverId);
        editTextTicketId = findViewById(R.id.editTextTicketId);
        editTextRouteId = findViewById(R.id.editTextRouteId);
        editTextSeats = findViewById(R.id.editTextSeats);
        buttonRegisterBus = findViewById(R.id.buttonRegisterBus);
    }

    private void setupDatabase() {
        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "mad_project_db")
                .fallbackToDestructiveMigration()
                .build();
    }

    private void registerBus() {
        String busNumber = editTextBusNumber.getText().toString();
        int busOwnerId = Integer.parseInt(editTextBusOwnerId.getText().toString());
        int busDriverId = Integer.parseInt(editTextBusDriverId.getText().toString());
        int ticketId = Integer.parseInt(editTextTicketId.getText().toString());
        int routeId = Integer.parseInt(editTextRouteId.getText().toString());
        int seats = Integer.parseInt(editTextSeats.getText().toString());

        Bus bus = new Bus(busNumber, "Start Location", "End Location", "Departure Time", "Arrival Time", seats);

        new Thread(() -> {
            try {
                BusDao busDao = db.busDao();
                busDao.insert(bus);
                runOnUiThread(() -> Toast.makeText(this, "Bus registered successfully", Toast.LENGTH_SHORT).show());
            } catch (Exception e) {
                runOnUiThread(() -> Toast.makeText(this, "Error registering bus: " + e.getMessage(), Toast.LENGTH_LONG).show());
            }
        }).start();
    }
}