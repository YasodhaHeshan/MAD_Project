package com.example.mad_project.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.example.mad_project.MainActivity;
import com.example.mad_project.R;
import com.example.mad_project.controller.BusController;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

public class DashboardActivity extends MainActivity {
    private AutoCompleteTextView fromLocationInput;
    private AutoCompleteTextView toLocationInput;
    private Button searchBusButton;
    private MaterialToolbar topAppBar;
    private BusController busController;
    private ArrayAdapter<String> fromAdapter;
    private ArrayAdapter<String> toAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_dashboard, contentFrame);
        setupNavigation(true, true, "Dashboard");

        busController = new BusController(this);
        fromLocationInput = findViewById(R.id.fromLocationInput);
        toLocationInput = findViewById(R.id.toLocationInput);
        searchBusButton = findViewById(R.id.searchBusButton);
        setupListeners();
        setupAdapters();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_app_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        
        if (id == R.id.action_notifications) {
            // TODO: Handle notifications
            return true;
        } else if (id == R.id.action_settings) {
            startActivity(new Intent(this, ProfileActivity.class));
            return true;
        }
        
        return super.onOptionsItemSelected(item);
    }
}
