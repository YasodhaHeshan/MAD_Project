package com.example.mad_project.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.mad_project.R;
import com.example.mad_project.data.BusOwner;
import com.example.mad_project.data.BusOwnerDao;
import com.example.mad_project.controller.AppDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RegisterOwnerActivity extends AppCompatActivity {

    private EditText ownerName, ownerEmail, ownerPhone;
    private Button registerOwnerButton;
    private Button buttonAddBus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_owner);

        ownerName = findViewById(R.id.editTextOwnerName);
        ownerEmail = findViewById(R.id.editTextOwnerEmail);
        ownerPhone = findViewById(R.id.editTextOwnerPhone);
        registerOwnerButton = findViewById(R.id.buttonRegisterOwner);
        buttonAddBus = findViewById(R.id.buttonAddBus); // Initialize buttonAddBus

        registerOwnerButton.setOnClickListener(v -> registerOwner());
        buttonAddBus.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterOwnerActivity.this, RegisterBusActivity.class);
            startActivity(intent);
        });
    }

    private void registerOwner() {
        String name = ownerName.getText().toString();
        String email = ownerEmail.getText().toString();
        String phone = ownerPhone.getText().toString();

        BusOwner owner = new BusOwner(0, name, email, phone, "password", "company", "busnayaka corp", 0, 1, 0);

        AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "mad_project_db").build();
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            try {
                BusOwnerDao ownerDao = db.busOwnerDao();
                ownerDao.insert(owner);
                runOnUiThread(() -> Toast.makeText(this, "Owner registered successfully", Toast.LENGTH_SHORT).show());
            } catch (Exception e) {
                runOnUiThread(() -> Toast.makeText(this, "Error registering owner: " + e.getMessage(), Toast.LENGTH_LONG).show());
            } finally {
                db.close();
            }
        });
    }
}