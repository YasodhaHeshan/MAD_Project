//package com.example.mad_project.ui;
//
//import android.os.Bundle;
//import android.widget.AutoCompleteTextView;
//import android.widget.ArrayAdapter;
//import android.widget.Toast;
//import com.example.mad_project.MainActivity;
//import com.example.mad_project.R;
//import com.example.mad_project.data.AppDatabase;
//import com.example.mad_project.data.Bus;
//import com.example.mad_project.utils.SessionManager;
//import com.google.android.material.textfield.TextInputEditText;
//import com.google.android.material.button.MaterialButton;
//import java.util.concurrent.Executor;
//import java.util.concurrent.Executors;
//
//public class AddBusActivity extends MainActivity {
//    private TextInputEditText registrationInput;
//    private TextInputEditText modelInput;
//    private TextInputEditText seatsInput;
//    private AutoCompleteTextView fromLocationInput;
//    private AutoCompleteTextView toLocationInput;
//    private TextInputEditText basePointsInput;
//    private MaterialButton addBusButton;
//    private AppDatabase db;
//    private Executor executor = Executors.newSingleThreadExecutor();
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        getLayoutInflater().inflate(R.layout.activity_add_bus, contentFrame);
//        setupNavigation(true, false, "Add New Bus");
//
//        db = AppDatabase.getDatabase(this);
//        initializeViews();
//        setupLocationAdapters();
//        setupAddBusButton();
//    }
//
//    private void initializeViews() {
//        registrationInput = findViewById(R.id.registrationInput);
//        modelInput = findViewById(R.id.modelInput);
//        seatsInput = findViewById(R.id.seatsInput);
//        fromLocationInput = findViewById(R.id.fromLocationInput);
//        toLocationInput = findViewById(R.id.toLocationInput);
//        basePointsInput = findViewById(R.id.basePointsInput);
//        addBusButton = findViewById(R.id.addBusButton);
//    }

//    private void setupLocationAdapters() {
//        String[] locations = getResources().getStringArray(R.array.locations);
//        ArrayAdapter<String> adapter = new ArrayAdapter<>(
//            this, android.R.layout.simple_dropdown_item_1line, locations
//        );
//        fromLocationInput.setAdapter(adapter);
//        toLocationInput.setAdapter(adapter);
//    }
//
//    private void setupAddBusButton() {
//        addBusButton.setOnClickListener(v -> {
//            if (validateInputs()) {
//                addBus();
//            }
//        });
//    }
//
//    private boolean validateInputs() {
//        // Add validation logic here
//        return true;
//    }
//
//    private void addBus() {
//        SessionManager sessionManager = new SessionManager(this);
//        int ownerId = sessionManager.getUserId();
//
//        Bus newBus = new Bus(
//            ownerId,
//            registrationInput.getText().toString(),
//            modelInput.getText().toString(),
//            Integer.parseInt(seatsInput.getText().toString()),
//            "WiFi, AC", // Default amenities
//            true,
//            fromLocationInput.getText().toString(),
//            toLocationInput.getText().toString(),
//            0.0, 0.0, // Default coordinates
//            System.currentTimeMillis(),
//            System.currentTimeMillis() + 3600000,
//            Integer.parseInt(basePointsInput.getText().toString())
//        );
//
//        executor.execute(() -> {
//            try {
//                long busId = db.busDao().insert(newBus);
//                runOnUiThread(() -> {
//                    Toast.makeText(this, "Bus added successfully", Toast.LENGTH_SHORT).show();
//                    finish();
//                });
//            } catch (Exception e) {
//                runOnUiThread(() -> {
//                    Toast.makeText(this, "Failed to add bus", Toast.LENGTH_SHORT).show();
//                });
//            }
//        });
//    }
//}