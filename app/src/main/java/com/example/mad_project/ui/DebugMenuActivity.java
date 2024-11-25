package com.example.mad_project.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.mad_project.MainActivity;
import com.example.mad_project.utils.FillDatabase;

public class DebugMenuActivity extends MainActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Create a vertical layout for debug buttons
        LinearLayout debugLayout = new LinearLayout(this);
        debugLayout.setOrientation(LinearLayout.VERTICAL);
        debugLayout.setPadding(32, 32, 32, 32);
        
        // Add fill database button
        Button fillDatabaseBtn = new Button(this);
        fillDatabaseBtn.setText("Fill Database");
        fillDatabaseBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, FillDatabase.class);
            startActivity(intent);
        });
        debugLayout.addView(fillDatabaseBtn);
        
        // Set the content view
        contentFrame.addView(debugLayout);
        
        // Setup navigation
        setupNavigation(true, true, "Debug Menu");
    }
}
