package com.example.mad_project.data;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "schedules",
        foreignKeys = {
                @ForeignKey(entity = Bus.class,
                        parentColumns = "id",
                        childColumns = "bus_id",
                        onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = Route.class,
                        parentColumns = "id",
                        childColumns = "route_id",
                        onDelete = ForeignKey.CASCADE)
        },
        indices = {
                @Index(value = "bus_id"),
                @Index(value = "route_id")
        })
public class Schedule {
    @PrimaryKey(autoGenerate = true)
    private int id;

    
    @ColumnInfo(name = "bus_id")
    private int busId;

    
    @ColumnInfo(name = "route_id")
    private int routeId;

    
    @ColumnInfo(name = "departure_time")
    private String departureTime;

    
    @ColumnInfo(name = "arrival_time")
    private String arrivalTime;

    public Schedule(int busId, int routeId,  String departureTime,  String arrivalTime) {
        this.busId = busId;
        this.routeId = routeId;
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

    public int getRouteId() {
        return routeId;
    }

    public void setRouteId(int routeId) {
        this.routeId = routeId;
    }

    
    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime( String departureTime) {
        this.departureTime = departureTime;
    }

    
    public String getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime( String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }
}