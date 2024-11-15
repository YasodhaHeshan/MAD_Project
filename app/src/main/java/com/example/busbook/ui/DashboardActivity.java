package com.example.busbook.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;


import com.example.busbook.R;

public class DashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        Button searchBusButton = findViewById(R.id.btnSearch);
        searchBusButton.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, SelectBusActivity.class);
            startActivity(intent);
        });
    }
}