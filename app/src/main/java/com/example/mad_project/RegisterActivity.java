package com.example.mad_project;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mad_project.data.DatabaseHelper;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registercus);

        Button login = findViewById(R.id.btnLogin);
        Button customer = findViewById(R.id.btnCustomer);
        Button driver = findViewById(R.id.btnDriver);
        Button owner = findViewById(R.id.btnOwner);
        Button signup = findViewById(R.id.btnSignup);


        EditText firstName = findViewById(R.id.editTextText2);
        EditText lastName = findViewById(R.id.editTextText4);
        EditText email = findViewById(R.id.editTextText5);
        EditText password = findViewById(R.id.editTextText7);
        EditText mobile = findViewById(R.id.editTextText6);

        login.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        signup.setOnClickListener(v -> {
            String firstNameStr = firstName.getText().toString();
            String lastNameStr = lastName.getText().toString();
            String emailStr = email.getText().toString();
            String passwordStr = password.getText().toString();
            String mobileStr = mobile.getText().toString();

            DatabaseHelper dbHelper = new DatabaseHelper(this);
            dbHelper.addUser(firstNameStr, lastNameStr, emailStr, passwordStr, mobileStr);

            Toast.makeText(this, "User registered successfully", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(RegisterActivity.this, PaymentActivity.class);
            startActivity(intent);
        });
    }
}