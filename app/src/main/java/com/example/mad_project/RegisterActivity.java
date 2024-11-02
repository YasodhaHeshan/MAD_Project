package com.example.mad_project;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registercus);
    }

    Button customer = findViewById(R.id.btnCustomer);
    Button driver = findViewById(R.id.btnDriver);
    Button owner = findViewById(R.id.btnCustomer);
    Button login = findViewById(R.id.btnLogin);
}
