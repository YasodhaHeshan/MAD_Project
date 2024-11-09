package com.example.mad_project.ui;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.mad_project.R;
import com.example.mad_project.data.Bus;
import com.example.mad_project.data.BusDao;
import com.example.mad_project.controller.AppDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RegisterBusActivity extends AppCompatActivity {

    private EditText departureLocation, arrivalLocation, departureTime, arrivalTime, availableSeats, busOwnerId, busDriverId, ticketPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_bus);

        departureLocation = findViewById(R.id.editTextDepartureLocation);
        arrivalLocation = findViewById(R.id.editTextArrivalLocation);
        departureTime = findViewById(R.id.editTextDepartureTime);
        arrivalTime = findViewById(R.id.editTextArrivalTime);
        availableSeats = findViewById(R.id.editTextAvailableSeats);
        busOwnerId = findViewById(R.id.editTextBusOwnerId);
        busDriverId = findViewById(R.id.editTextBusDriverId);
        ticketPrice = findViewById(R.id.editTextTicketPrice);
        Button registerBusButton = findViewById(R.id.buttonRegisterBus);

        registerBusButton.setOnClickListener(v -> registerBus());
    }

    private void registerBus() {
        String departureLoc = departureLocation.getText().toString();
        String arrivalLoc = arrivalLocation.getText().toString();
        String departureT = departureTime.getText().toString();
        String arrivalT = arrivalTime.getText().toString();
        int availableS, busOwner, busDriver, ticketP;
        try {
            availableS = Integer.parseInt(availableSeats.getText().toString());
            busOwner = Integer.parseInt(busOwnerId.getText().toString());
            busDriver = Integer.parseInt(busDriverId.getText().toString());
            ticketP = Integer.parseInt(ticketPrice.getText().toString());
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter valid numbers", Toast.LENGTH_LONG).show();
            return;
        }

        Bus bus = new Bus(0, departureLoc, arrivalLoc, departureT, arrivalT, availableS, busOwner, busDriver, ticketP);

        AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "mad_project_db").build();
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            try {
                BusDao busDao = db.busDao();
                busDao.insert(bus);
                runOnUiThread(() -> Toast.makeText(this, "Bus registered successfully", Toast.LENGTH_SHORT).show());
            } catch (Exception e) {
                runOnUiThread(() -> Toast.makeText(this, "Error registering bus: " + e.getMessage(), Toast.LENGTH_LONG).show());
            } finally {
                db.close();
            }
        });
    }
}