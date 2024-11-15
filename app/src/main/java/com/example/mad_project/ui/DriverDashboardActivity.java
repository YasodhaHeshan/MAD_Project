package com.example.mad_project.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mad_project.R;

public class DriverDashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_dashboard);

        Button btnFindBus = findViewById(R.id.btnFindBus);
        btnFindBus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DriverDashboardActivity.this, DriverFindBusActivity.class);
                startActivity(intent);
            }
        });
    }
}