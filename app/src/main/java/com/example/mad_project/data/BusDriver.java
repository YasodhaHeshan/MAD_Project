package com.example.mad_project.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;

@Entity(tableName = "bus_drivers")
public class BusDriver extends User {
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

    public BusDriver(int id, String firstName, String lastName, String email, String phoneNumber, String password, String licenseNumber, String routeAssigned, int experienceYears, String busAssigned, double rating, String nic) {
        super(id, firstName, lastName, email, phoneNumber, password);
        this.licenseNumber = licenseNumber;
        this.routeAssigned = routeAssigned;
        this.experienceYears = experienceYears;
        this.busAssigned = busAssigned;
        this.rating = rating;
        this.nic = nic;
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
}