package com.example.mad_project.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Schedule {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public int busId;
    public int routeId;
    public String departureTime;
    public String arrivalTime;

    public Schedule(int id, int busId, int routeId, String departureTime, String arrivalTime) {
        this.id = id;
        this.busId = busId;
        this.routeId = routeId;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
    }
}