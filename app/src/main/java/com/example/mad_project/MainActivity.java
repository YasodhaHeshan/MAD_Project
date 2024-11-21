package com.example.mad_project;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mad_project.ui.DashboardActivity;
import com.example.mad_project.ui.FillDatabaseActivity;
import com.example.mad_project.ui.LoginActivity;
import com.example.mad_project.utils.SessionManager;

public class MainActivity extends AppCompatActivity {

    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Initialize SessionManager
        sessionManager = new SessionManager(this);

        // Check if user is already logged in
        if (sessionManager.isLoggedIn()) {
            // Redirect to Dashboard
            Intent intent = new Intent(MainActivity.this, DashboardActivity.class);
            startActivity(intent);
            finish(); // Close MainActivity so user can't go back to it
            return;
        }

        Button getStartedBtn = findViewById(R.id.btnStart);
        Button fillDatabaseBtn = findViewById(R.id.btn_fill_database);

        getStartedBtn.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        fillDatabaseBtn.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, FillDatabaseActivity.class);
            startActivity(intent);
        });
    }
}