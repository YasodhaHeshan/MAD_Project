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

    public BusDriver(int id, String firstName, String lastName, String email, String phoneNumber, String password, String licenseNumber, String routeAssigned, int experienceYears, String busAssigned) {
        super(id, firstName, lastName, email, phoneNumber, password);
        this.licenseNumber = licenseNumber;
        this.routeAssigned = routeAssigned;
        this.experienceYears = experienceYears;
        this.busAssigned = busAssigned;
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
}