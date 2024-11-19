package com.example.mad_project.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.EditText;
import android.widget.Toast;
import android.app.DatePickerDialog;
import java.util.Calendar;
import java.util.Locale;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import android.view.MotionEvent;
import android.view.inputmethod.InputMethodManager;
import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mad_project.R;
import com.example.mad_project.controller.BusController;
import com.example.mad_project.controller.TicketController;
import com.example.mad_project.adapter.BusAdapter;
import com.example.mad_project.data.Bus;

public class SelectBusActivity extends AppCompatActivity {

    private RecyclerView busRecyclerView;
    private BusController busController;
    private TicketController ticketController;
    private BusAdapter busAdapter;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_bus);

        // Set up back button
        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());

        // Initialize RecyclerView and adapter
        busRecyclerView = findViewById(R.id.fragment_container);
        busRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        busAdapter = new BusAdapter(new ArrayList<>(), bus -> {
            // Handle bus selection
            Intent intent = new Intent(this, BookSeat.class);
            intent.putExtra("busId", bus.getId());
            startActivity(intent);
        });
        busRecyclerView.setAdapter(busAdapter);

        // Initialize controllers
        busController = new BusController(this);
        ticketController = new TicketController(this);

        // Set up search button
        Button searchButton = findViewById(R.id.btnSearch);
        searchButton.setOnClickListener(v -> filterBuses());

        // Set up date picker
        EditText dateField = findViewById(R.id.dateField);
        dateField.setOnClickListener(v -> showDatePickerDialog());

        // Initialize with current date
        Calendar calendar = Calendar.getInstance();
        updateDateField(calendar.get(Calendar.YEAR), 
            calendar.get(Calendar.MONTH), 
            calendar.get(Calendar.DAY_OF_MONTH));

        // Load initial bus data
        loadBuses();

    }

    private void loadBuses() {
        busController.getAllBuses(buses -> {
            runOnUiThread(() -> {
                busAdapter.updateBusList(buses);
            });
        });
    }

    private void filterBuses() {
        EditText fromField = findViewById(R.id.fromField);
        EditText toField = findViewById(R.id.toField);
        EditText dateField = findViewById(R.id.dateField);

        String from = fromField.getText().toString().trim();
        String to = toField.getText().toString().trim();
        long date = dateField.getTag() != null ? (long) dateField.getTag() : 0;

        if (from.isEmpty() || to.isEmpty() || date == 0) {
            Toast.makeText(this, "Please enter all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Filter buses based on criteria
        busController.getAllBuses(buses -> {
            List<Bus> filteredBuses = buses.stream()
                .filter(bus -> bus.getStartLocation().equalsIgnoreCase(from) &&
                              bus.getEndLocation().equalsIgnoreCase(to))
                .collect(Collectors.toList());
            
            runOnUiThread(() -> {
                busAdapter.updateBusList(filteredBuses);
                if (filteredBuses.isEmpty()) {
                    Toast.makeText(this, "No buses found for selected route", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private void showDatePickerDialog() {
        // Get Current Date
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, selectedYear, selectedMonth, selectedDay) -> 
                    updateDateField(selectedYear, selectedMonth, selectedDay),
                year, month, day);
        
        // Set minimum date to today
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        
        // Show the dialog
        datePickerDialog.show();
    }

    private void updateDateField(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day, 0, 0, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        
        // Store the timestamp for filtering
        long timestamp = calendar.getTimeInMillis();
        
        // Format date for display
        String selectedDate = String.format(Locale.getDefault(), "%04d/%02d/%02d", year, month + 1, day);
        EditText dateField = findViewById(R.id.dateField);
        dateField.setTag(timestamp); // Store timestamp in view tag
        dateField.setText(selectedDate);
    }

    private void showLocationSelectionDialog(EditText field) {
        new androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("Select Location Method")
            .setMessage("How would you like to select the location?")
            .setPositiveButton("Use Map", (dialog, which) -> {
                Intent intent = new Intent(this, MapActivity.class);
                intent.putExtra("fieldType", field.getId() == R.id.fromField ? "from" : "to");
                startActivityForResult(intent, 1);
            })
            .setNegativeButton("Enter Manually", (dialog, which) -> {
                field.setFocusableInTouchMode(true);
                field.requestFocus();
                field.setFocusable(true);
                
                // Show keyboard
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(field, InputMethodManager.SHOW_IMPLICIT);
            })
            .show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            String location = data.getStringExtra("location");
            String fieldType = data.getStringExtra("fieldType");
            
            if (location != null && fieldType != null) {
                EditText field = fieldType.equals("from") ? 
                    findViewById(R.id.fromField) : findViewById(R.id.toField);
                field.setText(location);
            }
        }
    }
}