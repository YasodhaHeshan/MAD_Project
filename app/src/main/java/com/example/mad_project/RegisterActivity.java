package com.example.mad_project;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registercus);

        Button login = findViewById(R.id.btnLogin);
        Button customer = findViewById(R.id.btnCustomer);
        Button driver = findViewById(R.id.btnDriver);
        Button owner = findViewById(R.id.btnCustomer);

        login.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
        });
    }





}
