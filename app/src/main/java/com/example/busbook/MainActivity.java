package com.example.busbook;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.busbook.ui.FillDatabaseActivity;
import com.example.busbook.ui.LoginActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

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