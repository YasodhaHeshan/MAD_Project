package com.example.mad_project.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.example.mad_project.R;
import com.example.mad_project.controller.BusController;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

public class DashboardActivity extends BaseActivity {
    private AutoCompleteTextView fromLocationInput;
    private AutoCompleteTextView toLocationInput;
    private Button searchBusButton;
    private BusController busController;
    private ArrayAdapter<String> fromAdapter;
    private ArrayAdapter<String> toAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        
        busController = new BusController(this);
        initializeViews();
        setupListeners();
        setupAdapters();
    }

    private void initializeViews() {
        fromLocationInput = findViewById(R.id.fromLocationInput);
        toLocationInput = findViewById(R.id.toLocationInput);
        searchBusButton = findViewById(R.id.searchBusButton);
    }

    private void setupAdapters() {
        fromAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line);
        toAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line);
        
        fromLocationInput.setAdapter(fromAdapter);
        toLocationInput.setAdapter(toAdapter);
    }

    private void setupListeners() {
        searchBusButton.setOnClickListener(v -> {
            String from = fromLocationInput.getText().toString().trim();
            String to = toLocationInput.getText().toString().trim();
            
            if (from.isEmpty() || to.isEmpty()) {
                Toast.makeText(this, "Please enter both locations", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent intent = new Intent(this, BusActivity.class);
            intent.putExtra("from", from);
            intent.putExtra("to", to);
            startActivity(intent);
        });

        fromLocationInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String query = s.toString().trim();
                if (query.length() >= 2) {
                    busController.getRouteSuggestions(query, suggestions -> {
                        runOnUiThread(() -> {
                            fromAdapter.clear();
                            fromAdapter.addAll(suggestions);
                            fromAdapter.notifyDataSetChanged();
                        });
                    });
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        toLocationInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String query = s.toString().trim();
                if (query.length() >= 2) {
                    busController.getRouteSuggestions(query, suggestions -> {
                        runOnUiThread(() -> {
                            toAdapter.clear();
                            toAdapter.addAll(suggestions);
                            toAdapter.notifyDataSetChanged();
                        });
                    });
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }
}
