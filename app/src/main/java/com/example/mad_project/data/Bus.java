package com.example.mad_project.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import androidx.room.Relation;
import java.util.List;

@Entity(tableName = "buses",
        foreignKeys = {
                @ForeignKey(entity = BusOwner.class,
                            parentColumns = "id",
                            childColumns = "bus_owner_id",
                            onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = BusDriver.class,
                            parentColumns = "id",
                            childColumns = "bus_driver_id",
                            onDelete = ForeignKey.CASCADE)
        })
public class Bus {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "departure_location")
    private String departureLocation;

    @ColumnInfo(name = "arrival_location")
    private String arrivalLocation;

    @ColumnInfo(name = "departure_time")
    private String departureTime;

    @ColumnInfo(name = "arrival_time")
    private String arrivalTime;

    @ColumnInfo(name = "available_seats")
    private int availableSeats;

    @ColumnInfo(name = "bus_owner_id")
    private int busOwnerId;

    @ColumnInfo(name = "bus_driver_id")
    private int busDriverId;

    @Relation(parentColumn = "id", entityColumn = "bus_id")
    private List<Customer> passengers;

    // Constructor, getters, and setters
    public Bus(int id, String departureLocation, String arrivalLocation, String departureTime, String arrivalTime, int availableSeats, int busOwnerId, int busDriverId, List<Customer> passengers) {
        this.id = id;
        this.departureLocation = departureLocation;
        this.arrivalLocation = arrivalLocation;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.availableSeats = availableSeats;
        this.busOwnerId = busOwnerId;
        this.busDriverId = busDriverId;
        this.passengers = passengers;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDepartureLocation() {
        return departureLocation;
    }

    public void setDepartureLocation(String departureLocation) {
        this.departureLocation = departureLocation;
    }

    public String getArrivalLocation() {
        return arrivalLocation;
    }

    public void setArrivalLocation(String arrivalLocation) {
        this.arrivalLocation = arrivalLocation;
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

    public int getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(int availableSeats) {
        this.availableSeats = availableSeats;
    }

    public int getBusOwnerId() {
        return busOwnerId;
    }

    public void setBusOwnerId(int busOwnerId) {
        this.busOwnerId = busOwnerId;
    }

    public int getBusDriverId() {
        return busDriverId;
    }

    public void setBusDriverId(int busDriverId) {
        this.busDriverId = busDriverId;
    }

    public List<Customer> getPassengers() {
        return passengers;
    }

    public void setPassengers(List<Customer> passengers) {
        this.passengers = passengers;
    }
}