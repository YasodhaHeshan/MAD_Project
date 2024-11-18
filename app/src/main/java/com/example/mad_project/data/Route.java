package com.example.mad_project.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "routes",
        foreignKeys = @ForeignKey(entity = Bus.class,
                parentColumns = "id",
                childColumns = "bus_id",
                onDelete = ForeignKey.CASCADE),
        indices = @Index(value = "bus_id"))
public class Route {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "bus_id")
    private int busId;

    @ColumnInfo(name = "start_location")
    private String startLocation;

    @ColumnInfo(name = "end_location")
    private String endLocation;

    @ColumnInfo(name = "departure_time")
    private String departureTime;

    @ColumnInfo(name = "arrival_time")
    private String arrivalTime;

    public Route(int busId, String startLocation, String endLocation, String departureTime, String arrivalTime) {
        this.busId = busId;
        this.startLocation = startLocation;
        this.endLocation = endLocation;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBusId() {
        return busId;
    }

    public void setBusId(int busId) {
        this.busId = busId;
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
}