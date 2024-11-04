package com.example.mad_project;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        EditText username = findViewById(R.id.editTextUsername);
        EditText password = findViewById(R.id.editTextPassword);
        Button regbutton = findViewById(R.id.btnRegister);
        Button loginButton = findViewById(R.id.btnLogin);

        regbutton.setOnClickListener(v -> {
            Intent intent = new Intent(Login.this, RegisterActivity.class);
            startActivity(intent);
        });

        loginButton.setOnClickListener(v -> {
            Intent intent = new Intent(Login.this, DashboardActivity.class);
            startActivity(intent);
        });
    }
}
