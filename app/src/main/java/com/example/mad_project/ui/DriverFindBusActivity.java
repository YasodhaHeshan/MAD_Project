package com.example.mad_project.ui;

import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mad_project.R;

public class DriverFindBusActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_find_bus);

        ImageButton backBtn = findViewById(R.id.btnBack);
        backBtn.setOnClickListener(v -> {
            finish();
        });

    }

}
