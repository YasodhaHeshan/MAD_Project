package com.example.mad_project.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "buses")
public class Bus {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "bus_number")
    private String busNumber;

    @ColumnInfo(name = "start_location")
    private String startLocation;

    @ColumnInfo(name = "end_location")
    private String endLocation;

    @ColumnInfo(name = "departure_time")
    private String departureTime;

    @ColumnInfo(name = "arrival_time")
    private String arrivalTime;

    @ColumnInfo(name = "total_seats")
    private int totalSeats;

    public Bus(String busNumber, String startLocation, String endLocation, String departureTime, String arrivalTime, int totalSeats) {
        this.busNumber = busNumber;
        this.startLocation = startLocation;
        this.endLocation = endLocation;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.totalSeats = totalSeats;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBusNumber() {
        return busNumber;
    }

    public void setBusNumber(String busNumber) {
        this.busNumber = busNumber;
    }

    public String getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(String startLocation) {
        this.startLocation = startLocation;
    }

    public String getEndLocation() {
        return endLocation;
    }

    public void setEndLocation(String endLocation) {
        this.endLocation = endLocation;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public int getTotalSeats() {
        return totalSeats;
    }

    public void setTotalSeats(int totalSeats) {
        this.totalSeats = totalSeats;
    }
}