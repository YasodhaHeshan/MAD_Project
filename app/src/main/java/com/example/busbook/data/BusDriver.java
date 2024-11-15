package com.example.busbook.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "bus_drivers",
        foreignKeys = @ForeignKey(entity = User.class,
                parentColumns = "id",
                childColumns = "user_id",
                onDelete = ForeignKey.CASCADE),
        indices = @Index(value = "user_id"))

public class BusDriver {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "user_id")
    private int userId;

    @ColumnInfo(name = "license_number")
    private String licenseNumber;

    @ColumnInfo(name = "route_assigned")
    private String routeAssigned;

    @ColumnInfo(name = "experience_years")
    private int experienceYears;

    @ColumnInfo(name = "bus_assigned")
    private String busAssigned;

    @ColumnInfo(name = "rating")
    private double rating;

    @ColumnInfo(name = "nic")
    private String nic;

    @ColumnInfo(name = "is_active")
    private boolean isActive;

    @ColumnInfo(name = "bus_number")
    private String busNumber;

    public BusDriver(int userId, String licenseNumber, String nic) {
        this.userId = userId;
        this.licenseNumber = licenseNumber;
        this.nic = nic;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public String getRouteAssigned() {
        return routeAssigned;
    }

    public void setRouteAssigned(String routeAssigned) {
        this.routeAssigned = routeAssigned;
    }


    public int getExperienceYears() {
        return experienceYears;
    }

    public void setExperienceYears(int experienceYears) {
        this.experienceYears = experienceYears;
    }

    public String getBusAssigned() {
        return busAssigned;
    }

    public void setBusAssigned(String busAssigned) {
        this.busAssigned = busAssigned;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getNic() {
        return nic;
    }

    public void setNic(String nic) {
        this.nic = nic;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    public String getBusNumber() {
        return busNumber;
    }

    public void setBusNumber(String busNumber) {
        this.busNumber = busNumber;
    }
}