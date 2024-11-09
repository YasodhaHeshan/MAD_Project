package com.example.mad_project.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.mad_project.R;
import com.example.mad_project.controller.AppDatabase;
import com.example.mad_project.data.Bus;
import com.example.mad_project.data.BusDao;
import com.example.mad_project.controller.BusListAdapter;
import com.google.android.material.datepicker.MaterialDatePicker;

import java.util.List;

public class SelectBusActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private BusListAdapter busListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_bus);

        EditText dateField = findViewById(R.id.dateField);
        dateField.setOnClickListener(v -> showDatePicker(dateField));

        EditText from = findViewById(R.id.fromField);
        EditText to = findViewById(R.id.toField);
        Button searchButton = findViewById(R.id.btnSearch);

        recyclerView = findViewById(R.id.busRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        searchButton.setOnClickListener(v -> {
            String origin = from.getText().toString();
            String destination = to.getText().toString();

            Toast.makeText(this, "Searching for buses from " + origin + " to " + destination, Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(this, MapActivity.class);
            intent.putExtra("origin", origin);
            intent.putExtra("destination", destination);
            startActivity(intent);
        });
    }

    private void showDatePicker(EditText dateField) {
        MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select Date")
            .build();

        datePicker.addOnPositiveButtonClickListener(selection -> dateField.setText(datePicker.getHeaderText()));

        datePicker.show(getSupportFragmentManager(), "DATE_PICKER");
    }
}