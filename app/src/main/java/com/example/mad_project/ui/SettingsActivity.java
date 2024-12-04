package com.example.mad_project.ui;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;

import com.example.mad_project.MainActivity;
import com.example.mad_project.R;
import com.example.mad_project.data.AppDatabase;
import com.example.mad_project.data.Location;
import com.example.mad_project.data.User;
import com.example.mad_project.data.BusDriver;
import com.example.mad_project.data.BusOwner;
import com.example.mad_project.utils.Validation;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class SettingsActivity extends MainActivity {
    private AppDatabase db;
    private final Executor executor = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_settings, contentFrame);
        setupNavigation(true, true, "Settings");

        db = AppDatabase.getDatabase(this);
        MaterialButton fillDbButton = findViewById(R.id.fillDatabaseButton);

        fillDbButton.setOnClickListener(v -> showFillConfirmation());

        findViewById(R.id.fillDatabaseButton).setOnClickListener(v -> {
            new MaterialAlertDialogBuilder(this)
                .setTitle("Fill Database")
                .setMessage("This will add sample users to the database. Continue?")
                .setPositiveButton("Yes", (dialog, which) -> fillDatabase())
                .setNegativeButton("No", null)
                .show();
        });
    }

    private void showFillConfirmation() {
        new AlertDialog.Builder(this)
            .setTitle("Fill Database")
            .setMessage("This will add sample users to the database. Are you sure?")
            .setPositiveButton("Fill", (dialog, which) -> fillDatabase())
            .setNegativeButton("Cancel", null)
            .show();
    }

    private void fillDatabase() {
        executor.execute(() -> {
            try {
                addHighwayLocations();

                runOnUiThread(() -> 
                    Toast.makeText(this, "Database reset successful", Toast.LENGTH_SHORT).show());
            } catch (Exception e) {
                runOnUiThread(() -> 
                    Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            }
        });
    }

    private void addHighwayLocations() {
        Location[] locations = {
            new Location("Colombo", 6.927079, 79.861243),
            new Location("Kandy", 7.290572, 80.633728),
            new Location("Galle", 6.053519, 80.220978),
            new Location("Jaffna", 9.661302, 80.025513),
            new Location("Anuradhapura", 8.311338, 80.403656),
            new Location("Trincomalee", 8.578132, 81.233040),
            new Location("Batticaloa", 7.717935, 81.700088),
            new Location("Kurunegala", 7.486842, 80.362439),
            new Location("Matara", 5.948853, 80.535888),
            new Location("Negombo", 7.189464, 79.858734),
            new Location("Ratnapura", 6.693813, 80.405845),
            new Location("Badulla", 6.989821, 81.054276),
            new Location("Polonnaruwa", 7.940576, 81.018193),
            new Location("Hambantota", 6.127064, 81.111197),
            new Location("Dambulla", 7.868039, 80.650698)
        };

        for (Location location : locations) {
            db.locationDao().insert(location);
        }

        executor.execute(() -> {
            try {
                db.runInTransaction(() -> {
                    // Create passenger users
                    User passenger1 = new User("Adithya Ekanayaka", "adithyasean@gmail.com", "+94702967909",
                        Validation.hashPassword("aaaaaa"), "user");
                    passenger1.setPoints(50000);
                    long passenger1Id = db.userDao().insert(passenger1);

                    User passenger2 = new User("Adithya Ekanayaka", "adithyasean@outlook.com", "+94767007909",
                        Validation.hashPassword("aaaaaa"), "user");
                    passenger2.setPoints(30000);
                    long passenger2Id = db.userDao().insert(passenger2);

                    User passenger3 = new User("Yasodha Heshan", "yasodhaheshan2002@gmail.com", "+94111111111",
                        Validation.hashPassword("aaaaaa"), "user");
                    passenger3.setPoints(45000);
                    long passenger3Id = db.userDao().insert(passenger3);

                    // Create owner users
                    User ownerUser1 = new User("Senith Nimsara", "senith2002n@gmail.com", "+94761234567",
                        Validation.hashPassword("aaaaaa"), "owner");
                    ownerUser1.setPoints(10000);
                    long ownerUser1Id = db.userDao().insert(ownerUser1);

                    User ownerUser2 = new User("Jalith Chamikara", "premjalith.@gmail.com", "+94762345678",
                        Validation.hashPassword("aaaaaa"), "owner");
                    ownerUser2.setPoints(15000);
                    long ownerUser2Id = db.userDao().insert(ownerUser2);

                    // Create owner records
                    BusOwner owner1 = new BusOwner(
                        (int)ownerUser1Id,
                        "Senith Transport",
                        "REG123456",
                        "TAX123456"
                    );
                    db.busOwnerDao().insert(owner1);

                    BusOwner owner2 = new BusOwner(
                        (int)ownerUser2Id,
                        "Jalith Transport",
                        "REG789012",
                        "TAX789012"
                    );
                    db.busOwnerDao().insert(owner2);

                    // Create driver users
                    User driverUser1 = new User("Dave Brown", "ai@gmail.com", "+94751234567",
                        Validation.hashPassword("aaaaaa"), "driver");
                    driverUser1.setPoints(10000);
                    long driverUser1Id = db.userDao().insert(driverUser1);

                    User driverUser2 = new User("John Connor", "a@a.a", "+94752345678",
                        Validation.hashPassword("aaaaaa"), "driver");
                    driverUser2.setPoints(12000);
                    long driverUser2Id = db.userDao().insert(driverUser2);

                    // Create driver records
                    BusDriver driver1 = new BusDriver(
                        (int)driverUser1Id,
                        "DL123456",
                        System.currentTimeMillis() + 31536000000L, // 1 year from now
                        5
                    );
                    db.busDriverDao().insert(driver1);

                    BusDriver driver2 = new BusDriver(
                        (int)driverUser2Id,
                        "DL789012",
                        System.currentTimeMillis() + 31536000000L,
                        3
                    );
                    db.busDriverDao().insert(driver2);
                });

                runOnUiThread(() -> {
                    Toast.makeText(this, "Database filled with initial users", Toast.LENGTH_SHORT).show();
                });
            } catch (Exception e) {
                Log.e("SettingsActivity", "Error filling database", e);
                runOnUiThread(() -> {
                    Toast.makeText(this, "Error filling database: " + e.getMessage(), 
                        Toast.LENGTH_SHORT).show();
                });
            }
        });
    }
} 