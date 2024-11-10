package com.example.mad_project.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;

@Entity(tableName = "bus_owners")
public class BusOwner extends User {
    @ColumnInfo(name = "company_name")
    private String companyName;

    @ColumnInfo(name = "fleet_size")
    private int fleetSize;

    @ColumnInfo(name = "years_in_business")
    private int yearsInBusiness;

    @ColumnInfo(name = "rating")
    private double rating;

    public BusOwner(int id, String firstName, String lastName, String email, String phoneNumber, String password, String companyName, int fleetSize, int yearsInBusiness, double rating) {
        super(id, firstName, lastName, email, phoneNumber, password);
        this.companyName = companyName;
        this.fleetSize = fleetSize;
        this.yearsInBusiness = yearsInBusiness;
        this.rating = rating;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public int getFleetSize() {
        return fleetSize;
    }

    public void setFleetSize(int fleetSize) {
        this.fleetSize = fleetSize;
    }

    public int getYearsInBusiness() {
        return yearsInBusiness;
    }

    public void setYearsInBusiness(int yearsInBusiness) {
        this.yearsInBusiness = yearsInBusiness;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }
}