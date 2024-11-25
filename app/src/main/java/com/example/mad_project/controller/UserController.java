package com.example.mad_project.controller;

import static com.example.mad_project.utils.Validation.hashPassword;

import android.content.Context;
import android.util.Log;

import androidx.room.Room;

import com.example.mad_project.data.AppDatabase;
import com.example.mad_project.data.BusDriver;
import com.example.mad_project.data.BusDriverDao;
import com.example.mad_project.data.BusOwner;
import com.example.mad_project.data.BusOwnerDao;
import com.example.mad_project.data.User;
import com.example.mad_project.data.UserDao;
import com.example.mad_project.utils.SessionManager;
import com.example.mad_project.utils.Validation;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class UserController {

    private final AppDatabase db;
    private final UserDao userDao;
    private final BusDriverDao driverDao;
    private final BusOwnerDao ownerDao;
    private final ExecutorService executorService;
    private final SessionManager sessionManager;

    public UserController(Context context) {
        db = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "mad_project_db").build();
        userDao = db.userDao();
        driverDao = db.busDriverDao();
        ownerDao = db.busOwnerDao();
        executorService = Executors.newSingleThreadExecutor();
        sessionManager = new SessionManager(context);
    }

    public void register(String name, String email, String phone, String password, String role, Consumer<User> callback) {
        executorService.execute(() -> {
            try {
                if (userDao.isEmailExists(email)) {
                    callback.accept(null);
                    return;
                }

                String hashedPassword = hashPassword(password);
                User user = new User(name, email, phone, hashedPassword, role);
                int userId = (int) userDao.insert(user);
                user.setId((int) userId);
                callback.accept(user);
            } catch (Exception e) {
                Log.e("UserController", "Error registering user", e);
                callback.accept(null);
            }
        });
    }

    public void login(String email, String password, Consumer<User> callback) {
        executorService.execute(() -> {
            try {
                User user = userDao.getUserByEmail(email);
                if (user != null && Validation.verifyPassword(password, user.getPassword())) {
                    sessionManager.setLogin(true, email, user.getId(), user.getRole());
                    callback.accept(user);
                } else {
                    callback.accept(null);
                }
            } catch (Exception e) {
                Log.e("UserController", "Error logging in", e);
                callback.accept(null);
            }
        });
    }

    public void registerDriver(String name, String email, String phone, String password,
                             String licenseNumber, long licenseExpiry, int yearsExperience,
                             Consumer<Boolean> callback) {
        register(name, email, phone, password, "driver", user -> {
            if (user == null) {
                callback.accept(false);
                return;
            }

            executorService.execute(() -> {
                try {
                    if (driverDao.isLicenseExists(licenseNumber)) {
                        // Rollback user creation
                        userDao.delete(user);
                        callback.accept(false);
                        return;
                    }

                    BusDriver driver = new BusDriver(user.getId(), licenseNumber, licenseExpiry, yearsExperience);
                    driverDao.insert(driver);
                    callback.accept(true);
                } catch (Exception e) {
                    Log.e("UserController", "Error registering driver", e);
                    userDao.delete(user);
                    callback.accept(false);
                }
            });
        });
    }

    public void registerOwner(String name, String email, String phone, String password,
                            String companyName, String companyRegistration, String taxId,
                            Consumer<Boolean> callback) {
        register(name, email, phone, password, "owner", user -> {
            if (user == null) {
                callback.accept(false);
                return;
            }

            executorService.execute(() -> {
                try {
                    if (ownerDao.isCompanyRegistrationExists(companyRegistration) || 
                        ownerDao.isTaxIdExists(taxId)) {
                        // Rollback user creation
                        userDao.delete(user);
                        callback.accept(false);
                        return;
                    }

                    BusOwner owner = new BusOwner(user.getId(), companyName, companyRegistration, taxId);
                    ownerDao.insert(owner);
                    callback.accept(true);
                } catch (Exception e) {
                    Log.e("UserController", "Error registering owner", e);
                    userDao.delete(user);
                    callback.accept(false);
                }
            });
        });
    }

    public void upgradeToDriver(String licenseNumber, long licenseExpiry, int yearsExperience,
                             Consumer<Boolean> callback) {
        executorService.execute(() -> {
            try {
                // Get current user ID from session
                int userId = sessionManager.getUserId();
                if (userId == -1) {
                    callback.accept(false);
                    return;
                }

                // Check if license exists
                if (driverDao.isLicenseExists(licenseNumber)) {
                    callback.accept(false);
                    return;
                }

                // Use a transaction to ensure both operations succeed or fail together
                db.runInTransaction(() -> {
                    // Update user role to driver
                    User user = userDao.getUserById(userId);
                    user.setRole("driver");
                    userDao.update(user);

                    // Create driver record
                    BusDriver driver = new BusDriver(userId, licenseNumber, licenseExpiry, yearsExperience);
                    driverDao.insert(driver);
                });

                callback.accept(true);
            } catch (Exception e) {
                Log.e("UserController", "Error upgrading to driver", e);
                callback.accept(false);
            }
        });
    }

    public void upgradeToOwner(String companyName, String companyRegistration, String taxId,
                            Consumer<Boolean> callback) {
        executorService.execute(() -> {
            try {
                // Get current user ID from session
                int userId = sessionManager.getUserId();
                if (userId == -1) {
                    callback.accept(false);
                    return;
                }

                // Check if registration number exists
                if (ownerDao.getBusOwnerByRegistration(companyRegistration) != null) {
                    callback.accept(false);
                    return;
                }

                // Use a transaction to ensure both operations succeed or fail together
                db.runInTransaction(() -> {
                    // Update user role to owner
                    User user = userDao.getUserById(userId);
                    user.setRole("owner");
                    userDao.update(user);

                    // Create owner record
                    BusOwner owner = new BusOwner(userId, companyName, companyRegistration, taxId);
                    ownerDao.insert(owner);
                });

                callback.accept(true);
            } catch (Exception e) {
                Log.e("UserController", "Error upgrading to owner", e);
                callback.accept(false);
            }
        });
    }
}