package com.example.busbook.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.busbook.R;
import com.example.busbook.controller.UserController;

public class LoginActivity extends AppCompatActivity {

    EditText username;
    EditText password;
    Button registerButton;
    Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = findViewById(R.id.editTextEmail);
        password = findViewById(R.id.editTextPassword);
        registerButton = findViewById(R.id.btnRegister);
        loginButton = findViewById(R.id.btnLogin);

        registerButton.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        loginButton.setOnClickListener(v -> {
            String email = this.username.getText().toString();
            String password = this.password.getText().toString();

            //if login is successful, send the user to the dashboard and close the login activity
            UserController userController = new UserController(this);
            userController.login(email, password, this);
            finish();
        });
    }
}