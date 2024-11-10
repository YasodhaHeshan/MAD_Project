package com.example.mad_project.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Route {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String startLocation;
    public String endLocation;
    public String departureTime;
    public String arrivalTime;

    public Route(int id, String startLocation, String endLocation, String departureTime, String arrivalTime) {
        this.id = id;
        this.startLocation = startLocation;
        this.endLocation = endLocation;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
    }
}