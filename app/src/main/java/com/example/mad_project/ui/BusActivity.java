package com.example.mad_project.ui;

import android.os.Bundle;
import com.example.mad_project.R;

public class BusActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus);
        
        // Setup actionbar with title "Buses"
        setupActionBar("Buses", true, true, true);
    }
} 